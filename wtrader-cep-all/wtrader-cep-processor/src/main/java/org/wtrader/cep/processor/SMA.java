package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.ISMA;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.SMARequestBean;
import org.wtrader.cep.utils.ta.beans.response.SMAResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class SMA implements ISMA {

	private final Logger LOGGER = Logger.getLogger(this.getClass());

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public SMA() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (ISMA)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public SMAResponseBean calculate(SMARequestBean requestBean) {
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(),
				requestBean.getPeriod() -1);

		return this.calculateSMA(data, requestBean.getPeriod());
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private SMAResponseBean calculateSMA(double[] data, int period) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the simple moving average with [%s] periods.", period));
		}

		double out[] = new double[data.length - period + 1];

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();

		RetCode shortSMACode = core.sma(0, data.length - 1, data, period, beginOut, lengthOut, out);

		return new SMAResponseBean((shortSMACode == RetCode.Success) ? out : null);
	}

}
