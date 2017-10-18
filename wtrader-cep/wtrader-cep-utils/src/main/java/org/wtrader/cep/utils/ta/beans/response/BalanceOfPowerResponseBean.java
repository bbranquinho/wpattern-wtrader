package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class BalanceOfPowerResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160642L;

	private double[] balanceOfPower;

	public BalanceOfPowerResponseBean() {
	}

	public BalanceOfPowerResponseBean(double[] balanceOfPower) {
		super();
		this.balanceOfPower = balanceOfPower;
	}

	public double[] getBalanceOfPower() {
		return this.balanceOfPower;
	}

	public void setBalanceOfPower(double[] balanceOfPower) {
		this.balanceOfPower = balanceOfPower;
	}

	@Override
	public void normalize() {
		this.balanceOfPower = RangeNormalization.normalize(this.balanceOfPower);
	}

	@Override
	public void normalize(double low, double high) {
		this.balanceOfPower = RangeNormalization.normalize(low, high, this.balanceOfPower);
	}

}
