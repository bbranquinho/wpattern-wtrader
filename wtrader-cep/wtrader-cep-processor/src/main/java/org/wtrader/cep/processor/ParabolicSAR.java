package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.interfaces.IParabolicSAR;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.ParabolicSARRequestBean;
import org.wtrader.cep.utils.ta.beans.response.ParabolicSARResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class ParabolicSAR implements IParabolicSAR {

	private static final Logger LOGGER = Logger.getLogger(ParabolicSAR.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public ParabolicSAR() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IParabolicSAR)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ParabolicSARResponseBean calculate(ParabolicSARRequestBean requestBean) {
		double acceleration = requestBean.getAcceleration();
		double maximum = requestBean.getMaximum();
		int numRecords = core.sarLookback(acceleration, maximum);
		double[] high = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.HIGHEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);
		double[] low = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				DataType.LOWEST_NEGOTIATION_PRICE, requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateParabolicSAR(high, low, acceleration, maximum, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public ParabolicSARResponseBean calculateParabolicSAR(double[] high, double[] low, double acceleration, double maximum, ITACore core) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Processing the Parabolic SAR with accelaration [%s] and maximun [%s].", acceleration, maximum));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outReal = new double[high.length - core.sarLookback(acceleration, maximum)];

		RetCode code = core.sar(0, high.length - 1, high, low, acceleration, maximum, beginOut, lengthOut, outReal);

		return new ParabolicSARResponseBean((code == RetCode.Success) ? outReal : null);
	}

}
