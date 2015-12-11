package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class BollingerBandsResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160702L;

	private double[] upperBollingerBands;

	private double[] middleBollingerBands;

	private double[] lowerBollingerBands;

	public BollingerBandsResponseBean() {
	}

	public BollingerBandsResponseBean(double[] upperBollingerBands, double[] middleBollingerBands, double[] lowerBollingerBands) {
		super();
		this.upperBollingerBands = upperBollingerBands;
		this.middleBollingerBands = middleBollingerBands;
		this.lowerBollingerBands = lowerBollingerBands;
	}

	public double[] getUpperBollingerBands() {
		return this.upperBollingerBands;
	}

	public void setUpperBollingerBands(double[] upperBollingerBands) {
		this.upperBollingerBands = upperBollingerBands;
	}

	public double[] getMiddleBollingerBands() {
		return this.middleBollingerBands;
	}

	public void setMiddleBollingerBands(double[] middleBollingerBands) {
		this.middleBollingerBands = middleBollingerBands;
	}

	public double[] getLowerBollingerBands() {
		return this.lowerBollingerBands;
	}

	public void setLowerBollingerBands(double[] lowerBollingerBands) {
		this.lowerBollingerBands = lowerBollingerBands;
	}

	@Override
	public void normalize() {
		this.upperBollingerBands = RangeNormalization.normalize(this.upperBollingerBands);
		this.middleBollingerBands = RangeNormalization.normalize(this.middleBollingerBands);
		this.lowerBollingerBands = RangeNormalization.normalize(this.lowerBollingerBands);
	}

	@Override
	public void normalize(double low, double high) {
		this.upperBollingerBands = RangeNormalization.normalize(low, high, this.upperBollingerBands);
		this.middleBollingerBands = RangeNormalization.normalize(low, high, this.middleBollingerBands);
		this.lowerBollingerBands = RangeNormalization.normalize(low, high, this.lowerBollingerBands);
	}

}
