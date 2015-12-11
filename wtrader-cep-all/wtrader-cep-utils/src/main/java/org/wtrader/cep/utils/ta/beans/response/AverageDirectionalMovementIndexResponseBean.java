package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AverageDirectionalMovementIndexResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160441L;

	private double[] averageDirectionalMovementIndex;

	public AverageDirectionalMovementIndexResponseBean() {
	}

	public AverageDirectionalMovementIndexResponseBean(double[] averageDirectionalMovementIndex) {
		super();
		this.averageDirectionalMovementIndex = averageDirectionalMovementIndex;
	}

	public double[] getAverageDirectionalMovementIndex() {
		return this.averageDirectionalMovementIndex;
	}

	public void setAverageDirectionalMovementIndex(
			double[] averageDirectionalMovementIndex) {
		this.averageDirectionalMovementIndex = averageDirectionalMovementIndex;
	}

	@Override
	public void normalize() {
		this.averageDirectionalMovementIndex = RangeNormalization.normalize(this.averageDirectionalMovementIndex);
	}

	@Override
	public void normalize(double low, double high) {
		this.averageDirectionalMovementIndex = RangeNormalization.normalize(low, high, this.averageDirectionalMovementIndex);
	}

}
