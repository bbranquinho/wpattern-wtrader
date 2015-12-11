package org.wtrader.cep.utils.ta.beans.utils;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.beans.BaseBean;

public abstract class AbstractRequestBean extends BaseBean {

	private static final long serialVersionUID = 201310311404L;

	private Date startDate;

	private Date endDate;

	private DataType dataType;

	private String stockName;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public AbstractRequestBean() {
	}

	public AbstractRequestBean(String stockName, Date startDate, Date endDate, DataType dataType) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dataType = dataType;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public abstract int getTACode();

	//////////////////////////////////////////////////////////////////////////////////////
	// GETTERS/SETTERS METHODS
	//////////////////////////////////////////////////////////////////////////////////////

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

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

}
