package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class LinearRegressionSlopeResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] linearRegressionSlope;

	public LinearRegressionSlopeResponseBean() {
	}

	public LinearRegressionSlopeResponseBean(double[] linearRegressionSlope) {
		super();
		this.linearRegressionSlope = linearRegressionSlope;
	}

	public double[] getLinearRegressionSlope() {
		return this.linearRegressionSlope;
	}

	public void setLinearRegressionSlope(double[] linearRegressionSlope) {
		this.linearRegressionSlope = linearRegressionSlope;
	}

	@Override
	public void normalize() {
		this.linearRegressionSlope = RangeNormalization.normalize(this.linearRegressionSlope);
	}

	@Override
	public void normalize(double low, double high) {
		this.linearRegressionSlope = RangeNormalization.normalize(low, high, this.linearRegressionSlope);
	}

}
