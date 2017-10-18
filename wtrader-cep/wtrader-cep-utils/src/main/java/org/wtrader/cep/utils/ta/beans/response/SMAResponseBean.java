package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class SMAResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201309030302L;

	private double[] sma;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public SMAResponseBean() {
	}

	public SMAResponseBean(double[] sma) {
		super();
		this.sma = sma;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (INormalization)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void normalize() {
		this.sma = RangeNormalization.normalize(this.sma);
	}

	@Override
	public void normalize(double low, double high) {
		this.sma = RangeNormalization.normalize(low, high, this.sma);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// GETTERS/SETTERS METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public double[] getSma() {
		return this.sma;
	}

	public void setSma(double[] sma) {
		this.sma = sma;
	}

}
