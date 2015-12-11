package org.wtrader.cep.data;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.wtrader.cep.data.repositories.IStockRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.enums.SortEnum;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;

@Named
public class DataSearch implements IDataSearch {

	private static final Logger LOGGER = Logger.getLogger(DataSearch.class);

	public static final DataType DATA_TYPE_DEFAULT = DataType.CLOSING_NEGOTIATION_PRICE;

	public static final SortEnum SORT_DEFAULT = SortEnum.ASCENDING;

	private static final String SORT_FIELD_DEFAULT = "tradeDate";

	@Inject
	private IStockRepository stockRepository;

	@Inject
	private IStockRecordRepository stockRecordRepository;

	public DataSearch() {
	}

	///////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IDataStoreByStockAndDate)
	///////////////////////////////////////////////////////////////////////////

	@Override
	public StockRecordEntity findByStockAndTradeDate(String stockName, Date tradeDate) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		List<StockRecordEntity> stockRecords = this.stockRecordRepository.findByStockAndTradeDate(stock, tradeDate);

		if ((stockRecords == null) ||(stockRecords.size() < 1)) {
			return null;
		}

		if (stockRecords.size() > 1) {
			LOGGER.error(String.format("More than 1 field founded to [%s][%s].", stockName, tradeDate));
		}

