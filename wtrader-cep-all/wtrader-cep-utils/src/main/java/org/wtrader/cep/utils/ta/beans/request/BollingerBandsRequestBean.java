package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.BollingerBandsMAType;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.beans.BaseBean;

public class BollingerBandsRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int period;

	private DataType dataType;

	private BollingerBandsMAType bollingerBandsMAType;

	private double devUp;

	private double devDn;

	public BollingerBandsRequestBean() {
	}

	public BollingerBandsRequestBean(String stockName, Date startDate, Date endDate, int period, DataType dataType,
			BollingerBandsMAType bollingerBandsMAType, double devUp, double devDn) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.period = period;
		this.dataType = dataType;
		this.bollingerBandsMAType = bollingerBandsMAType;
		this.devUp = devUp;
		this.devDn = devDn;
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

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public BollingerBandsMAType getBollingerBandsMAType() {
		return this.bollingerBandsMAType;
	}

	public void setBollingerBandsMAType(BollingerBandsMAType bollingerBandsMAType) {
		this.bollingerBandsMAType = bollingerBandsMAType;
	}

	public double getDevUp() {
		return this.devUp;
	}

	public void setDevUp(double devUp) {
		this.devUp = devUp;
	}

	public double getDevDn() {
		return this.devDn;
	}

	public void setDevDn(double devDn) {
		this.devDn = devDn;
	}

}
