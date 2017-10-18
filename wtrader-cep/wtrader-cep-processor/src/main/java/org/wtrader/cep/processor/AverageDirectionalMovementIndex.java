package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IAverageDirectionalMovementIndex;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageDirectionalMovementIndexResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class AverageDirectionalMovementIndex implements IAverageDirectionalMovementIndex {

	private final Logger LOGGER = Logger.getLogger(AverageDirectionalMovementIndex.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AverageDirectionalMovementIndex() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAverageDirectionalMovementIndex)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AverageDirectionalMovementIndexResponseBean calculate(AverageDirectionalMovementIndexRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.adxLookback(period);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAverageDirectionalMovementIndex(close, high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public AverageDirectionalMovementIndexResponseBean calculateAverageDirectionalMovementIndex(
			double[] close, double[] high, double[] low, int period, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Average Directional Movement Index with period [%s].", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length - core.adxLookback(period)];

		RetCode code = core.adx(0, close.length - 1, high, low, close, period, beginOut, lengthOut, outReal);

		return new AverageDirectionalMovementIndexResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
