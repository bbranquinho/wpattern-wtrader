package org.wtrader.test.cep.data.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.wtrader.cep.data.repositories.IStockRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.beans.StockDataBean;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class StockRecordRepositoryTest extends AbstractCepDatabaseTest {

	private Logger logger = Logger.getLogger(this.getClass());

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	private IStockRecordRepository stockRecordRepository;

	@Inject
	private IStockRepository stockRepository;

	@Test
	public void testFindAll() {
		List<StockRecordEntity> stockRecords = this.stockRecordRepository.findAll();

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(stockRecords);
		}
	}

	@Test
	public void testPageable() {
		Page<StockRecordEntity> stocks = this.stockRecordRepository.findAll(new PageRequest(0, 20));
		Iterator<StockRecordEntity> stocksIterator = stocks.iterator();

		while (stocksIterator.hasNext()) {
			this.logger.debug(stocksIterator.next().getClosingNegotiationPrice());
		}
	}

	@Test
	public void testFindByStockDateEntityBetween() throws ParseException {
		Date start = this.DATE_FORMAT.parse("2012-02-01");
		Date end = this.DATE_FORMAT.parse("2012-03-30");

		StockEntity stock = this.stockRepository.findByName("PETR4");

		List<StockRecordEntity> stockRecords = this.stockRecordRepository.findByTradeDateBetweenAndStock(start, end, stock);

		this.logger.info(stockRecords);
	}

	@Test
	public void testTradeDate() throws ParseException {
		Date tradeDate = this.DATE_FORMAT.parse("2012-02-01");

		StockEntity stock = this.stockRepository.findByName("PETR4");

		List<StockRecordEntity> stockRecords = this.stockRecordRepository.findByTradeDateGreaterThanAndStockOrderByTradeDateAsc(tradeDate, stock, new PageRequest(0, 14));

		this.logger.info(stockRecords);
	}

	@Test
	public void testFindOne() {
		StockRecordEntity record = this.stockRecordRepository.findOne(640199L);

		this.logger.info(record);
	}

	@Test
	public void testStockDataBetween() throws ParseException {
		StockEntity stock = this.stockRepository.findByName("PETR4");
		Date initialDate = this.DATE_FORMAT.parse("2000-01-01");
		Date finalDate = this.DATE_FORMAT.parse("2008-02-01");

		List<StockDataBean> stockData = this.stockRecordRepository.findStockDataByTradeDateBetween(stock, initialDate, finalDate);

		this.logger.info(stockData);
	}

	@Test
	public void testStockDataBefore() throws ParseException {
		StockEntity stock = this.stockRepository.findByName("PETR4");
		Date date = this.DATE_FORMAT.parse("2000-01-01");
		Pageable pageable = new PageRequest(0, 14);

		try {
			List<StockDataBean> stockData = this.stockRecordRepository.findStockDataByTradeDateBefore(stock, date, pageable);

			this.logger.info(stockData);
		} catch (Exception e) {
			this.logAndFail(e.getMessage(), this.logger);
		}
	}

	@Test
	public void testStockDataAfter() throws ParseException {
		StockEntity stock = this.stockRepository.findByName("PETR4");
		Date date = this.DATE_FORMAT.parse("2000-01-03");
		Pageable pageable = new PageRequest(0, 5);

		try {
			List<StockDataBean> stockData = this.stockRecordRepository.findStockDataByTradeDateAfter(stock, date, pageable);

			this.logger.info(stockData);
		} catch (Exception e) {
			this.logAndFail(e.getMessage(), this.logger);
		}
	}

}
