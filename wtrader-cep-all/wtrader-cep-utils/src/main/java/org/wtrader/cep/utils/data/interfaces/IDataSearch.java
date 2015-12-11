package org.wtrader.cep.utils.data.interfaces;

import java.util.Date;
import java.util.List;

import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.enums.SortEnum;
import org.wtrader.cep.utils.enums.DataType;

public interface IDataSearch {

	public StockRecordEntity findByStockAndTradeDate(String stockName, Date tradeDate);

	// DATES

	public List<Date> findTradeDateByTradeDateBetween(Date start, Date end);

	// COUNT

	public int countRecordsByTradeDate(Date start, Date end);

	// BETWEEN

	public double[] findBetween(String stockName, Date start, Date end);

	public double[] findBetweenByDataType(String stockName, DataType dataType, Date start, Date end);

	public double[] findBetweenByDataType(String stockName, DataType dataType, Date start, Date end, SortEnum sort);

	// BETWEEN AND ABOVE

	public double[] findBetweenAndAbove(String stockName, Date start, Date end, int numRecords);

	public double[] findBetweenAndAboveByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords);

	public double[] findBetweenAndAboveByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords, SortEnum sort);

	// BETWEEN AND BELOW

	public double[] findBetweenAndBelow(String stockName, Date start, Date end, int numRecords);

	public double[] findBetweenAndBelowByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords);

	public double[] findBetweenAndBelowByDataType(String stockName, DataType dataType, Date start, Date end, int numRecords, SortEnum sort);

	// ABOVE (ALL)

	public double[] findAbove(String stockName, Date start);

	public double[] findAboveByDataType(String stockName, DataType dataType, Date start);

	public double[] findAboveByDataType(String stockName, DataType dataType, Date start, SortEnum sort);

	// ABOVE (NUM_RECORDS)

	public double[] findAbove(String stockName, Date start, int numRecords);

	public double[] findAboveByDataType(String stockName, DataType dataType, Date start, int numRecords);

	public double[] findAboveByDataType(String stockName, DataType dataType, Date start, int numRecords, SortEnum sort);

	// BELOW (ALL)

	public double[] findBelow(String stockName, Date end);

	public double[] findBelowByDataType(String stockName, DataType dataType, Date end);

	public double[] findBelowByDataType(String stockName, DataType dataType, Date end, SortEnum sort);

	// BELOW (NUM_RECORDS)

	public double[] findBelow(String stockName, Date end, int numRecords);

	public double[] findBelowByDataType(String stockName, DataType dataType, Date end, int numRecords);

	public double[] findBelowByDataType(String stockName, DataType dataType, Date end, int numRecords, SortEnum sort);

	// BELOW (DATE)

	/**
	 * Return the first date below the <b>date</b> to a stock.
	 *
	 * @param stockName
	 * @param date
	 *
	 * @return
	 */
	public Date findDateBelowByTradeDate(String stockName, Date date);

}
