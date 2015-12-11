package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IChaikinLine;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.ChaikinLineRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ChaikinLineResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class ChaikinLine implements IChaikinLine {

	private final Logger LOGGER = Logger.getLogger(ChaikinLine.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public ChaikinLine() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IChaikinLine)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ChaikinLineResponseBean calculate(ChaikinLineRequestBean requestBean) {
		double[] close = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] high = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] low = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] volume = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.VOLUME, requestBean.getStartDate(), requestBean.getEndDate());

		return this.calculateChaikinLine(close, high, low, volume);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private ChaikinLineResponseBean calculateChaikinLine(double[] close, double[] high, double[] low, double[] volume) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Chaikin Line."));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length];

		RetCode chaikinLineCode = core.ad(0, close.length - 1, high, low, close, volume, beginOut, lengthOut, outReal);

		return new ChaikinLineResponseBean((chaikinLineCode == RetCode.Success) ? outReal : null);
	}

}
