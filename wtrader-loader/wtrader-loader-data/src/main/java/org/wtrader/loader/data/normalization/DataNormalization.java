package org.wtrader.loader.data.normalization;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.interfaces.IBenefitStockData;
import org.wtrader.cep.utils.data.interfaces.ICompanyData;
import org.wtrader.cep.utils.data.interfaces.IDataStorage;
import org.wtrader.cep.utils.data.interfaces.IIndexRecordData;
import org.wtrader.cep.utils.data.interfaces.IStockData;
import org.wtrader.cep.utils.data.interfaces.IStockRecordData;
import org.wtrader.loader.utils.interfaces.IDataNormalization;
import org.wtrader.loader.utils.interfaces.IDatabase;
import org.wtrader.loader.utils.properties.ILoaderProperties;

@Named
public class DataNormalization implements IDataNormalization {

	private static final Logger LOGGER = Logger.getLogger(DataNormalization.class);

	@Inject
	private IBenefitStockData benefitStockData;

	@Inject
	private ICompanyData companyData;

	@Inject
	private IIndexRecordData indexRecordData;

	@Inject
	private IStockData stockData;

	@Inject
	private IStockRecordData stockRecordData;

	@Inject
	private ILoaderProperties dataLoaderProperties;

	@Inject
	private IDataStorage dataStorage;

	@Inject
	private IDatabase database;

	@Override
	public void normalize() {
		// Update any wrong unitary quotation.
		if (this.dataLoaderProperties.isNormalizeUpdateUnitaryQuotationSince()) {
			this.updateUnitaryQuotationSince();
			this.database.backup("unitary-quotation-since");
		}

		// Remove companies when not found stock records.
		if (this.dataLoaderProperties.isNormalizeCleanUpCompanyRecords()) {
			this.cleanUpCompanyRecords();
			this.database.backup("clean-company-records");
		}

		// Normalize all records.
		if (this.dataLoaderProperties.isNormalizeStockRecords()) {
			this.normalizeStockRecords();
			this.database.backup("normalize-stock-records");
		}

		// Fill empty stock records.
		if (this.dataLoaderProperties.isNormalizeFillEmptyStockRecords()) {
			this.fillEmptyStockRecords();
			this.database.backup("fill-empty-stock-records");
		}
	}

