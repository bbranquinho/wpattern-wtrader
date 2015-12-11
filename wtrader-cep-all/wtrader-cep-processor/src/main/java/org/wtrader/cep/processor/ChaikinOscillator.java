package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IChaikinOscillator;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.ChaikinOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ChaikinOscillatorResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class ChaikinOscillator implements IChaikinOscillator {

	private final Logger LOGGER = Logger.getLogger(ChaikinOscillator.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public ChaikinOscillator() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IChaikinOscillator)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ChaikinOscillatorResponseBean calculate(ChaikinOscillatorRequestBean requestBean) {
		int fastPeriod = requestBean.getFastPeriod();
		int slowPeriod = requestBean.getSlowPeriod();

		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), slowPeriod - 1);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), slowPeriod - 1);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), slowPeriod - 1);
		double[] volume = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.VOLUME, requestBean.getStartDate(), requestBean.getEndDate(), slowPeriod - 1);

		return this.calculateChaikinOscillator(close, high, low, volume, fastPeriod, slowPeriod);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private ChaikinOscillatorResponseBean calculateChaikinOscillator(double[] close, double[] high, double[] low, double[] volume,
			int fastPeriod, int slowPeriod) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Chaikin Oscillator with fast period [%s] and slow period [%s].",
					fastPeriod, slowPeriod));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length - slowPeriod + 1];

		RetCode chaikinOscillatorCode = core.adOsc(0, close.length - 1, high, low, close, volume, fastPeriod, slowPeriod, beginOut, lengthOut, outReal);

		return new ChaikinOscillatorResponseBean((chaikinOscillatorCode == RetCode.Success) ? outReal : null);
	}

}
