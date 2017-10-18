package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IDirectionalMovementIndex;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.DirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.DirectionalMovementIndexResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class DirectionalMovementIndex implements IDirectionalMovementIndex {

	private static final Logger LOGGER = Logger.getLogger(DirectionalMovementIndex.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public DirectionalMovementIndex() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IDirectionalMovementIndex)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public DirectionalMovementIndexResponseBean calculate(DirectionalMovementIndexRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.dxLookback(period);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateDirectionalMovementIndex(close, high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public DirectionalMovementIndexResponseBean calculateDirectionalMovementIndex(double[] close, double[] high, double[] low, int period, ITACore core) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Processing the Directional Movement Index with [%s] periods.", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length - core.dxLookback(period)];

		RetCode code = core.dx(0, close.length - 1, high, low, close, period, beginOut, lengthOut, outReal);

		return new DirectionalMovementIndexResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
