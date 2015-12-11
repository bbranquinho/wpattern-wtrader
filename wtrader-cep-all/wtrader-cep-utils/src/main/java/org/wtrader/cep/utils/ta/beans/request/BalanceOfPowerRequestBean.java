package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class BalanceOfPowerRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160643L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	public BalanceOfPowerRequestBean() {
	}

	public BalanceOfPowerRequestBean(String stockName, Date startDate,
			Date endDate) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
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

}
