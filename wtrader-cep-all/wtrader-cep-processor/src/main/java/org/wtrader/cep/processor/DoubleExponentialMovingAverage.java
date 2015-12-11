package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.IDoubleExponentialMovingAverage;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.DoubleExponentialMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.response.DoubleExponentialMovingAverageResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class DoubleExponentialMovingAverage implements IDoubleExponentialMovingAverage {

	private final Logger LOGGER = Logger.getLogger(DoubleExponentialMovingAverage.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public DoubleExponentialMovingAverage() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IDoubleExponentialMovingAverage)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public DoubleExponentialMovingAverageResponseBean calculate(DoubleExponentialMovingAverageRequestBean requestBean) {
		int timePeriod = requestBean.getTimePeriod();
		int numRecords = core.demaLookback(timePeriod);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateDoubleExponentialMovingAverage(data, timePeriod, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private DoubleExponentialMovingAverageResponseBean calculateDoubleExponentialMovingAverage(double[] data, int timePeriod, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Double Exponential Moving Average with period [%s].", timePeriod));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.demaLookback(timePeriod)];

		RetCode code = core.dema(0, data.length - 1, data, timePeriod, beginOut, lengthOut, outReal);

		return new DoubleExponentialMovingAverageResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
