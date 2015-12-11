package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.interfaces.IHilbertTransformDominantCyclePeriod;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePeriodRequestBean;
import org.wtrader.cep.utils.ta.beans.response.HilbertTransformDominantCyclePeriodResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class HilbertTransformDominantCyclePeriod implements IHilbertTransformDominantCyclePeriod {

	private final Logger LOGGER = Logger.getLogger(HilbertTransformDominantCyclePeriod.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public HilbertTransformDominantCyclePeriod() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IHilbertTransformDominantCyclePeriod)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public HilbertTransformDominantCyclePeriodResponseBean calculate(HilbertTransformDominantCyclePeriodRequestBean requestBean) {
		int numRecords = core.htDcPeriodLookback();
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateHilbertTransformDominantCyclePeriod(data, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private HilbertTransformDominantCyclePeriodResponseBean calculateHilbertTransformDominantCyclePeriod(double[] data, ITACore core) {
		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Hilbert Transform - Dominant Cycle Period."));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[data.length - core.htDcPeriodLookback()];

		RetCode code = core.htDcPeriod(0, data.length - 1, data, beginOut, lengthOut, outReal);

		return new HilbertTransformDominantCyclePeriodResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
