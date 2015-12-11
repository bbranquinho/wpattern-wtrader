package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class EMAResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201309030304L;

	private double[] ema;

	public EMAResponseBean() {
	}

	public EMAResponseBean(double[] ema) {
		super();
		this.ema = ema;
	}

	public double[] getEma() {
		return this.ema;
	}

	public void setEma(double[] ema) {
		this.ema = ema;
	}

	@Override
	public void normalize() {
		this.ema = RangeNormalization.normalize(this.ema);
	}

	@Override
	public void normalize(double low, double high) {
		this.ema = RangeNormalization.normalize(low, high, this.ema);
	}

}
