package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.ILinearRegressionSlope;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.LinearRegressionSlopeRequestBean;
import org.wtrader.cep.utils.ta.beans.response.LinearRegressionSlopeResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class LinearRegressionSlope implements ILinearRegressionSlope {

	private final Logger LOGGER = Logger.getLogger(LinearRegressionSlope.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public LinearRegressionSlope() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (ILinearRegressionSlope)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public LinearRegressionSlopeResponseBean calculate(LinearRegressionSlopeRequestBean requestBean) {
		int timePeriod = requestBean.getTimePeriod();
		int numRecords = core.linearRegSlopeLookback(timePeriod);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateLinearRegressionSlope(data, timePeriod, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private LinearRegressionSlopeResponseBean calculateLinearRegressionSlope(double[] data, int timePeriod, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Linear Regression Slope with tim period [%s].",
					timePeriod));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.linearRegSlopeLookback(timePeriod)];

		RetCode code = core.linearRegSlope(0, data.length - 1, data, timePeriod, beginOut, lengthOut, outReal);

		return new LinearRegressionSlopeResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
