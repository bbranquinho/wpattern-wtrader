package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.enums.StochasticRSIMAType;
import org.wtrader.cep.utils.beans.BaseBean;

public class StochasticRSIRequestBean extends BaseBean {

	private static final long serialVersionUID = 201401160544L;

	private String stockName;

	private Date startDate;

	private Date endDate;

	private int timePeriod;

	private int fastKPeriod;

	private int fastDPeriod;

	private StochasticRSIMAType maType;

	private DataType dataType;

	public StochasticRSIRequestBean() {
	}

	public StochasticRSIRequestBean(String stockName, Date startDate,
			Date endDate, int timePeriod, int fastKPeriod, int fastDPeriod,
			StochasticRSIMAType maType, DataType dataType) {
		super();
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.timePeriod = timePeriod;
		this.fastKPeriod = fastKPeriod;
		this.fastDPeriod = fastDPeriod;
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

	public int getTimePeriod() {
		return this.timePeriod;
	}

	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
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

	public StochasticRSIMAType getMaType() {
		return this.maType;
	}

	public void setMaType(StochasticRSIMAType maType) {
		this.maType = maType;
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
