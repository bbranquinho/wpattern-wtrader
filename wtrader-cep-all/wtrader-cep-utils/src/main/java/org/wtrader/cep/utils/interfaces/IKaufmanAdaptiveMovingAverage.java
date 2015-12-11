package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.KaufmanAdaptiveMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.response.KaufmanAdaptiveMovingAverageResponseBean;

@TechnicalAnalysis(name = "Kaufman Adaptative Moving Average")
public interface IKaufmanAdaptiveMovingAverage {

	public KaufmanAdaptiveMovingAverageResponseBean calculate(KaufmanAdaptiveMovingAverageRequestBean requestBean);

}
