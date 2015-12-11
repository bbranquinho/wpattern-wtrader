package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.StochasticRSIMAType;
import org.wtrader.cep.utils.interfaces.IStochasticRSI;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.StochasticRSIRequestBean;
import org.wtrader.cep.utils.ta.beans.response.StochasticRSIResponseBean;
import org.wtrader.cep.utils.ta.enums.MAType;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class StochasticRSI implements IStochasticRSI {

	private final Logger LOGGER = Logger.getLogger(StochasticRSI.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public StochasticRSI() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IStochasticRSI)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public StochasticRSIResponseBean calculate(StochasticRSIRequestBean requestBean) {
		int timePeriod = requestBean.getTimePeriod();
		int fastKPeriod = requestBean.getFastKPeriod();
		int fastDPeriod = requestBean.getFastDPeriod();
		MAType maType = this.convertMAType(requestBean.getMaType());
		int numRecords = core.stochRsiLookback(timePeriod, fastKPeriod, fastDPeriod, maType);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateStochasticRSI(data, timePeriod, fastKPeriod, fastDPeriod, maType, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private StochasticRSIResponseBean calculateStochasticRSI(double[] data, int timePeriod, int fastKPeriod, int fastDPeriod,
			MAType maType, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Stochastic RSI with period [%s], fast K period [%s], fast D period [%s] and ma type [%s].",
					timePeriod, fastKPeriod, fastDPeriod, maType));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outFastK = new double[data.length - core.stochRsiLookback(timePeriod, fastKPeriod, fastDPeriod, maType)];
		double[] outFastD = new double[data.length - core.stochRsiLookback(timePeriod, fastKPeriod, fastDPeriod, maType)];

		RetCode code = core.stochRsi(0, data.length - 1, data, timePeriod, fastKPeriod, fastDPeriod, maType, beginOut, lengthOut,
				outFastK, outFastD);

		return new StochasticRSIResponseBean((code == RetCode.Success) ? outFastK : null, (code == RetCode.Success) ? outFastD : null);
	}

	private MAType convertMAType(StochasticRSIMAType maType) {
		switch (maType) {
		case DEMA: return MAType.Dema;
		case EMA: return MAType.Ema;
		case KAMA: return MAType.Kama;
		case MAMA: return MAType.Mama;
		case SMA: return MAType.Sma;
		case T3: return MAType.T3;
		case TEMA: return MAType.Tema;
		case TRIMA: return MAType.Trima;
		case WMA: return MAType.Wma;
		}

		this.LOGGER.error(String.format("Type not founded to MAType [%s].", maType));

		return null;
	}

}
