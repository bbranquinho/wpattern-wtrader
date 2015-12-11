package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class DirectionalMovementIndexResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160702L;

	private double[] directionalMovementIndex;

	public DirectionalMovementIndexResponseBean() {
	}

	public DirectionalMovementIndexResponseBean(double[] directionalMovementIndex) {
		super();
		this.directionalMovementIndex = directionalMovementIndex;
	}

	public double[] getDirectionalMovementIndex() {
		return this.directionalMovementIndex;
	}

	public void setDirectionalMovementIndex(double[] directionalMovementIndex) {
		this.directionalMovementIndex = directionalMovementIndex;
	}

	@Override
	public void normalize() {
		this.directionalMovementIndex = RangeNormalization.normalize(this.directionalMovementIndex);
	}

	@Override
	public void normalize(double low, double high) {
		this.directionalMovementIndex = RangeNormalization.normalize(low, high, this.directionalMovementIndex);
	}

}
