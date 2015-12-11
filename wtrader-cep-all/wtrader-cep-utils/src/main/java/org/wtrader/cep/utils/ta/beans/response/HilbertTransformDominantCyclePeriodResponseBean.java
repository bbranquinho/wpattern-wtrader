package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class HilbertTransformDominantCyclePeriodResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] hilbertTransformDominantCyclePeriod;

	public HilbertTransformDominantCyclePeriodResponseBean() {
	}

	public HilbertTransformDominantCyclePeriodResponseBean(double[] hilbertTransformDominantCyclePeriod) {
		super();
		this.hilbertTransformDominantCyclePeriod = hilbertTransformDominantCyclePeriod;
	}

	public double[] getHilbertTransformDominantCyclePeriod() {
		return this.hilbertTransformDominantCyclePeriod;
	}

	public void setHilbertTransformDominantCyclePeriod(double[] hilbertTransformDominantCyclePeriod) {
		this.hilbertTransformDominantCyclePeriod = hilbertTransformDominantCyclePeriod;
	}

	@Override
	public void normalize() {
		this.hilbertTransformDominantCyclePeriod = RangeNormalization.normalize(this.hilbertTransformDominantCyclePeriod);
	}

	@Override
	public void normalize(double low, double high) {
		this.hilbertTransformDominantCyclePeriod = RangeNormalization.normalize(low, high, this.hilbertTransformDominantCyclePeriod);
	}

}
