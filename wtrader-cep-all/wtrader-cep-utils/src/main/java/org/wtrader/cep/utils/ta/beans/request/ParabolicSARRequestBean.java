package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class ParabolicSARRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private double acceleration;

	private double maximum;

	public ParabolicSARRequestBean() {
	}

	public ParabolicSARRequestBean(String stockName, Date startDate, Date endDate, double acceleration, double maximum) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.acceleration = acceleration;
		this.maximum = maximum;
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

	public double getAcceleration() {
		return this.acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getMaximum() {
		return this.maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

}
