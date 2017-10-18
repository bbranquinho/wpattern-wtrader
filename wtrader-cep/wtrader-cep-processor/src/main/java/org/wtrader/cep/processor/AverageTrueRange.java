package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IAverageTrueRange;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.AverageTrueRangeRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageTrueRangeResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class AverageTrueRange implements IAverageTrueRange {

	private final Logger LOGGER = Logger.getLogger(AverageTrueRange.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AverageTrueRange() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IAverageTrueRange)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public AverageTrueRangeResponseBean calculate(AverageTrueRangeRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.atrLookback(period);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateAverageTrueRange(high, low, close, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private AverageTrueRangeResponseBean calculateAverageTrueRange(double[] high, double[] low, double[] close, int period, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Average True Range with period [%s].", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[high.length - core.atrLookback(period)];

		RetCode code = core.atr(0, close.length - 1, high, low, close, period, beginOut, lengthOut, outReal);

		return new AverageTrueRangeResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
