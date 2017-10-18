package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRatingRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageDirectionalMovementIndexRatingResponseBean;

@TechnicalAnalysis(name = "Average Directional Movement Index Rating")
public interface IAverageDirectionalMovementIndexRating {

	public AverageDirectionalMovementIndexRatingResponseBean calculate(AverageDirectionalMovementIndexRatingRequestBean requestBean);

}
