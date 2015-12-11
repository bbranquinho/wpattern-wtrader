package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.LinearRegressionSlopeRequestBean;
import org.wtrader.cep.utils.ta.beans.response.LinearRegressionSlopeResponseBean;

@TechnicalAnalysis(name = "Linear Regression Slope")
public interface ILinearRegressionSlope {

	public LinearRegressionSlopeResponseBean calculate(LinearRegressionSlopeRequestBean requestBean);

}
