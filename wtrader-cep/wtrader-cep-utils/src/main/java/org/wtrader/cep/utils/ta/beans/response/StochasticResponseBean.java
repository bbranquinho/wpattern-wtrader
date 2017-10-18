package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class StochasticResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] stochasticSlowK;

	private double[] stochasticSlowD;

	public StochasticResponseBean() {
	}

	public StochasticResponseBean(double[] stochasticSlowK, double[] stochasticSlowD) {
		super();
		this.stochasticSlowK = stochasticSlowK;
		this.stochasticSlowD = stochasticSlowD;
	}

	public double[] getStochasticSlowK() {
		return this.stochasticSlowK;
	}

	public void setStochasticSlowK(double[] stochasticSlowK) {
		this.stochasticSlowK = stochasticSlowK;
	}

	public double[] getStochasticSlowD() {
		return this.stochasticSlowD;
	}

	public void setStochasticSlowD(double[] stochasticSlowD) {
		this.stochasticSlowD = stochasticSlowD;
	}

	@Override
	public void normalize() {
		this.stochasticSlowK = RangeNormalization.normalize(this.stochasticSlowK);
		this.stochasticSlowD = RangeNormalization.normalize(this.stochasticSlowD);
	}

	@Override
	public void normalize(double low, double high) {
		this.stochasticSlowK = RangeNormalization.normalize(low, high, this.stochasticSlowK);
		this.stochasticSlowD = RangeNormalization.normalize(low, high, this.stochasticSlowD);
	}

}
