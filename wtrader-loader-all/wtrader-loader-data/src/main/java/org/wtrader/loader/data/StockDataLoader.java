package org.wtrader.loader.data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.interfaces.IDataStorage;
import org.wtrader.cep.utils.data.interfaces.IStockData;
import org.wtrader.loader.data.enums.ParserPositionEnum;
import org.wtrader.loader.utils.beans.StockRecordBean;
import org.wtrader.loader.utils.interfaces.IStockDataLoader;

@Named
public class StockDataLoader implements IStockDataLoader {

	private Logger logger = Logger.getLogger(this.getClass());

	private static final String DATE_PATTERN = "yyyyMMdd";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

	private static final String FILE_FORMAT = "UTF-8";

	private static final int RECORD_SIZE = 245;

	@Inject
	private IDataStorage dataStorage;

	@Inject
	private IStockData stockData;

	public StockDataLoader() {
	}

	@Override
	public void loadFile(String fullnamePath, final List<String> acceptedStocks) throws IOException {
		if (this.logger.isInfoEnabled()) {
			this.logger.info(String.format("Load data from file name path [%s].", fullnamePath));
		}

		LineIterator lineIterator = FileUtils.lineIterator(new File(fullnamePath), FILE_FORMAT);

		while (lineIterator.hasNext()) {
			final String line = lineIterator.next().toLowerCase().trim();
			StockRecordBean record = null;

			try {
				if (line.length() >= RECORD_SIZE) {
					record = StockDataLoader.this.parseLine(line);

					if ((acceptedStocks == null) || acceptedStocks.isEmpty() || acceptedStocks.contains(record.getStockName().toLowerCase())) {
						if (record.getBdi() == 2) {
							StockDataLoader.this.loadRecord(record);
						} else {
							if (this.logger.isTraceEnabled()) {
								this.logger.trace(String.format("Record [%s] with DBI not equals 2.", record));
							}
						}
					}
				} else {
					if (this.logger.isInfoEnabled()) {
						this.logger.info(String.format("First or Last line aren´t loaded [%s].", line));
					}
				}
			} catch (Exception e) {
				this.logger.error(String.format("Problem to parse or save the record [%s] and the bean [%s].", line, record));
				this.logger.error(e.getMessage(), e);
			}
		}
	}

	private StockRecordBean parseLine(String line) throws ParseException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(String.format("Parse the record [%s].", line));
		}

		StockRecordBean record = new StockRecordBean();
		String field;

		for (ParserPositionEnum value : ParserPositionEnum.values()) {
			field = line.substring(value.getStartPosition(), value.getEndPosition()).trim().toUpperCase();

			switch (value) {
			case AVERAGE_NEGOTIATION_PRICE:
				record.setAverageNegotiationPrice(this.addPointAndParseToDouble(field));
				break;

			case BDI:
				record.setBdi(Long.parseLong(field));
				break;

			case CLOSING_NEGOTIATION_PRICE:
				record.setClosingNegotiationPrice(this.addPointAndParseToDouble(field));
				break;

			case DATE:
				record.setTradeDate(DATE_FORMAT.parse(field));
				break;

			case HIGHEST_BUY_OFFER_PRICE:
				record.setHighestBuyOfferPrice(this.addPointAndParseToDouble(field));
				break;

			case HIGHEST_NEGOTIATION_PRICE:
				record.setHighestNegotiationPrice(this.addPointAndParseToDouble(field));
				break;

			case LOWEST_NEGOTIATION_PRICE:
				record.setLowestNegotiationPrice(this.addPointAndParseToDouble(field));
				break;

			case LOWEST_SELL_OFFER_PRICE:
				record.setLowestSellOfferPrice(this.addPointAndParseToDouble(field));
				break;

			case START_PRICE:
				record.setStartPrice(this.addPointAndParseToDouble(field));
				break;

			case STOCK_NAME:
				record.setStockName(field);
				break;

			case TOTAL_BUSINESS:
				record.setTotalBusiness(Long.parseLong(field));
				break;

			case TOTAL_NEGOTIATION:
				record.setTotalNegotiation(Long.parseLong(field));
				break;

			case TOTAL_VOLUME:
				record.setTotalVolume(this.addPointAndParseToDouble(field));
				break;
			}
		}

		return record;
	}

	private Double addPointAndParseToDouble(String number) {
		return Double.parseDouble(number.substring(0, number.length() - 2) + "." + number.substring(number.length() - 2));
	}

	private StockRecordEntity parseToStockRecord(StockRecordBean record, StockEntity stock) {
		StockRecordEntity stockRecordEntity = new StockRecordEntity();

		stockRecordEntity.setTradeDate(record.getTradeDate());
		stockRecordEntity.setStock(stock);
		stockRecordEntity.setAverageNegotiationPrice(record.getAverageNegotiationPrice());
		stockRecordEntity.setClosingNegotiationPrice(record.getClosingNegotiationPrice());
		stockRecordEntity.setHighestBuyOfferPrice(record.getHighestBuyOfferPrice());
		stockRecordEntity.setHighestNegotiationPrice(record.getHighestNegotiationPrice());
		stockRecordEntity.setLowestNegotiationPrice(record.getLowestNegotiationPrice());
		stockRecordEntity.setLowestSellOfferPrice(record.getLowestSellOfferPrice());
		stockRecordEntity.setStartPrice(record.getStartPrice());
		stockRecordEntity.setTotalBusiness(record.getTotalBusiness());
		stockRecordEntity.setTotalNegotiation(record.getTotalNegotiation());
		stockRecordEntity.setTotalVolume(record.getTotalVolume());

		return stockRecordEntity;
	}

	private void loadRecord(StockRecordBean record) {
		StockEntity stockEntity = this.retrieveStock(record);

		StockRecordEntity stockRecordEntity = this.parseToStockRecord(record, stockEntity);

		stockRecordEntity = this.dataStorage.saveStockRecord(stockRecordEntity);

		if (this.logger.isTraceEnabled()) {
			this.logger.trace(String.format("Record saved [%s].", stockRecordEntity));
		}
	}

	private StockEntity retrieveStock(StockRecordBean record) {
		StockEntity stockEntity = this.stockData.findByName(record.getStockName());

		if (stockEntity == null) {
			stockEntity = new StockEntity();
			stockEntity.setName(record.getStockName());
			stockEntity = this.dataStorage.saveStock(stockEntity);
		}

		return stockEntity;
	}

}
