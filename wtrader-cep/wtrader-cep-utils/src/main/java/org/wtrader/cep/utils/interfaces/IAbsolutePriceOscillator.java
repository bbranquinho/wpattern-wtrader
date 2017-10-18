package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.AbsolutePriceOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.response.AbsolutePriceOscillatorResponseBean;

@TechnicalAnalysis(name = "Absolute Price Oscillator")
public interface IAbsolutePriceOscillator {

	public AbsolutePriceOscillatorResponseBean calculate(AbsolutePriceOscillatorRequestBean requestBean);

}
