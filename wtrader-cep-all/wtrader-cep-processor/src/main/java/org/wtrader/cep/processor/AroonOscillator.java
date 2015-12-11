package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IAroonOscillator;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AroonOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AroonOscillatorResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class AroonOscillator implements IAroonOscillator {

	private final Logger LOGGER = Logger.getLogger(AroonOscillator.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AroonOscillator() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAroonOscillator)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AroonOscillatorResponseBean calculate(AroonOscillatorRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.aroonLookback(period);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAroonOscillator(high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private AroonOscillatorResponseBean calculateAroonOscillator(double[] high, double[] low, int period, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the AroonOscillator Index with period [%s].", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[high.length - core.aroonLookback(period)];

		RetCode code = core.aroonOsc(0, high.length - 1, high, low, period, beginOut, lengthOut, outReal);

		return new AroonOscillatorResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
