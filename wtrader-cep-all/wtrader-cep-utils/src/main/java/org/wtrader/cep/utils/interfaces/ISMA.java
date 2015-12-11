package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.SMARequestBean;
import org.wtrader.cep.utils.ta.beans.response.SMAResponseBean;

@TechnicalAnalysis(name = "Simple Moving Average")
public interface ISMA {

	public SMAResponseBean calculate(SMARequestBean requestBean);

}
