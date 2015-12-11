package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageDirectionalMovementIndexResponseBean;

@TechnicalAnalysis(name = "Average Directional Movement Index")
public interface IAverageDirectionalMovementIndex {

	public AverageDirectionalMovementIndexResponseBean calculate(AverageDirectionalMovementIndexRequestBean requestBean);

}