	private void updateUnitaryQuotationSince() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Updating unitary quotation since.");
		}

		Date currentDate = new Date();

		List<CompanyEntity> companies = this.companyData.findByUnitaryQuotationSince(currentDate);

		for (CompanyEntity company : companies) {
			for (StockEntity stock : this.stockData.findByCompany(company)) {
				List<StockRecordEntity> firstRecord = this.stockRecordData.findByStock(stock, new PageRequest(0, 1, new Sort(Direction.ASC, "tradeDate")));

				if (firstRecord.size() == 1) {
					company.setUnitaryQuotationSince(this.subtractOneDay(firstRecord.get(0).getTradeDate()));

					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Updating the unitary quotation since of the stock [%s] to company [%s].", stock, company));
					}

					try {
						this.dataStorage.saveCompany(company);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						LOGGER.error(String.format("Error to save the record [%s].", company));
					}
				} else {
					LOGGER.warn(String.format("Records not founded to stock [%s].", stock));
				}
			}
		}

	}

	private Date subtractOneDay(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -1);

		return calendar.getTime();
	}

	private void cleanUpCompanyRecords() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Clean up company records.");
		}

		for (CompanyEntity company : this.companyData.findAll()) {
			for (StockEntity stock : this.stockData.findByCompany(company)) {
				if ((this.stockRecordData.countByStock(stock) <= 0) && (this.indexRecordData.countByStock(stock) <= 0)) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Removing from database the stock [%s].", stock));
					}

					this.dataStorage.deleteStock(stock);
				}
			}

			if (this.stockData.countByCompany(company) <= 0) {
				for (BenefitStockEntity benefit : this.benefitStockData.findByCompany(company)) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Removing the benefit stock [%s].", benefit));
					}

					this.dataStorage.deleteBenefitStock(benefit);
				}

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Removing the company [%s].", company));
				}

				this.dataStorage.deleteCompany(company);
			}
		}
	}

	private void normalizeStockRecords() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Normalize stock records.");
		}

		for (StockEntity stock : this.stockData.findAll()) {
			if (!stock.getWasNormalized()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Normalizing stock [%s].", stock.getName()));
				}

				List<BenefitStockEntity> benefitsStocks = this.benefitStockData.findByCompanyOrderByBusinessesWithUpAsc(stock.getCompany());

				if (benefitsStocks.size() > 0) {
					this.normalizeStock(stock, benefitsStocks);
				} else {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Stock [%s] without benefits.", stock.getName()));
					}
				}
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Stock [%s] already normalized.", stock.getName()));
				}
			}
		}
	}

	private void normalizeStock(StockEntity stock, List<BenefitStockEntity> benefitsStocks) {
		if (stock.getCompany().getUnitaryQuotationSince() == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Stock [%s] without unitary quotation since.", stock.getName()));
			}
			return;
		}

		List<StockRecordEntity> stockRecords = this.stockRecordData.findByTradeDateBetweenAndStockOrderByTradeDateAsc
				(stock.getCompany().getUnitaryQuotationSince(), benefitsStocks.get(benefitsStocks.size() - 1).getBusinessesWithUp(), stock);

		for (StockRecordEntity stockRecord : stockRecords) {
			for (BenefitStockEntity benefitStock : benefitsStocks) {
				if (benefitStock.getBusinessesWithUp().compareTo(stockRecord.getTradeDate()) >= 0) {
					this.normalizeStockRecord(benefitStock, stockRecord);
				}
			}

			try {
				this.dataStorage.saveStockRecord(stockRecord);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				LOGGER.error(String.format("Error to save the record [%s].", stockRecord));
			}
		}

		stock.setWasNormalized(true);

		try {
			this.dataStorage.saveStock(stock);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the record [%s].", stock));
		}
	}

	private void normalizeStockRecord(BenefitStockEntity benefitStock, StockRecordEntity stockRecord) {
		if (benefitStock.getFactor() == null) {
			return;
		}

		switch (benefitStock.getType()) {
		case BONIFICATION:
			this.splitRecord(benefitStock.getFactor(), stockRecord);
			break;

		case FISSION:
			this.splitRecord(benefitStock.getFactor(), stockRecord);
			break;

		case INPLIT:
			this.inplitRecord(benefitStock.getFactor(), stockRecord);
			break;

		case RETURN_CAPITAL:
			// Do nothing.
			break;

		case SPLIT:
			this.splitRecord(benefitStock.getFactor(), stockRecord);
			break;

		default:
			LOGGER.error(String.format("Type [%s] not treated.", benefitStock.getType()));
			break;
		}
	}

	private void splitRecord(Double factor, StockRecordEntity stockRecord) {
		double ratio = 1.0 + (factor / 100.0);

		// Avoid division by zero.
		if (ratio == 0.0) {
			return;
		}

		stockRecord.setAverageNegotiationPrice(stockRecord.getAverageNegotiationPrice() / ratio);
		stockRecord.setClosingNegotiationPrice(stockRecord.getClosingNegotiationPrice() / ratio);
		stockRecord.setHighestBuyOfferPrice(stockRecord.getHighestBuyOfferPrice() / ratio);
		stockRecord.setHighestNegotiationPrice(stockRecord.getHighestNegotiationPrice() / ratio);
		stockRecord.setLowestNegotiationPrice(stockRecord.getLowestNegotiationPrice() / ratio);
		stockRecord.setLowestSellOfferPrice(stockRecord.getLowestSellOfferPrice() / ratio);
		stockRecord.setStartPrice(stockRecord.getStartPrice() / ratio);
		stockRecord.setTotalVolume(stockRecord.getTotalVolume() / ratio);
		stockRecord.setTotalBusiness((long)(stockRecord.getTotalBusiness() / ratio));
		stockRecord.setTotalNegotiation((long) (stockRecord.getTotalNegotiation() / ratio));
	}

	private void inplitRecord(Double factor, StockRecordEntity stockRecord) {
		if (factor == 0.0) {
			return;
		}

		stockRecord.setAverageNegotiationPrice(stockRecord.getAverageNegotiationPrice() * factor);
		stockRecord.setClosingNegotiationPrice(stockRecord.getClosingNegotiationPrice() * factor);
		stockRecord.setHighestBuyOfferPrice(stockRecord.getHighestBuyOfferPrice() * factor);
		stockRecord.setHighestNegotiationPrice(stockRecord.getHighestNegotiationPrice() * factor);
		stockRecord.setLowestNegotiationPrice(stockRecord.getLowestNegotiationPrice() * factor);
		stockRecord.setLowestSellOfferPrice(stockRecord.getLowestSellOfferPrice() * factor);
		stockRecord.setStartPrice(stockRecord.getStartPrice() * factor);
		stockRecord.setTotalVolume(stockRecord.getTotalVolume() * factor);
		stockRecord.setTotalBusiness((long)(stockRecord.getTotalBusiness() * factor));
		stockRecord.setTotalNegotiation((long) (stockRecord.getTotalNegotiation() * factor));
	}

	private void fillEmptyStockRecords() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Fill empty stock records.");
		}

		List<Date> allDates = this.stockRecordData.groupByTradeDate();

		for (StockEntity stock : this.stockData.findAll()) {
			List<StockRecordEntity> stockRecords = this.stockRecordData.findByStockOrderByTradeDateAsc(stock);

			if (stockRecords.size() > 0) {
				this.normalizeStockRecord(stock, allDates, stockRecords);
			} else {
				List<IndexRecordEntity> indexRecords = this.indexRecordData.findByStockOrderByTradeDateAsc(stock);

				this.normalizeIndexRecord(stock, allDates, indexRecords);
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Filled empty records of the stock [%s].", stock.getName()));
			}
		}
	}

	private void normalizeIndexRecord(StockEntity stock, List<Date> allDates, List<IndexRecordEntity> indexRecords) {
		this.updateEmptyRecordsPercentual(stock, allDates, indexRecords);

		int currentIndex = allDates.indexOf(indexRecords.get(0).getTradeDate());

		for (IndexRecordEntity indexRecord : indexRecords) {
			Date newDate;
			while (indexRecord.getTradeDate().compareTo(newDate = allDates.get(currentIndex++)) != 0) {
				this.addNewIndexRecord(indexRecord, newDate);
			}
		}

		IndexRecordEntity lastStockRecord = indexRecords.get(indexRecords.size() - 1);

		for (int i = currentIndex; i < allDates.size(); i++) {
			this.addNewIndexRecord(lastStockRecord, allDates.get(i));
		}

		stock.setWasNormalized(true);

		try {
			this.dataStorage.saveStock(stock);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the record [%s].", stock));
		}
	}

	private void normalizeStockRecord(StockEntity stock, List<Date> allDates, List<StockRecordEntity> stockRecords) {
		this.updateEmptyRecordsPercentual(stock, allDates, stockRecords);

		int currentIndex = allDates.indexOf(stockRecords.get(0).getTradeDate());

		for (StockRecordEntity stockRecord : stockRecords) {
			Date newDate;
			while (stockRecord.getTradeDate().compareTo(newDate = allDates.get(currentIndex++)) != 0) {
				this.addNewStockRecord(stockRecord, newDate);
			}
		}

		StockRecordEntity lastStockRecord = stockRecords.get(stockRecords.size() - 1);

		for (int i = currentIndex; i < allDates.size(); i++) {
			this.addNewStockRecord(lastStockRecord, allDates.get(i));
		}

		stock.setWasNormalized(true);

		try {
			this.dataStorage.saveStock(stock);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the record [%s].", stock));
		}
	}

	private void addNewStockRecord(StockRecordEntity stockRecord, Date newDate) {
		StockRecordEntity newStockRecord = stockRecord.clone();

		newStockRecord.setTradeDate(newDate);
		newStockRecord.setId(null);

		try {
			this.dataStorage.saveStockRecord(newStockRecord);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the record [%s].", newStockRecord));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Added a new stock record [%s].", newStockRecord));
		}
	}

	private void addNewIndexRecord(IndexRecordEntity indexRecord, Date newDate) {
		IndexRecordEntity newIndexRecord = indexRecord.clone();

		newIndexRecord.setTradeDate(newDate);
		newIndexRecord.setId(null);

		try {
			this.dataStorage.saveIndexRecord(newIndexRecord);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the index record [%s].", newIndexRecord));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Added a new index record [%s].", newIndexRecord));
		}
	}

	private void updateEmptyRecordsPercentual(StockEntity stock, List<Date> allDates, List<?> stockRecords) {
		if (stock.getEmptyRecordsPercentual() != null) {
			return;
		}

		if ((stockRecords == null) || (stockRecords.size() <= 0)) {
			LOGGER.warn(String.format("Stock without records [%s].", stock));
			return;
		}

		Date firstTradeDate = null;
		Date lastTradeDate = null;

		if (stockRecords.get(0) instanceof StockRecordEntity) {
			firstTradeDate = ((StockRecordEntity)stockRecords.get(0)).getTradeDate();
			lastTradeDate = ((StockRecordEntity)stockRecords.get(stockRecords.size() - 1)).getTradeDate();
		} else if (stockRecords.get(0) instanceof IndexRecordEntity) {
			firstTradeDate = ((IndexRecordEntity)stockRecords.get(0)).getTradeDate();
			lastTradeDate = ((IndexRecordEntity)stockRecords.get(stockRecords.size() - 1)).getTradeDate();
		} else {
			LOGGER.error(String.format("Unrecornized type of list [%s].", stockRecords.getClass()));
			return;
		}

		int firstIndex = allDates.indexOf(firstTradeDate);
		int lastIndex = allDates.indexOf(lastTradeDate);
		double size = lastIndex - firstIndex + 1.0d;

		if (size <= 0.0d) {
			LOGGER.error(String.format("Amount of dates is wrong to stock [%s].", stock.getName()));

			size = 0.0d;
		}

		stock.setEmptyRecordsPercentual((double) size / (allDates.size() - firstIndex));

		if (Double.isInfinite(stock.getEmptyRecordsPercentual()) || Double.isNaN(stock.getEmptyRecordsPercentual())) {
			LOGGER.error(String.format("Stock with \"EmptyRecordsPercentual\" infinite or NaN [%s]."
					+ "First Index [%s], last Index [%s] and size [%s].", stock, firstIndex, lastIndex, size));
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Updating the empty records percentual [%s].", stock));
		}

		// Update the empty records percentual
		try {
			this.dataStorage.saveStock(stock);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(String.format("Error to save the record [%s].", stock));
		}
	}
}
