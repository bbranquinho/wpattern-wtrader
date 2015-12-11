package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.MACDType;
import org.wtrader.cep.utils.interfaces.IMACD;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.MACDRequestBean;
import org.wtrader.cep.utils.ta.beans.response.MACDResponseBean;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class MACD implements IMACD {

	private final Logger LOGGER = Logger.getLogger(this.getClass());

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public MACD() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IMACD)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public MACDResponseBean calculate(MACDRequestBean requestBean) {
		int period = (requestBean.getFastPeriod() > requestBean.getSlowPeriod()) ? requestBean.getFastPeriod() : requestBean.getSlowPeriod();
		period += requestBean.getSignalPeriod() - 2;

		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), period);

		return this.calculateMACD(data, requestBean.getFastPeriod(), requestBean.getSlowPeriod(), requestBean.getSignalPeriod(),
				requestBean.getMacdType());
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private MACDResponseBean calculateMACD(double[] data, int fastPeriod, int slowPeriod, int signalPeriod, MACDType macdType) {
		// FIXME: Develop the MACD with different types.
		if (macdType != MACDType.EMA) {
			throw new RuntimeException("Method not developed.");
		}

		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(String.format("Processing the Moving Average Convergence / Divergence with fast period [%s], slow period [%s], signal period [%s] and macd type [%s].",
					fastPeriod, slowPeriod, signalPeriod, macdType));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		int lookbackLength = core.emaLookback(signalPeriod);
		lookbackLength += core.emaLookback((fastPeriod > slowPeriod) ? fastPeriod : slowPeriod);
		double[] outMACD = new double[data.length - lookbackLength];
		double[] outMACDSignal = new double[data.length - lookbackLength];
		double[] outMACDHist = new double[data.length - lookbackLength];

		RetCode macdCode = core.macd(0, data.length - 1, data, fastPeriod, slowPeriod, signalPeriod, beginOut, lengthOut, outMACD, outMACDSignal, outMACDHist);

		return (macdCode == RetCode.Success) ? new MACDResponseBean(outMACD, outMACDSignal, outMACDHist) : null;
	}

}
