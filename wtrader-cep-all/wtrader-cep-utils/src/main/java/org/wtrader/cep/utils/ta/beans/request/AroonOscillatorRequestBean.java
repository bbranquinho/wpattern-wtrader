package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class AroonOscillatorRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int period;

	public AroonOscillatorRequestBean() {
	}

	public AroonOscillatorRequestBean(String stockName, Date startDate, Date endDate, int period) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.period = period;
	}

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}
