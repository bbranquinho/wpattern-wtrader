package org.wtrader.test.cep.factory.ta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.utils.enums.StochasticMAType;
import org.wtrader.cep.utils.interfaces.IStochastic;
import org.wtrader.cep.utils.ta.beans.request.StochasticRequestBean;
import org.wtrader.cep.utils.ta.beans.response.StochasticResponseBean;
import org.wtrader.cep.utils.ta.beans.utils.RequestBeanFactory;
import org.wtrader.test.cep.factory.utils.AbstractCepFactoryTest;

public class StochasticTest extends AbstractCepFactoryTest {

	private static final Logger LOGGER = Logger.getLogger(StochasticTest.class);

	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

	@Inject
	private IStochastic stochasticTA;

	@Test
	public void testStochasticTA() throws ParseException {
		String stockName = "PETR4";
		Date startDate = DATE_FORMAT.parse("01/01/2000");
		Date endDate = DATE_FORMAT.parse("04/01/2000");

		int fastKPeriod = 14;
		int fastDPeriod = 14;
		int slowKPeriod = 3;
		StochasticMAType slowKMAType = StochasticMAType.SMA;
		int slowDPeriod = 3;
		StochasticMAType slowDMAType = StochasticMAType.SMA;

		StochasticRequestBean requestBean = RequestBeanFactory.instanceStochasticBean(stockName, startDate, endDate,
				fastKPeriod, fastDPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType);

		StochasticResponseBean stochasticData = this.stochasticTA.calculate(requestBean);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(stochasticData);
		}
	}

}
