package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class DoubleExponentialMovingAverageResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] doubleExponentialMovingAverage;

	public DoubleExponentialMovingAverageResponseBean() {
	}

	public DoubleExponentialMovingAverageResponseBean(double[] doubleExponentialMovingAverage) {
		super();
		this.doubleExponentialMovingAverage = doubleExponentialMovingAverage;
	}

	public double[] getDoubleExponentialMovingAverage() {
		return this.doubleExponentialMovingAverage;
	}

	public void setDoubleExponentialMovingAverage(double[] doubleExponentialMovingAverage) {
		this.doubleExponentialMovingAverage = doubleExponentialMovingAverage;
	}

	@Override
	public void normalize() {
		this.doubleExponentialMovingAverage = RangeNormalization.normalize(this.doubleExponentialMovingAverage);
	}

	@Override
	public void normalize(double low, double high) {
		this.doubleExponentialMovingAverage = RangeNormalization.normalize(low, high, this.doubleExponentialMovingAverage);
	}

}
