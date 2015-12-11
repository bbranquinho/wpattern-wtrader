package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.ChaikinLineRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ChaikinLineResponseBean;

@TechnicalAnalysis(name = "Chaikin Line")
public interface IChaikinLine {

	public ChaikinLineResponseBean calculate(ChaikinLineRequestBean requestBean);

}
