package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AverageTrueRangeResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] averageTrueRange;

	public AverageTrueRangeResponseBean() {
	}

	public AverageTrueRangeResponseBean(double[] averageTrueRange) {
		super();
		this.averageTrueRange = averageTrueRange;
	}

	public double[] getAverageTrueRange() {
		return this.averageTrueRange;
	}

	public void setAverageTrueRange(double[] averageTrueRange) {
		this.averageTrueRange = averageTrueRange;
	}

	@Override
	public void normalize() {
		this.averageTrueRange = RangeNormalization.normalize(this.averageTrueRange);
	}

	@Override
	public void normalize(double low, double high) {
		this.averageTrueRange = RangeNormalization.normalize(low, high, this.averageTrueRange);
	}

}
