package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AverageDirectionalMovementIndexRatingResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160441L;

	private double[] averageDirectionalMovementIndexRating;

	public AverageDirectionalMovementIndexRatingResponseBean() {
	}

	public AverageDirectionalMovementIndexRatingResponseBean(double[] averageDirectionalMovementIndexRating) {
		super();
		this.averageDirectionalMovementIndexRating = averageDirectionalMovementIndexRating;
	}

	public double[] getAverageDirectionalMovementIndexRating() {
		return this.averageDirectionalMovementIndexRating;
	}

	public void setAverageDirectionalMovementIndexRating(double[] averageDirectionalMovementIndexRating) {
		this.averageDirectionalMovementIndexRating = averageDirectionalMovementIndexRating;
	}

	@Override
	public void normalize() {
		this.averageDirectionalMovementIndexRating = RangeNormalization.normalize(this.averageDirectionalMovementIndexRating);
	}

	@Override
	public void normalize(double low, double high) {
		this.averageDirectionalMovementIndexRating = RangeNormalization.normalize(low, high, this.averageDirectionalMovementIndexRating);
	}

}
