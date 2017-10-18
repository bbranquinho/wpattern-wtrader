package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.MACDRequestBean;
import org.wtrader.cep.utils.ta.beans.response.MACDResponseBean;

@TechnicalAnalysis(name = "MACD")
public interface IMACD {

	public MACDResponseBean calculate(MACDRequestBean requestBean);

}
