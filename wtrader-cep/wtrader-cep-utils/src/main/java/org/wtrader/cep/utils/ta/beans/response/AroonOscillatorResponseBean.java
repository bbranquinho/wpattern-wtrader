package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AroonOscillatorResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160543L;

	private double[] aroonOscillator;

	public AroonOscillatorResponseBean() {
	}

	public AroonOscillatorResponseBean(double[] aroonOscillator) {
		super();
		this.aroonOscillator = aroonOscillator;
	}

	public double[] getAroonOscillator() {
		return this.aroonOscillator;
	}

	public void setAroonOscillator(double[] aroonOscillator) {
		this.aroonOscillator = aroonOscillator;
	}

	@Override
	public void normalize() {
		this.aroonOscillator = RangeNormalization.normalize(this.aroonOscillator);
	}

	@Override
	public void normalize(double low, double high) {
		this.aroonOscillator = RangeNormalization.normalize(low, high, this.aroonOscillator);
	}

}
