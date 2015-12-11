package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePhaseRequestBean;
import org.wtrader.cep.utils.ta.beans.response.HilbertTransformDominantCyclePhaseResponseBean;

@TechnicalAnalysis(name = "Hilbert Transform Domain Cycle Phase")
public interface IHilbertTransformDominantCyclePhase {

	public HilbertTransformDominantCyclePhaseResponseBean calculate(HilbertTransformDominantCyclePhaseRequestBean requestBean);

}
