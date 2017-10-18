package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.ParabolicSARRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ParabolicSARResponseBean;

@TechnicalAnalysis(name = "Parabolic SAR")
public interface IParabolicSAR {

	public ParabolicSARResponseBean calculate(ParabolicSARRequestBean requestBean);

}
