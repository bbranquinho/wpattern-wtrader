package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.RSIRequestBean;
import org.wtrader.cep.utils.ta.beans.response.RSIResponseBean;

@TechnicalAnalysis(name = "RSI")
public interface IRSI {

	public RSIResponseBean calculate(RSIRequestBean requestBean);

}
