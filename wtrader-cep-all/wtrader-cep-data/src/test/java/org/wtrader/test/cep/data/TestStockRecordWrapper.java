package org.wtrader.test.cep.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.utils.data.beans.StockDataBean;
import org.wtrader.cep.utils.data.interfaces.IStockRecordData;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class TestStockRecordWrapper extends AbstractCepDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TestStockRecordWrapper.class);

	private static final String PATTERN = "yyyy-MM-dd";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);

	@Inject
	private IStockRecordData stockRecordData;

	@Test
	public void testFindStockDataBetweenAndBeforeTradeDate() {
		String stock = "PETR4";
		int numDataBefore = 10, numDataAfter = 5;
		Date initialDate = null, finalDate = null;

		try {
			initialDate = DATE_FORMAT.parse("2000-01-01");
			finalDate = DATE_FORMAT.parse("2000-02-01");
		} catch (ParseException e) {
			this.logAndFail(e.getMessage(), LOGGER);
		}

		List<StockDataBean> stockData = this.stockRecordData.findStockDataBetweenAndBeforeTradeDate(
				stock, numDataBefore, numDataAfter, initialDate, finalDate);

		LOGGER.info(stockData);
	}

}
