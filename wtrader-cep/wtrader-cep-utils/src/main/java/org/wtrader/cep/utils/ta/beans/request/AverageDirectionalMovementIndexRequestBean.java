package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class AverageDirectionalMovementIndexRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160442L;

	private String stockName;

	private int period;

	private Date startDate;

	private Date endDate;

	public AverageDirectionalMovementIndexRequestBean() {
	}

	public AverageDirectionalMovementIndexRequestBean(String stockName, int period, Date startDate, Date endDate) {
		super();
		this.stockName = stockName;
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
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

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

}
