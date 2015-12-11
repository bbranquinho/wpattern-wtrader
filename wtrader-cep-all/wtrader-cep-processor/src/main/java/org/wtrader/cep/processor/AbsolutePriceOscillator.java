package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.AbsolutePriceOscillatorMAType;
import org.wtrader.cep.utils.interfaces.IAbsolutePriceOscillator;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AbsolutePriceOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AbsolutePriceOscillatorResponseBean;
import org.wtrader.cep.utils.ta.enums.MAType;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class AbsolutePriceOscillator implements IAbsolutePriceOscillator {

	private final Logger LOGGER = Logger.getLogger(AbsolutePriceOscillator.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AbsolutePriceOscillator() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAbsolutePriceOscillator)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AbsolutePriceOscillatorResponseBean calculate(AbsolutePriceOscillatorRequestBean requestBean) {
		int slowPeriod = requestBean.getSlowPeriod();
		int fastPeriod = requestBean.getFastPeriod();
		MAType maType = this.convertMAType(requestBean.getMaType());
		int numRecords = this.core.apoLookback(fastPeriod, slowPeriod, maType);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAbsolutePriceOscillator(data, slowPeriod, fastPeriod, maType, this.core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private AbsolutePriceOscillatorResponseBean calculateAbsolutePriceOscillator(double[] data, int slowPeriod, int fastPeriod,
			MAType maType, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Absolute Price Oscillator with slow period [%s], fast period [%s] and ma type [%s].",
					slowPeriod, fastPeriod, maType));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.apoLookback(fastPeriod, slowPeriod, maType)];

		RetCode code = core.apo(0, data.length - 1, data, fastPeriod, slowPeriod, maType, beginOut, lengthOut, outReal);

		return new AbsolutePriceOscillatorResponseBean((code == RetCode.Success) ? outReal : null);
	}

	private MAType convertMAType(AbsolutePriceOscillatorMAType maType) {
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
