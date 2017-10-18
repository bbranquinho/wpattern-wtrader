package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePeriodRequestBean;
import org.wtrader.cep.utils.ta.beans.response.HilbertTransformDominantCyclePeriodResponseBean;

@TechnicalAnalysis(name = "Hilbert Trnsform Dominant Cycle Period")
public interface IHilbertTransformDominantCyclePeriod {

	public HilbertTransformDominantCyclePeriodResponseBean calculate(HilbertTransformDominantCyclePeriodRequestBean requestBean);

}
