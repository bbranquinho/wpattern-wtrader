package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class KaufmanAdaptiveMovingAverageResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] kaufmanAdaptiveMovingAverage;

	public KaufmanAdaptiveMovingAverageResponseBean() {
	}

	public KaufmanAdaptiveMovingAverageResponseBean(double[] kaufmanAdaptiveMovingAverage) {
		super();
		this.kaufmanAdaptiveMovingAverage = kaufmanAdaptiveMovingAverage;
	}

	public double[] getKaufmanAdaptiveMovingAverage() {
		return this.kaufmanAdaptiveMovingAverage;
	}

	public void setKaufmanAdaptiveMovingAverage(double[] kaufmanAdaptiveMovingAverage) {
		this.kaufmanAdaptiveMovingAverage = kaufmanAdaptiveMovingAverage;
	}

	@Override
	public void normalize() {
		this.kaufmanAdaptiveMovingAverage = RangeNormalization.normalize(this.kaufmanAdaptiveMovingAverage);
	}

	@Override
	public void normalize(double low, double high) {
		this.kaufmanAdaptiveMovingAverage = RangeNormalization.normalize(low, high, this.kaufmanAdaptiveMovingAverage);
	}

}
