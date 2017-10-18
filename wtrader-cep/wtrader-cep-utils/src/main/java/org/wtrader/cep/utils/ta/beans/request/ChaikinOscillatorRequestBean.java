package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class ChaikinOscillatorRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401151848L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int fastPeriod;

	private int slowPeriod;

	public ChaikinOscillatorRequestBean() {
	}

	public ChaikinOscillatorRequestBean(String stockName, Date startDate, Date endDate, int fastPeriod, int slowPeriod) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fastPeriod = fastPeriod;
		this.slowPeriod = slowPeriod;
	}

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public int getFastPeriod() {
		return this.fastPeriod;
	}

	public void setFastPeriod(int fastPeriod) {
		this.fastPeriod = fastPeriod;
	}

	public int getSlowPeriod() {
		return this.slowPeriod;
	}

	public void setSlowPeriod(int slowPeriod) {
		this.slowPeriod = slowPeriod;
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

}
