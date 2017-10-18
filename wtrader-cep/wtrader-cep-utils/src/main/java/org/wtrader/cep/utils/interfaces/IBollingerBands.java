package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.BollingerBandsRequestBean;
import org.wtrader.cep.utils.ta.beans.response.BollingerBandsResponseBean;

@TechnicalAnalysis(name = "Bollinger Bands")
public interface IBollingerBands {

	public BollingerBandsResponseBean calculate(BollingerBandsRequestBean requestBean);

}
