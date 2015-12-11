package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class RSIResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201311062326L;

	private double[] rsi;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public RSIResponseBean() {
	}

	public RSIResponseBean(double[] rsi) {
		this.rsi = rsi;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (INormalization)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void normalize() {
		this.rsi = RangeNormalization.normalize(this.rsi);
	}

	@Override
	public void normalize(double low, double high) {
		this.rsi = RangeNormalization.normalize(low, high, this.rsi);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// GETTERS/SETTERS METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public double[] getRsi() {
		return this.rsi;
	}

	public void setRsi(double[] rsi) {
		this.rsi = rsi;
	}

}
