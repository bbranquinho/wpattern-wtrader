package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.IHilbertTransformDominantCyclePhase;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePhaseRequestBean;
import org.wtrader.cep.utils.ta.beans.response.HilbertTransformDominantCyclePhaseResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class HilbertTransformDominantCyclePhase implements IHilbertTransformDominantCyclePhase {

	private final Logger LOGGER = Logger.getLogger(HilbertTransformDominantCyclePhase.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public HilbertTransformDominantCyclePhase() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IHilbertTransformDominantCyclePhase)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public HilbertTransformDominantCyclePhaseResponseBean calculate(HilbertTransformDominantCyclePhaseRequestBean requestBean) {
		int numRecords = core.htDcPhaseLookback();
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateHilbertTransformDominantCyclePhase(data, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private HilbertTransformDominantCyclePhaseResponseBean calculateHilbertTransformDominantCyclePhase(double[] data, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Hilbert Transform - Dominant Cycle Phase."));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.htDcPhaseLookback()];

		RetCode code = core.htDcPhase(0, data.length - 1, data, beginOut, lengthOut, outReal);

		return new HilbertTransformDominantCyclePhaseResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
