package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.ICommodityChannelIndex;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.CommodityChannelIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.CommodityChannelIndexResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class CommodityChannelIndex implements ICommodityChannelIndex {

	private static final Logger LOGGER = Logger.getLogger(CommodityChannelIndex.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public CommodityChannelIndex() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (ICommodityChannelIndex)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public CommodityChannelIndexResponseBean calculate(CommodityChannelIndexRequestBean requestBean) {
		int period = requestBean.getPeriod();
		int numRecords = core.cciLookback(period);
		double[] close = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateCommodityChannelIndex(close, high, low, period, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public CommodityChannelIndexResponseBean calculateCommodityChannelIndex(double[] close, double[] high, double[] low, int period, ITACore core) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Processing the Commodity Channel Index with [%s] periods.", period));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length - core.cciLookback(period)];

		RetCode code = core.cci(0, close.length - 1, high, low, close, period, beginOut, lengthOut, outReal);

		return new CommodityChannelIndexResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
