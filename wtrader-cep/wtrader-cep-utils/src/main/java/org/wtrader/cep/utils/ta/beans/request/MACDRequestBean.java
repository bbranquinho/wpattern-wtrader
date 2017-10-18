package org.wtrader.cep.utils.ta.beans.request;

import java.util.Date;

import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.enums.MACDType;
import org.wtrader.cep.utils.ta.beans.utils.AbstractRequestBean;
import org.wtrader.cep.utils.ta.enums.TACodeEnum;

public class MACDRequestBean extends AbstractRequestBean {

	private static final long serialVersionUID = 201310311416L;

	private int fastPeriod;

	private int slowPeriod;

	private int signalPeriod;

	private MACDType macdType;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public MACDRequestBean() {
	}

	public MACDRequestBean(int fastPeriod, int slowPeriod, int signalPeriod, MACDType macdType, String stockName,
			Date startDate, Date endDate, DataType dataType) {
		super(stockName, startDate, endDate, dataType);
		this.fastPeriod = fastPeriod;
		this.slowPeriod = slowPeriod;
		this.signalPeriod = signalPeriod;
		this.macdType = macdType;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getTACode() {
		return TACodeEnum.MACD.code();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// GETTERS/SETTERS METHODS
	//////////////////////////////////////////////////////////////////////////////////////

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

	public int getSignalPeriod() {
		return this.signalPeriod;
	}

	public void setSignalPeriod(int signalPeriod) {
		this.signalPeriod = signalPeriod;
	}

	public MACDType getMacdType() {
		return this.macdType;
	}

	public void setMacdType(MACDType macdType) {
		this.macdType = macdType;
	}

}
