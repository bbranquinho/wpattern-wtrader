package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.StochasticRequestBean;
import org.wtrader.cep.utils.ta.beans.response.StochasticResponseBean;

@TechnicalAnalysis(name = "Stochastic")
public interface IStochastic {

	public StochasticResponseBean calculate(StochasticRequestBean requestBean);

}
