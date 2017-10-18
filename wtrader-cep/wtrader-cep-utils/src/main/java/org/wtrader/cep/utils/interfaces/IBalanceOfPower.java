package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.BalanceOfPowerRequestBean;
import org.wtrader.cep.utils.ta.beans.response.BalanceOfPowerResponseBean;

@TechnicalAnalysis(name = "Balance of Power")
public interface IBalanceOfPower {

	public BalanceOfPowerResponseBean calculate(BalanceOfPowerRequestBean requestBean);

}
