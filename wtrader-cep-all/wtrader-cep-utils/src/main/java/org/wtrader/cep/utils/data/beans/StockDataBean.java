package org.wtrader.cep.utils.data.beans;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;

public class StockDataBean extends BaseBean {

	private static final long serialVersionUID = 201505060201L;

	private final Date date;

	private final double data;

	public StockDataBean() {
		this.data = 0;
		this.date = null;
	}

	public StockDataBean(Date date, double data) {
		super();
		this.date = date;
		this.data = data;
	}

	public Date getDate() {
		return this.date;
	}

	public double getData() {
		return this.data;
	}

}
