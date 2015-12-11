package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.ta.beans.utils.AbstractRequestBean;
import org.wtrader.cep.utils.ta.enums.TACodeEnum;

public class RSIRequestBean extends AbstractRequestBean {

	private static final long serialVersionUID = 201311062118L;

	private int period;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public RSIRequestBean() {
	}

	public RSIRequestBean(int period, String stockName, Date startDate, Date endDate, DataType dataType) {
		super(stockName, startDate, endDate, dataType);
		this.period = period;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getTACode() {
		return TACodeEnum.RSI.code();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// GETTERS/SETTERS METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}
