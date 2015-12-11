package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.beans.BaseBean;

public class HilbertTransformDominantCyclePhaseRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private DataType dataType;

	public HilbertTransformDominantCyclePhaseRequestBean() {
	}

	public HilbertTransformDominantCyclePhaseRequestBean(String stockName, Date startDate, Date endDate, DataType dataType) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dataType = dataType;
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

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
