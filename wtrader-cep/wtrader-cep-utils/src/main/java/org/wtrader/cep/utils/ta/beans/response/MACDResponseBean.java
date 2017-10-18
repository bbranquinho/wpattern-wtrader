package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class MACDResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201309020318L;

	private double[] macd;

	private double[] signal;

	private double[] histogram;

	public MACDResponseBean() {
	}

	public MACDResponseBean(double[] macd, double[] signal, double[] histogram) {
		super();
		this.macd = macd;
		this.signal = signal;
		this.histogram = histogram;
	}

	public double[] getMacd() {
		return this.macd;
	}

	public void setMacd(double[] macd) {
		this.macd = macd;
	}

	public double[] getSignal() {
		return this.signal;
	}

	public void setSignal(double[] signal) {
		this.signal = signal;
	}

	public double[] getHistogram() {
		return this.histogram;
	}

	public void setHistogram(double[] histogram) {
		this.histogram = histogram;
	}

	@Override
	public void normalize() {
		this.macd = RangeNormalization.normalize(this.macd);
		this.signal = RangeNormalization.normalize(this.signal);
		this.histogram = RangeNormalization.normalize(this.histogram);
	}

	@Override
	public void normalize(double low, double high) {
		this.macd = RangeNormalization.normalize(low, high, this.macd);
		this.signal = RangeNormalization.normalize(low, high, this.signal);
		this.histogram = RangeNormalization.normalize(low, high, this.histogram);
	}

}
