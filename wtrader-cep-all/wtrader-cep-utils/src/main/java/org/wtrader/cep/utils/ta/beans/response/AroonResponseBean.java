package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class AroonResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160543L;

	private double[] aroonDown;

	private double[] aroonUp;

	public AroonResponseBean() {
	}

	public AroonResponseBean(double[] aroonDown, double[] aroonoutUp) {
		super();
		this.aroonDown = aroonDown;
		this.aroonUp = aroonoutUp;
	}

	public double[] getAroonDown() {
		return this.aroonDown;
	}

	public void setAroonDown(double[] aroonDown) {
		this.aroonDown = aroonDown;
	}

	public double[] getAroonUp() {
		return this.aroonUp;
	}

	public void setAroonUp(double[] aroonUp) {
		this.aroonUp = aroonUp;
	}

	@Override
	public void normalize() {
		this.aroonDown = RangeNormalization.normalize(this.aroonDown);
		this.aroonUp = RangeNormalization.normalize(this.aroonUp);
	}

	@Override
	public void normalize(double low, double high) {
		this.aroonDown = RangeNormalization.normalize(low, high, this.aroonDown);
		this.aroonUp = RangeNormalization.normalize(low, high, this.aroonUp);
	}

}
