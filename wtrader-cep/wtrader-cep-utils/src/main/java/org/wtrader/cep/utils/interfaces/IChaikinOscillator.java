package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.ChaikinOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ChaikinOscillatorResponseBean;

@TechnicalAnalysis(name = "Chaikin Ascillator")
public interface IChaikinOscillator {

	public ChaikinOscillatorResponseBean calculate(ChaikinOscillatorRequestBean requestBean);

}
