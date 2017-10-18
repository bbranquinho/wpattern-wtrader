package org.wtrader.cep.data.wrappers;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.wtrader.cep.data.repositories.IStockRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.beans.StockDataBean;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.interfaces.IStockRecordData;

@Named
public class StockRecordWrapper implements IStockRecordData {

	@Inject
	private IStockRecordRepository stockRecordRepository;

	@Inject
	private IStockRepository stockRepository;

	@Override
	public List<StockRecordEntity> findByTradeDateBetweenAndStockOrderByTradeDateAsc(Date unitaryQuotationSince,
			Date businessesWithUp, StockEntity stock) {
		return this.stockRecordRepository.findByTradeDateBetweenAndStockOrderByTradeDateAsc(unitaryQuotationSince,
				businessesWithUp, stock);
	}

	@Override
	public List<Date> groupByTradeDate() {
		return this.stockRecordRepository.groupByTradeDate();
	}

	@Override
	public List<StockRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock) {
		return this.stockRecordRepository.findByStockOrderByTradeDateAsc(stock);
	}

	@Override
	public List<StockRecordEntity> findByStock(StockEntity stock, PageRequest pageRequest) {
		return this.stockRecordRepository.findByStock(stock, pageRequest);
	}

	@Override
	public int countByStock(StockEntity stock) {
		return this.stockRecordRepository.countByStock(stock);
	}

	@Override
	public List<StockRecordEntity> findByTradeDateBetweenAndStock(Date initialDate, Date finalDate, StockEntity stock, Sort sort) {
		return this.stockRecordRepository.findByTradeDateBetweenAndStock(initialDate, finalDate, stock, sort);
	}

	@Override
	public int countByTradeDate(Date initialDate, Date finalDate) {
		return this.stockRecordRepository.countByTradeDate(initialDate, finalDate);
	}

	@Override
	public int countByTradeDateAndStock(Date initialDate, Date finalDate, StockEntity stockEntity) {
		return this.stockRecordRepository.countByTradeDateAndStock(initialDate, finalDate, stockEntity);
	}

	@Override
	public List<StockDataBean> findStockDataBetweenAndBeforeTradeDate(String stockName, int numDataBefore, int numDataAfter,
			Date initialDate, Date finalDate) {
		StockEntity stock = this.stockRepository.findByName(stockName);

		List<StockDataBean> stockData = this.stockRecordRepository.findStockDataByTradeDateBefore(stock, initialDate, new PageRequest(0, numDataBefore));

		stockData.sort(new Comparator<StockDataBean>() {
			@Override
			public int compare(StockDataBean obj1, StockDataBean obj2) {
				return obj1.getDate().compareTo(obj2.getDate());
			}
		});

		stockData.addAll(this.stockRecordRepository.findStockDataByTradeDateBetween(stock, initialDate, finalDate));
		stockData.addAll(this.stockRecordRepository.findStockDataByTradeDateAfter(stock, initialDate, new PageRequest(0, numDataAfter)));

		return stockData;
	}

}
