package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IAroon;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AroonRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AroonResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class Aroon implements IAroon {

	private final Logger LOGGER = Logger.getLogger(Aroon.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public Aroon() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAroon)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AroonResponseBean calculate(AroonRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.aroonLookback(period);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAroon(high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private AroonResponseBean calculateAroon(double[] high, double[] low, int period, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Aroon Index with period [%s].", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outDown = new double[high.length - core.aroonLookback(period)];
		double[] outUp = new double[high.length - core.aroonLookback(period)];

		RetCode code = core.aroon(0, high.length - 1, high, low, period, beginOut, lengthOut, outDown, outUp);

		return new AroonResponseBean((code == RetCode.Success) ? outDown : null, (code == RetCode.Success) ? outUp : null);
	}

}
