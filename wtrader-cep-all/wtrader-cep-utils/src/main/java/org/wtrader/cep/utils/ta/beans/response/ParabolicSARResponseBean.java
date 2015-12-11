package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class ParabolicSARResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160702L;

	private double[] parabolicSAR;

	public ParabolicSARResponseBean() {
	}

	public ParabolicSARResponseBean(double[] parabolicSAR) {
		super();
		this.parabolicSAR = parabolicSAR;
	}

	public double[] getParabolicSAR() {
		return this.parabolicSAR;
	}

	public void setParabolicSAR(double[] parabolicSAR) {
		this.parabolicSAR = parabolicSAR;
	}

	@Override
	public void normalize() {
		this.parabolicSAR = RangeNormalization.normalize(this.parabolicSAR);
	}

	@Override
	public void normalize(double low, double high) {
		this.parabolicSAR = RangeNormalization.normalize(low, high, this.parabolicSAR);
	}

}
