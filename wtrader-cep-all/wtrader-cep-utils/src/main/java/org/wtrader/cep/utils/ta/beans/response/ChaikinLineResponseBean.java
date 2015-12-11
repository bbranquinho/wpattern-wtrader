package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class ChaikinLineResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160401L;

	private double[] chaikinLine;

	public ChaikinLineResponseBean() {
	}

	public ChaikinLineResponseBean(double[] chaikinLine) {
		super();
		this.chaikinLine = chaikinLine;
	}

	public double[] getChaikinLine() {
		return this.chaikinLine;
	}

	public void setChaikinLine(double[] chaikinLine) {
		this.chaikinLine = chaikinLine;
	}

	@Override
	public void normalize() {
		this.chaikinLine = RangeNormalization.normalize(this.chaikinLine);
	}

	@Override
	public void normalize(double low, double high) {
		this.chaikinLine = RangeNormalization.normalize(low, high, this.chaikinLine);
	}

}
