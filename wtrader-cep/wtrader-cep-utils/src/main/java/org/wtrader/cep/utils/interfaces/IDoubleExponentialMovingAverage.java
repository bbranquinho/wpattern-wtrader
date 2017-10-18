package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.DoubleExponentialMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.response.DoubleExponentialMovingAverageResponseBean;

@TechnicalAnalysis(name = "Double Exponential Moving Average")
public interface IDoubleExponentialMovingAverage {

	public DoubleExponentialMovingAverageResponseBean calculate(DoubleExponentialMovingAverageRequestBean requestBean);

}
