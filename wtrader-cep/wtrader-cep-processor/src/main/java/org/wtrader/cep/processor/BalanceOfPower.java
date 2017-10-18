package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IBalanceOfPower;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.BalanceOfPowerRequestBean;
import org.wtrader.cep.utils.ta.beans.response.BalanceOfPowerResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class BalanceOfPower implements IBalanceOfPower {

	private final Logger LOGGER = Logger.getLogger(BalanceOfPower.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public BalanceOfPower() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IBalanceOfPower)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public BalanceOfPowerResponseBean calculate(BalanceOfPowerRequestBean requestBean) {
		double[] close = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.CLOSING_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] high = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] low = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate());
		double[] open = this.dataStoreByStockAndDate.findBetweenByDataType(requestBean.getStockName(),
				DataType.START_PRICE, requestBean.getStartDate(), requestBean.getEndDate());

		return this.calculateBalanceOfPower(close, open, high, low, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private BalanceOfPowerResponseBean calculateBalanceOfPower(double[] close, double[] open, double[] high, double[] low, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Balance of Power."));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[close.length];

		RetCode code = core.bop(0, close.length - 1, open, high, low, close, beginOut, lengthOut, outReal);

		return new BalanceOfPowerResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
