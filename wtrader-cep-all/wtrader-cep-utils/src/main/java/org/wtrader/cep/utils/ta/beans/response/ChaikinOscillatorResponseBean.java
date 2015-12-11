package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class ChaikinOscillatorResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401151858L;

	private double[] chaikinOscillator;

	public ChaikinOscillatorResponseBean() {
	}

	public ChaikinOscillatorResponseBean(double[] chaikinOscillator) {
		super();
		this.chaikinOscillator = chaikinOscillator;
	}

	public double[] getChaikinOscillator() {
		return this.chaikinOscillator;
	}

	public void setChaikinOscillator(double[] chaikinOscillator) {
		this.chaikinOscillator = chaikinOscillator;
	}

	@Override
	public void normalize() {
		this.chaikinOscillator = RangeNormalization.normalize(this.chaikinOscillator);
	}

	@Override
	public void normalize(double low, double high) {
		this.chaikinOscillator = RangeNormalization.normalize(low, high, this.chaikinOscillator);
	}

}
