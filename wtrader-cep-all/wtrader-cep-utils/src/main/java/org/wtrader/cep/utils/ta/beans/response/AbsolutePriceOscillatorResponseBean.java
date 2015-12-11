package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AbsolutePriceOscillatorResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] absolutePriceOscillator;

	public AbsolutePriceOscillatorResponseBean() {
	}

	public AbsolutePriceOscillatorResponseBean(double[] absolutePriceOscillator) {
		super();
		this.absolutePriceOscillator = absolutePriceOscillator;
	}

	public double[] getAbsolutePriceOscillator() {
		return this.absolutePriceOscillator;
	}

	public void setAbsolutePriceOscillator(double[] absolutePriceOscillator) {
		this.absolutePriceOscillator = absolutePriceOscillator;
	}

	@Override
	public void normalize() {
		this.absolutePriceOscillator = RangeNormalization.normalize(this.absolutePriceOscillator);
	}

	@Override
	public void normalize(double low, double high) {
		this.absolutePriceOscillator = RangeNormalization.normalize(low, high, this.absolutePriceOscillator);
	}

}
