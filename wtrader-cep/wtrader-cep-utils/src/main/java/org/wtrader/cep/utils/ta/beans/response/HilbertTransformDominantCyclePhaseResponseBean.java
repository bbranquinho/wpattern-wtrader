package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class HilbertTransformDominantCyclePhaseResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160626L;

	private double[] hilbertTransformDominantCyclePhase;

	public HilbertTransformDominantCyclePhaseResponseBean() {
	}

	public HilbertTransformDominantCyclePhaseResponseBean(double[] hilbertTransformDominantCyclePhase) {
		super();
		this.hilbertTransformDominantCyclePhase = hilbertTransformDominantCyclePhase;
	}

	public double[] getHilbertTransformDominantCyclePhase() {
		return this.hilbertTransformDominantCyclePhase;
	}

	public void setHilbertTransformDominantCyclePhase(double[] hilbertTransformDominantCyclePhase) {
		this.hilbertTransformDominantCyclePhase = hilbertTransformDominantCyclePhase;
	}

	@Override
	public void normalize() {
		this.hilbertTransformDominantCyclePhase = RangeNormalization.normalize(this.hilbertTransformDominantCyclePhase);
	}

	@Override
	public void normalize(double low, double high) {
		this.hilbertTransformDominantCyclePhase = RangeNormalization.normalize(low, high, this.hilbertTransformDominantCyclePhase);
	}

}
