package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.IRSI;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.RSIRequestBean;
import org.wtrader.cep.utils.ta.beans.response.RSIResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class RSI implements IRSI {

	private final Logger LOGGER = Logger.getLogger(this.getClass());

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public RSI() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IRSI)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public RSIResponseBean calculate(RSIRequestBean requestBean) {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(),
				requestBean.getPeriod());

		return this.calculateRSI(data, requestBean.getPeriod());
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private RSIResponseBean calculateRSI(double[] data, int period) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the relative strength index with [%s] periods.", period));
		}

		double out[] = new double[data.length - period];

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();

		RetCode shortSMACode = core.rsi(0, data.length - 1, data, period, beginOut, lengthOut, out);

		return new RSIResponseBean((shortSMACode == RetCode.Success) ? out : null);
	}

}
