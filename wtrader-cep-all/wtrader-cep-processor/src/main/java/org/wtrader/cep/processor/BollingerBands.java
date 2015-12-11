package org.wtrader.cep.processor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IDataSearch;
import org.wtrader.cep.utils.enums.BollingerBandsMAType;
import org.wtrader.cep.utils.interfaces.IBollingerBands;
import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.beans.request.BollingerBandsRequestBean;
import org.wtrader.cep.utils.ta.beans.response.BollingerBandsResponseBean;
import org.wtrader.cep.utils.ta.enums.MAType;
import org.wtrader.cep.utils.ta.enums.RetCode;
import org.wtrader.cep.utils.ta.interfaces.ITACore;

@Named
public class BollingerBands implements IBollingerBands {

	private static final Logger LOGGER = Logger.getLogger(BollingerBands.class);

	@Inject
	private IDataSearch dataStoreByStockAndDate;

	@Inject
	private ITACore core;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public BollingerBands() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS (IBollingerBands)
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public BollingerBandsResponseBean calculate(BollingerBandsRequestBean requestBean) {
		int period = requestBean.getPeriod();
		MAType maType = this.convertMAType(requestBean.getBollingerBandsMAType());
		double devUp = requestBean.getDevUp();
		double devDn = requestBean.getDevDn();
		int numRecords = core.bbandsLookback(period, devUp, devDn, maType);
		double[] data = this.dataStoreByStockAndDate.findBetweenAndBelowByDataType(requestBean.getStockName(),
				requestBean.getDataType(), requestBean.getStartDate(), requestBean.getEndDate(), numRecords);

		return this.calculateBollingerBands(data, period, devUp, devDn, maType, core);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private BollingerBandsResponseBean calculateBollingerBands(double[] data, int period, double devUp, double devDn, MAType maType, ITACore core) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Processing the Bollinger Bands with period [%s], deviation up [%s], deviation down [%s] and MA Type [%s].",
					period, devUp, devDn, maType));
		}

		MInteger beginOut = new MInteger();
		MInteger lengthOut = new MInteger();
		double[] outUpperBand = new double[data.length - core.bbandsLookback(period, devUp, devDn, maType)];
		double[] outMiddleBand = new double[data.length - core.bbandsLookback(period, devUp, devDn, maType)];
		double[] outLowerBand = new double[data.length - core.bbandsLookback(period, devUp, devDn, maType)];

		RetCode code = core.bbands(0, data.length - 1, data, period, devUp, devDn, maType, beginOut, lengthOut, outUpperBand,
				outMiddleBand, outLowerBand);

		return new BollingerBandsResponseBean((code == RetCode.Success) ? outUpperBand : null,
				(code == RetCode.Success) ? outMiddleBand : null, (code == RetCode.Success) ? outLowerBand : null);
	}

	private MAType convertMAType(BollingerBandsMAType maType) {
		switch (maType) {
		case DEMA: return MAType.Dema;
		case EMA: return MAType.Ema;
		case KAMA: return MAType.Kama;
		case MAMA: return MAType.Mama;
		case SMA: return MAType.Sma;
		case T3: return MAType.T3;
		case TEMA: return MAType.Tema;
		case TRIMA: return MAType.Trima;
		case WMA: return MAType.Wma;
		}

		LOGGER.error(String.format("Type not founded to MAType [%s].", maType));

		return null;
	}

}