		return stockRecords.get(0);
	}

	// DATES

	@Override
	public List<Date> findTradeDateByTradeDateBetween(Date start, Date end) {
		return this.stockRecordRepository.findDatesByTradeDateBetween(start, end);
	}

	// COUNT

	@Override
	public int countRecordsByTradeDate(Date start, Date end) {
		return this.stockRecordRepository.countByTradeDate(start, end);
	}

	// BETWEEN

	@Override
	public double[] findBetween(String stockName, Date start, Date end) {
		return this.findBetweenByDataType(stockName, DATA_TYPE_DEFAULT, start, end, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenByDataType(String stockName, DataType dataType, Date start, Date end) {
		return this.findBetweenByDataType(stockName, dataType, start, end, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenByDataType(String stockName, DataType dataType, Date start, Date end, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> records = this.stockRecordRepository.findByTradeDateBetweenAndStock(start, end, stock, direction);

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// BETWEEN AND ABOVE

	@Override
	public double[] findBetweenAndAbove(String stockName, Date start, Date end, int numRecords) {
		return this.findBetweenAndAboveByDataType(stockName, DATA_TYPE_DEFAULT, start, end, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenAndAboveByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords) {
		return this.findBetweenAndAboveByDataType(stockName, dataType, start, end, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenAndAboveByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> recordsBetween = this.stockRecordRepository.findByTradeDateBetweenAndStock(start, end, stock, direction);

		// Add one day.
		end = this.addDays(end, 1);

		List<StockRecordEntity> recordsAbove = this.stockRecordRepository.findByTradeDateGreaterThanAndStock(end, stock, new PageRequest(0, numRecords, new Sort(Direction.ASC, SORT_FIELD_DEFAULT)));

		recordsAbove = this.sortRecordsByDate(recordsAbove, sort);

		List<StockRecordEntity> records = this.joinLists(recordsBetween, recordsAbove, sort);

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// BETWEEN AND BELOW

	@Override
	public double[] findBetweenAndBelow(String stockName, Date start, Date end, int numRecords) {
		return this.findBetweenAndBelowByDataType(stockName, DATA_TYPE_DEFAULT, start, end, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenAndBelowByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords) {
		return this.findBetweenAndBelowByDataType(stockName, dataType, start, end, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBetweenAndBelowByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> recordsBetween = this.stockRecordRepository.findByTradeDateBetweenAndStock(start, end, stock, direction);

		// Subtract one day.
		start = this.addDays(start, -1);

		List<StockRecordEntity> recordsBelow = this.stockRecordRepository.findByTradeDateLessThanAndStock(start, stock, new PageRequest(0, numRecords, new Sort(Direction.DESC, SORT_FIELD_DEFAULT)));

		recordsBelow = this.sortRecordsByDate(recordsBelow, sort);

		List<StockRecordEntity> records = this.joinLists(recordsBelow, recordsBetween, sort);

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// ABOVE (ALL)

	@Override
	public double[] findAbove(String stockName, Date date) {
		return this.findAboveByDataType(stockName, DATA_TYPE_DEFAULT, date, SORT_DEFAULT);
	}

	@Override
	public double[] findAboveByDataType(String stockName, DataType dataType, Date date) {
		return this.findAboveByDataType(stockName, dataType, date, SORT_DEFAULT);
	}

	@Override
	public double[] findAboveByDataType(String stockName, DataType dataType, Date date, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> records = this.stockRecordRepository.findByTradeDateGreaterThanAndStock(date, stock, direction);

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// ABOVE (NUM_RECORDS)

	@Override
	public double[] findAbove(String stockName, Date date, int numRecords) {
		return this.findAboveByDataType(stockName, DATA_TYPE_DEFAULT, date, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findAboveByDataType(String stockName, DataType dataType, Date date, int numRecords) {
		return this.findAboveByDataType(stockName, dataType, date, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findAboveByDataType(String stockName, DataType dataType, Date date, int numRecords, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> records = this.stockRecordRepository.findByTradeDateGreaterThanAndStock(date, stock, new PageRequest(0, numRecords, direction));

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// BELOW (ALL)

	@Override
	public double[] findBelow(String stockName, Date date) {
		return this.findBelowByDataType(stockName, DATA_TYPE_DEFAULT, date, SORT_DEFAULT);
	}

	@Override
	public double[] findBelowByDataType(String stockName, DataType dataType, Date date) {
		return this.findBelowByDataType(stockName, dataType, date, SORT_DEFAULT);
	}

	@Override
	public double[] findBelowByDataType(String stockName, DataType dataType, Date date, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> records = this.stockRecordRepository.findByTradeDateLessThanAndStock(date, stock, direction);

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// BELOW (NUM_RECORDS)

	@Override
	public double[] findBelow(String stockName, Date date, int numRecords) {
		return this.findBelowByDataType(stockName, DATA_TYPE_DEFAULT, date, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBelowByDataType(String stockName, DataType dataType, Date date, int numRecords) {
		return this.findBelowByDataType(stockName, dataType, date, numRecords, SORT_DEFAULT);
	}

	@Override
	public double[] findBelowByDataType(String stockName, DataType dataType, Date date, int numRecords, SortEnum sort) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		Sort direction = WPatternPhDDatabaseWrapper.parseSort(sort, SORT_FIELD_DEFAULT);

		List<StockRecordEntity> records = this.stockRecordRepository.findByTradeDateLessThanAndStock(date, stock, new PageRequest(0, numRecords, direction));

		return WPatternPhDDatabaseWrapper.parseRecord(dataType, records);
	}

	// BELOW (DATE)

	@Override
	public Date findDateBelowByTradeDate(String stockName, Date date) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		List<Date> stockRecords = this.stockRecordRepository.findDatesByDate(stock, date, new PageRequest(0, 1));

		if (stockRecords.size() != 1) {
			LOGGER.error(String.format("Invalid amount of stock records [%s], must be just one.", stockRecords));
		}

		return stockRecords.get(0);
	}



	///////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	///////////////////////////////////////////////////////////////////////////

	private Date addDays(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);

		return calendar.getTime();
	}

	private List<StockRecordEntity> sortRecordsByDate(List<StockRecordEntity> records, final SortEnum direction) {
		Collections.sort(records, new Comparator<StockRecordEntity>() {
			@Override
			public int compare(StockRecordEntity record1, StockRecordEntity record2) {
				if (direction == SortEnum.ASCENDING) {
					return record1.getTradeDate().compareTo(record2.getTradeDate());
				} else {
					return record2.getTradeDate().compareTo(record1.getTradeDate());
				}
			}
		});

		return records;
	}

	private List<StockRecordEntity> joinLists(List<StockRecordEntity> records1, List<StockRecordEntity> records2, SortEnum sort) {
		if (sort == SortEnum.ASCENDING) {
			records1.addAll(records2);
			return records1;
		} else {
			records2.addAll(records1);
			return records2;
		}
	}

}
