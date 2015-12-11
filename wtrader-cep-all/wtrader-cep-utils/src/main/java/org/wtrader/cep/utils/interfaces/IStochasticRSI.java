package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.StochasticRSIRequestBean;
import org.wtrader.cep.utils.ta.beans.response.StochasticRSIResponseBean;

@TechnicalAnalysis(name = "Stochastic RSI")
public interface IStochasticRSI {

	public StochasticRSIResponseBean calculate(StochasticRSIRequestBean requestBean);

}
