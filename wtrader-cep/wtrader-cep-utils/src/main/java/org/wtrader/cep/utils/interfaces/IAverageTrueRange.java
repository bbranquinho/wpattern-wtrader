package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AverageTrueRangeRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AverageTrueRangeResponseBean;

@TechnicalAnalysis(name = "Average True Range")
public interface IAverageTrueRange {

	public AverageTrueRangeResponseBean calculate(AverageTrueRangeRequestBean requestBean);

}
