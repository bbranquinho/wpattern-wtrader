package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.EMARequestBean;
import org.wtrader.cep.utils.ta.beans.response.EMAResponseBean;

@TechnicalAnalysis(name = "Exponential Moving Average")
public interface IEMA {

	public EMAResponseBean calculate(EMARequestBean requestBean);

}
