package org.wtrader.test.cep.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.utils.data.enums.SortEnum;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class TestDataStoreByStockAndDate extends AbstractCepDatabaseTest {

	private Logger logger = Logger.getLogger(this.getClass());

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void testBetweenAndAbove_Ascending() throws ParseException {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndAboveByDataType("PETR4", DataType.CLOSING_NEGOTIATION_PRICE, this.simpleDateFormat.parse("01/01/2012"),
				this.simpleDateFormat.parse("29/01/2012"), 5, SortEnum.ASCENDING);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(data);
		}
	}

	@Test
	public void testBetweenAndAbove_Descending() throws ParseException {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndAboveByDataType("PETR4", DataType.CLOSING_NEGOTIATION_PRICE, this.simpleDateFormat.parse("01/01/2012"),
				this.simpleDateFormat.parse("29/01/2012"), 5, SortEnum.DESCENDING);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(data);
		}
	}

	@Test
	public void testBetweenAndBelow_Ascending() throws ParseException {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType("PETR4", DataType.CLOSING_NEGOTIATION_PRICE, this.simpleDateFormat.parse("01/01/2012"),
				this.simpleDateFormat.parse("29/01/2012"), 5, SortEnum.ASCENDING);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(data);
		}
	}

	@Test
	public void testBetweenAndBelow_Descending() throws ParseException {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType("PETR4", DataType.CLOSING_NEGOTIATION_PRICE, this.simpleDateFormat.parse("01/01/2012"),
				this.simpleDateFormat.parse("29/01/2012"), 5, SortEnum.DESCENDING);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(data);
		}
	}

}
