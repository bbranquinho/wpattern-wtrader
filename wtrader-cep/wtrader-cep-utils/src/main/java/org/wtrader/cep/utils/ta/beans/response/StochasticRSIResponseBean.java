package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class StochasticRSIResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] stochasticRSIFastK;

	private double[] stochasticRSIFastD;

	public StochasticRSIResponseBean() {
	}

	public StochasticRSIResponseBean(double[] stochasticRSIFastK, double[] stochasticRSIFastD) {
		super();
		this.stochasticRSIFastK = stochasticRSIFastK;
		this.stochasticRSIFastD = stochasticRSIFastD;
	}

	public double[] getStochasticRSIFastK() {
		return this.stochasticRSIFastK;
	}

	public void setStochasticRSIFastK(double[] stochasticRSIFastK) {
		this.stochasticRSIFastK = stochasticRSIFastK;
	}

	public double[] getStochasticRSIFastD() {
		return this.stochasticRSIFastD;
	}

	public void setStochasticRSIFastD(double[] stochasticRSIFastD) {
		this.stochasticRSIFastD = stochasticRSIFastD;
	}

	@Override
	public void normalize() {
		this.stochasticRSIFastK = RangeNormalization.normalize(this.stochasticRSIFastK);
		this.stochasticRSIFastD = RangeNormalization.normalize(this.stochasticRSIFastD);
	}

	@Override
	public void normalize(double low, double high) {
		this.stochasticRSIFastK = RangeNormalization.normalize(low, high, this.stochasticRSIFastK);
		this.stochasticRSIFastD = RangeNormalization.normalize(low, high, this.stochasticRSIFastD);
	}

}
