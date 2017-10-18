package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.ta.beans.utils.AbstractRequestBean;
import org.wtrader.cep.utils.ta.enums.TACodeEnum;


public class EMARequestBean extends AbstractRequestBean {

	private static final long serialVersionUID = 201310311413L;

	private int period;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public EMARequestBean() {
	}

	public EMARequestBean(int period, String stockName, Date startDate, Date endDate, DataType dataType) {
		super(stockName, startDate, endDate, dataType);
		this.period = period;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getTACode() {
		return TACodeEnum.EMA.code();
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
