package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.beans.BaseBean;
import org.wtrader.cep.utils.enums.AbsolutePriceOscillatorMAType;
import org.wtrader.cep.utils.enums.DataType;

public class AbsolutePriceOscillatorRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int slowPeriod;

	private int fastPeriod;

	private AbsolutePriceOscillatorMAType maType;

	private DataType dataType;

	public AbsolutePriceOscillatorRequestBean() {
	}

	public AbsolutePriceOscillatorRequestBean(String stockName, Date startDate, Date endDate, int slowPeriod,
			int fastPeriod, AbsolutePriceOscillatorMAType maType, DataType dataType) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.slowPeriod = slowPeriod;
		this.fastPeriod = fastPeriod;
		this.maType = maType;
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

	public int getSlowPeriod() {
		return this.slowPeriod;
	}

	public void setSlowPeriod(int slowPeriod) {
		this.slowPeriod = slowPeriod;
	}

	public int getFastPeriod() {
		return this.fastPeriod;
	}

	public void setFastPeriod(int fastPeriod) {
		this.fastPeriod = fastPeriod;
	}

	public AbsolutePriceOscillatorMAType getMaType() {
		return this.maType;
	}

	public void setMaType(AbsolutePriceOscillatorMAType maType) {
		this.maType = maType;
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
