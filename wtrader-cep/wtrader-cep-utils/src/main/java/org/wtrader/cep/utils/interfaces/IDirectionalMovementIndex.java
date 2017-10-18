package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.DirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.DirectionalMovementIndexResponseBean;

@TechnicalAnalysis(name = "Directional Movement Index")
public interface IDirectionalMovementIndex {

	public DirectionalMovementIndexResponseBean calculate(DirectionalMovementIndexRequestBean requestBean);

}
