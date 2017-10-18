package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AroonRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AroonResponseBean;

@TechnicalAnalysis(name = "Aroon")
public interface IAroon {

	public AroonResponseBean calculate(AroonRequestBean requestBean);

}
