package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.StochasticMAType;
import org.wtrader.cep.utils.beans.BaseBean;

public class StochasticRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int fastKPeriod;

	private int fastDPeriod;

	private int slowKPeriod;

	private StochasticMAType slowKMAType;

	private int slowDPeriod;

	private StochasticMAType slowDMAType;

	public StochasticRequestBean() {
	}

	public StochasticRequestBean(String stockName, Date startDate,
			Date endDate, int fastKPeriod, int fastDPeriod, int slowKPeriod,
			StochasticMAType slowKMAType, int slowDPeriod,
			StochasticMAType slowDMAType) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fastKPeriod = fastKPeriod;
		this.fastDPeriod = fastDPeriod;
		this.slowKPeriod = slowKPeriod;
		this.slowKMAType = slowKMAType;
		this.slowDPeriod = slowDPeriod;
		this.slowDMAType = slowDMAType;
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

	public int getFastKPeriod() {
		return this.fastKPeriod;
	}

	public void setFastKPeriod(int fastKPeriod) {
		this.fastKPeriod = fastKPeriod;
	}

	public int getFastDPeriod() {
		return this.fastDPeriod;
	}

	public void setFastDPeriod(int fastDPeriod) {
		this.fastDPeriod = fastDPeriod;
	}

	public int getSlowKPeriod() {
		return this.slowKPeriod;
	}

	public void setSlowKPeriod(int slowKPeriod) {
		this.slowKPeriod = slowKPeriod;
	}

	public StochasticMAType getSlowKMAType() {
		return this.slowKMAType;
	}

	public void setSlowKMAType(StochasticMAType slowKMAType) {
		this.slowKMAType = slowKMAType;
	}

	public int getSlowDPeriod() {
		return this.slowDPeriod;
	}

	public void setSlowDPeriod(int slowDPeriod) {
		this.slowDPeriod = slowDPeriod;
	}

	public StochasticMAType getSlowDMAType() {
		return this.slowDMAType;
	}

	public void setSlowDMAType(StochasticMAType slowDMAType) {
		this.slowDMAType = slowDMAType;
	}

}
