package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.IKaufmanAdaptiveMovingAverage;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.KaufmanAdaptiveMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.response.KaufmanAdaptiveMovingAverageResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class KaufmanAdaptiveMovingAverage implements IKaufmanAdaptiveMovingAverage {

	private final Logger LOGGER = Logger.getLogger(KaufmanAdaptiveMovingAverage.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public KaufmanAdaptiveMovingAverage() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IKaufmanAdaptiveMovingAverage)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public KaufmanAdaptiveMovingAverageResponseBean calculate(KaufmanAdaptiveMovingAverageRequestBean requestBean) {
		int timePeriod = requestBean.getTimePeriod();
		int numRecords = core.kamaLookback(timePeriod);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateKaufmanAdaptiveMovingAverage(data, timePeriod, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private KaufmanAdaptiveMovingAverageResponseBean calculateKaufmanAdaptiveMovingAverage(double[] data, int timePeriod, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Kaufman Adaptive Moving Average with period [%s].",
					timePeriod));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.kamaLookback(timePeriod)];

		RetCode code = core.kama(0, data.length - 1, data, timePeriod, beginOut, lengthOut, outReal);

		return new KaufmanAdaptiveMovingAverageResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
