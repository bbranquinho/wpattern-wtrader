package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AroonOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AroonOscillatorResponseBean;

@TechnicalAnalysis(name = "Aroon Oscillator")
public interface IAroonOscillator {

	public AroonOscillatorResponseBean calculate(AroonOscillatorRequestBean requestBean);

}
