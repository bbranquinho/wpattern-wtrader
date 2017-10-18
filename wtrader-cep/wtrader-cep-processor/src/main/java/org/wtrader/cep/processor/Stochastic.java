package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.enums.StochasticMAType;
import org.wtrader.cep.utils.interfaces.IStochastic;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.StochasticRequestBean;
import org.wtrader.cep.utils.ta.beans.response.StochasticResponseBean;
import org.wtrader.cep.utils.ta.enums.MAType;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class Stochastic implements IStochastic {

	private final Logger LOGGER = Logger.getLogger(Stochastic.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public Stochastic() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IStochastic)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public StochasticResponseBean calculate(StochasticRequestBean requestBean) {
		int fastKPeriod = requestBean.getFastKPeriod();
		int fastDPeriod = requestBean.getFastDPeriod();
		int slowKPeriod = requestBean.getSlowKPeriod();
		MAType slowKMAType = this.convertMAType(requestBean.getSlowKMAType());
		int slowDPeriod = requestBean.getSlowDPeriod();
		MAType slowDMAType = this.convertMAType(requestBean.getSlowDMAType());

		int numRecords = core.stochLookback(fastKPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateStochastic(high, low, close, fastKPeriod, fastDPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private StochasticResponseBean calculateStochastic(double[] high, double[] low, double[] close, int fastKPeriod, int fastDPeriod,
			int slowKPeriod, MAType slowKMAType, int slowDPeriod, MAType slowDMAType, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Stochastic with fast K period [%s], fast D period [%s], slow K period [%s], "
					+ "slow K MA type [%s], slow D period [%s] and slow D MA type [%s].",
					fastKPeriod, fastDPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();

		double[] outSlowK = new double[close.length - core.stochLookback(fastKPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType)];
		double[] outSlowD = new double[close.length - core.stochLookback(fastKPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType)];

		RetCode code = core.stoch(0, close.length - 1, high, low, close, fastKPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType,
				beginOut, lengthOut, outSlowK, outSlowD);

		return new StochasticResponseBean((code == RetCode.Success) ? outSlowK : null, (code == RetCode.Success) ? outSlowD : null);
	}

	private MAType convertMAType(StochasticMAType maType) {
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
