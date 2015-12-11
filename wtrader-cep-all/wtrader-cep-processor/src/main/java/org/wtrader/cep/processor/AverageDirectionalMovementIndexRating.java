package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IAverageDirectionalMovementIndexRating;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRatingRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageDirectionalMovementIndexRatingResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class AverageDirectionalMovementIndexRating implements IAverageDirectionalMovementIndexRating {

	private final Logger LOGGER = Logger.getLogger(AverageDirectionalMovementIndexRating.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AverageDirectionalMovementIndexRating() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAverageDirectionalMovementIndexRating)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AverageDirectionalMovementIndexRatingResponseBean calculate(AverageDirectionalMovementIndexRatingRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.adxrLookback(period);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAverageDirectionalMovementIndexRating(close, high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public AverageDirectionalMovementIndexRatingResponseBean calculateAverageDirectionalMovementIndexRating(
			double[] close, double[] high, double[] low, int period, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Average Directional Movement Index Rating with period [%s].", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length - core.adxrLookback(period)];

		RetCode code = core.adxr(0, close.length - 1, high, low, close, period, beginOut, lengthOut, outReal);

		return new AverageDirectionalMovementIndexRatingResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
