package org.wtrader.cep.utils.ta.beans.utils;

import java.util.Date;

import org.wtrader.cep.utils.annotations.DefaultValue;
import org.wtrader.cep.utils.annotations.TechnicalAnalysisMethod;
import org.wtrader.cep.utils.enums.AbsolutePriceOscillatorMAType;
import org.wtrader.cep.utils.enums.BollingerBandsMAType;
import org.wtrader.cep.utils.enums.DataType;
import org.wtrader.cep.utils.enums.MACDType;
import org.wtrader.cep.utils.enums.StochasticMAType;
import org.wtrader.cep.utils.enums.StochasticRSIMAType;
import org.wtrader.cep.utils.ta.beans.request.AbsolutePriceOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.request.AroonOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.request.AroonRequestBean;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRatingRequestBean;
import org.wtrader.cep.utils.ta.beans.request.AverageDirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.request.AverageTrueRangeRequestBean;
import org.wtrader.cep.utils.ta.beans.request.BalanceOfPowerRequestBean;
import org.wtrader.cep.utils.ta.beans.request.BollingerBandsRequestBean;
import org.wtrader.cep.utils.ta.beans.request.ChaikinLineRequestBean;
import org.wtrader.cep.utils.ta.beans.request.ChaikinOscillatorRequestBean;
import org.wtrader.cep.utils.ta.beans.request.CommodityChannelIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.request.DirectionalMovementIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.request.DoubleExponentialMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.request.EMARequestBean;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePeriodRequestBean;
import org.wtrader.cep.utils.ta.beans.request.HilbertTransformDominantCyclePhaseRequestBean;
import org.wtrader.cep.utils.ta.beans.request.KaufmanAdaptiveMovingAverageRequestBean;
import org.wtrader.cep.utils.ta.beans.request.LinearRegressionSlopeRequestBean;
import org.wtrader.cep.utils.ta.beans.request.MACDRequestBean;
import org.wtrader.cep.utils.ta.beans.request.ParabolicSARRequestBean;
import org.wtrader.cep.utils.ta.beans.request.RSIRequestBean;
import org.wtrader.cep.utils.ta.beans.request.SMARequestBean;
import org.wtrader.cep.utils.ta.beans.request.StochasticRSIRequestBean;
import org.wtrader.cep.utils.ta.beans.request.StochasticRequestBean;

public final class RequestBeanFactory {

	// MACD, SMA, EMA, RSI, ABSOLUTE PRICE OSCILLATOR, DOUBLE EXPONENTIAL MOVING AVERAGE
	// HILBERT TRANSFORM - DOMINANT CYCLE PERIOD, HILBERT TRANSFORM - DOMINANT CYCLE PHASE,
	// STOCHASTIC RSI, KAUFMAN ADAPTIVE MOVING AVERAGE
	public static final DataType DATA_TYPE_DEFAULT = DataType.CLOSING_NEGOTIATION_PRICE;

	// SMA, EMA
	public static final int SHORT_PERIOD_DEFAULT = 20;

	public static final int LONG_PERIOD_DEFAULT = 100;

	// MACD
	public static final MACDType MACD_TYPE_DEFAULT = MACDType.EMA;

	public static final int MACD_FAST_PERIOD_DEFAULT = 12;

	public static final int MACD_SLOW_PERIOD_DEFAULT = 26;

	public static final int MACD_SIGNAL_PERIOD_DEFAULT = 9;

	// RSI
	public static final int RSI_PERIOD_DEFAULT = 14;

	// CHAIKIN OSCILLATOR
	public static final int CHAIKIN_OSCILLATOR_FAST_PERIOD_DEFAULT = 3;

	public static final int CHAIKIN_OSCILLATOR_SLOW_PERIOD_DEFAULT = 10;

	// AVERAGE DIRECTIONAL MOVEMENT INDEX
	public static final int AVERAGE_DIRECTIONAL_MOVEMENT_INDEX_PERIOD_DEFAULT = 14;

	// AVERAGE DIRECTIONAL MOVEMENT INDEX RAITING
	public static final int AVERAGE_DIRECTIONAL_MOVEMENT_INDEX_RATING_PERIOD_DEFAULT = 14;

	// AROON
	public static final int AROON_PERIOD_DEFAULT = 25;

	// AROON OSCILLATOR
	public static final int AROON_OSCILLATOR_PERIOD_DEFAULT = 25;

	// AVERAGE TRUE RANGE
	public static final int AVERAGE_TRUE_RANGE_PERIOD_DEFAULT = 14;

	// COMODITY CHANNEL INDEX
	public static final int COMODITY_CHANNEL_INDEX_PERIOD_DEFAULT = 20;

	// DIRECTIONAL MOVEMENT INDEX
	public static final int DIRECTIONAL_MOVEMENT_INDEX_PERIOD_DEFAULT = 14;

	// PARABOLIC SAR
	public static final double PARABOLIC_SAR_ACCELERATION_DEFAULT = 0.02d;

	public static final double PARABOLIC_SAR_MAXIMUM_DEFAULT = 0.2d;

	// BOLLINGER BANDS
	public static final int BOLLINGER_BANDS_PERIOD_DEFAULT = 20;

	public static final BollingerBandsMAType BOLLINGER_BANDS_MA_TYPE_DEFAULT = BollingerBandsMAType.SMA;

	public static final double BOLLINGER_BANDS_DEVIATION_UP_DEFAULT = 2.0;

	public static final double BOLLINGER_BANDS_DEVIATION_DOWN_DEFAULT = 2.0;

	// ABSOLUTE PRICE OSCILLATOR
	public static final int ABSOLUTE_PRICE_OSCILLATOR_SLOW_PERIOD_DEFAULT = LONG_PERIOD_DEFAULT;

	public static final int ABSOLUTE_PRICE_OSCILLATOR_FAST_PERIOD_DEFAULT = SHORT_PERIOD_DEFAULT;

	public static final AbsolutePriceOscillatorMAType ABSOLUTE_PRICE_OSCILLATOR_MA_TYPE_DEFAULT = AbsolutePriceOscillatorMAType.EMA;

	public static final int STOCHASTIC_RSI_TIME_PERIOD_DEFAULT = 14;

	public static final int STOCHASTIC_RSI_FAST_K_PERIOD_DEFAULT = 14;

	public static final int STOCHASTIC_RSI_FAST_D_PERIOD = 14;

	public static final StochasticRSIMAType STOCHASTIC_RSI_MA_TYPE_DEFAULT = StochasticRSIMAType.SMA;

	// DOUBLE EXPONENTIAL MOVING AVERAGE_
	public static final int DOUBLE_EXPONENTIAL_MOVING_AVERAGE_PERIOD_DEFAULT = 20;

	// KAUFMAN ADAPTIVE MOVING AVERAGE
	public static final int KAUFMAN_ADAPTIVE_MOVING_AVERAGE_PERIOD_DEFAULT = 50;

	// LINEAR REGRESSION SLOPE
	private static final int LINEAR_REGRESSION_SLOPE_PERIOD_DEFAULT = 20;

	// STOCHASTIC
	private static final int STOCHASTIC_FAST_K_PERIOD_DEFAULT = 14;

	private static final int STOCHASTIC_FAST_D_PERIOD_DEFAULT = 14;

	private static final int STOCHASTIC_SLOW_K_PERIOD_DEFAULT = 50;

	private static final StochasticMAType STOCHASTIC_SLOW_K_MA_TYPE_DEFAULT = StochasticMAType.SMA;

	private static final int STOCHASTIC_SLOW_D_PERIOD_DEFAULT = 50;

	private static final StochasticMAType STOCHASTIC_SLOW_D_MA_TYPE_DEFAULT = StochasticMAType.SMA;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	private RequestBeanFactory() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// SMA REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod(
			paramsName = { "Period", "Stock Name", "Start Date", "End Date" },
			defaultValues = { @DefaultValue(name = "Data Type", value = "Closing Negotiation Price") })
	public static SMARequestBean instanceSMABean(int period, String stockName, Date startDate, Date endDate) {
		return new SMARequestBean(period, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod(paramsName = { "Data Type", "Period", "Stock Name", "Start Date", "End Date" })
	public static SMARequestBean instanceSMABean(DataType dataType, int period, String stockName, Date startDate,
			Date endDate) {
		return new SMARequestBean(period, stockName, startDate, endDate, dataType);
	}

	@TechnicalAnalysisMethod(
			paramsName = { "Stock Name", "Start Date", "End Date" },
			defaultValues = {
					@DefaultValue(name = "Period", value = "Short Period (" + SHORT_PERIOD_DEFAULT + ")"),
					@DefaultValue(name = "Data Type", value = "Closing Negotiation Price")
			})
	public static SMARequestBean instanceSMABeanShort(String stockName, Date startDate, Date endDate) {
		return new SMARequestBean(SHORT_PERIOD_DEFAULT, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod(
			paramsName = { "Date Type",  "Stock Name", "Start Date", "End Date" },
			defaultValues = { @DefaultValue(name = "Period", value = "Short Period (" + SHORT_PERIOD_DEFAULT + ")") })
	public static SMARequestBean instanceSMABeanShort(DataType dataType, String stockName, Date startDate,
			Date endDate) {
		return new SMARequestBean(SHORT_PERIOD_DEFAULT, stockName, startDate, endDate, dataType);
	}

	@TechnicalAnalysisMethod(
			paramsName = { "Stock Name", "Start Date", "End Date" },
			defaultValues = {
					@DefaultValue(name = "Period", value = "Short Period (" + LONG_PERIOD_DEFAULT + ")"),
					@DefaultValue(name = "Data Type", value = "Closing Negotiation Price")
			})
	public static SMARequestBean instanceSMABeanLong(String stockName, Date startDate, Date endDate) {
		return new SMARequestBean(LONG_PERIOD_DEFAULT, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod(
			paramsName = { "Date Type",  "Stock Name", "Start Date", "End Date" },
			defaultValues = { @DefaultValue(name = "Period", value = "Short Period (" + LONG_PERIOD_DEFAULT + ")") })
	public static SMARequestBean instanceSMABeanLong(DataType dataType, String stockName, Date startDate,
			Date endDate) {
		return new SMARequestBean(LONG_PERIOD_DEFAULT, stockName, startDate, endDate, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// EMA REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABean(int period, String stockName, Date startDate, Date endDate) {
		return new EMARequestBean(period, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABean(DataType dataType, int period, String stockName, Date startDate,
			Date endDate) {
		return new EMARequestBean(period, stockName, startDate, endDate, dataType);
	}

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABeanShort(String stockName, Date startDate, Date endDate) {
		return new EMARequestBean(SHORT_PERIOD_DEFAULT, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABeanShort(DataType dataType, String stockName, Date startDate,
			Date endDate) {
		return new EMARequestBean(SHORT_PERIOD_DEFAULT, stockName, startDate, endDate, dataType);
	}

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABeanLong(String stockName, Date startDate, Date endDate) {
		return new EMARequestBean(LONG_PERIOD_DEFAULT, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static EMARequestBean instanceEMABeanLong(DataType dataType, String stockName, Date startDate,
			Date endDate) {
		return new EMARequestBean(LONG_PERIOD_DEFAULT, stockName, startDate, endDate, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// MACD REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(String stockName, Date start, Date end) {
		return new MACDRequestBean(MACD_FAST_PERIOD_DEFAULT, MACD_SLOW_PERIOD_DEFAULT, MACD_SIGNAL_PERIOD_DEFAULT,
				MACD_TYPE_DEFAULT, stockName, start, end, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(String stockName, Date start, Date end, MACDType macdType) {
		return new MACDRequestBean(MACD_FAST_PERIOD_DEFAULT, MACD_SLOW_PERIOD_DEFAULT, MACD_SIGNAL_PERIOD_DEFAULT,
				macdType, stockName, start, end, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(String stockName, Date start, Date end, int fastPeriod,
			int slowPeriod, int signalPeriod) {
		return new MACDRequestBean(fastPeriod, slowPeriod, signalPeriod, MACD_TYPE_DEFAULT, stockName,
				start, end, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(String stockName, Date start, Date end, int fastPeriod,
			int slowPeriod, int signalPeriod, MACDType macdType) {
		return new MACDRequestBean(fastPeriod, slowPeriod, signalPeriod, macdType, stockName, start, end,
				DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(DataType dataType, String stockName, Date start, Date end) {
		return new MACDRequestBean(MACD_FAST_PERIOD_DEFAULT, MACD_SLOW_PERIOD_DEFAULT, MACD_SIGNAL_PERIOD_DEFAULT,
				MACD_TYPE_DEFAULT, stockName, start, end, dataType);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(DataType dataType, String stockName, Date start, Date end,
			MACDType macdType) {
		return new MACDRequestBean(MACD_FAST_PERIOD_DEFAULT, MACD_SLOW_PERIOD_DEFAULT, MACD_SIGNAL_PERIOD_DEFAULT,
				macdType, stockName, start, end, dataType);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(DataType dataType, String stockName, Date start, Date end,
			int fastPeriod, int slowPeriod, int signalPeriod) {
		return new MACDRequestBean(fastPeriod, slowPeriod, signalPeriod, MACD_TYPE_DEFAULT, stockName,
				start, end, dataType);
	}

	@TechnicalAnalysisMethod
	public static MACDRequestBean instanceMACDBean(DataType dataType, String stockName, Date start, Date end,
			int fastPeriod, int slowPeriod, int signalPeriod, MACDType macdType) {
		return new MACDRequestBean(fastPeriod, slowPeriod, signalPeriod, macdType, stockName, start, end,
				dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// RSI REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static RSIRequestBean instanceRSIBean(int period, String stockName, Date startDate, Date endDate) {
		return new RSIRequestBean(period, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static RSIRequestBean instanceRSIBean(DataType dataType, int period, String stockName, Date startDate,
			Date endDate) {
		return new RSIRequestBean(period, stockName, startDate, endDate, dataType);
	}

	@TechnicalAnalysisMethod
	public static RSIRequestBean instanceRSIBean(String stockName, Date startDate, Date endDate) {
		return new RSIRequestBean(RSI_PERIOD_DEFAULT, stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static RSIRequestBean instanceRSIBean(DataType dataType, String stockName, Date startDate, Date endDate) {
		return new RSIRequestBean(RSI_PERIOD_DEFAULT, stockName, startDate, endDate, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// CHAIKIN OSCILLATOR REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static ChaikinOscillatorRequestBean instanceChaikinOscillatorBean(String stockName, Date startDate, Date endDate) {
		return new ChaikinOscillatorRequestBean(stockName, startDate, endDate, CHAIKIN_OSCILLATOR_FAST_PERIOD_DEFAULT, CHAIKIN_OSCILLATOR_SLOW_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static ChaikinOscillatorRequestBean instanceChaikinOscillatorBean(String stockName, Date startDate, Date endDate, int fastPeriod, int slowPeriod) {
		return new ChaikinOscillatorRequestBean(stockName, startDate, endDate, fastPeriod, slowPeriod);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// CHAIKIN LINE REQUEST BEAN
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static ChaikinLineRequestBean instanceChaikinLineBean(String stockName, Date startDate, Date endDate) {
		return new ChaikinLineRequestBean(stockName, startDate, endDate);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// AVERAGE DIRECTIONAL MOVEMENT INDEX
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AverageDirectionalMovementIndexRequestBean instanceAverageDirectionalMovementIndexBean(String stockName, Date startDate,
			Date endDate) {
		return new AverageDirectionalMovementIndexRequestBean(stockName, AVERAGE_DIRECTIONAL_MOVEMENT_INDEX_PERIOD_DEFAULT, startDate, endDate);
	}

	@TechnicalAnalysisMethod
	public static AverageDirectionalMovementIndexRequestBean instanceAverageDirectionalMovementIndexBean(String stockName, Date startDate,
			Date endDate, int period) {
		return new AverageDirectionalMovementIndexRequestBean(stockName, period, startDate, endDate);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// AVERAGE DIRECTIONAL MOVEMENT INDEX RATING
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AverageDirectionalMovementIndexRatingRequestBean instanceAverageDirectionalMovementIndexRatingBean(
			String stockName, Date startDate, Date endDate) {
		return new AverageDirectionalMovementIndexRatingRequestBean(stockName, AVERAGE_DIRECTIONAL_MOVEMENT_INDEX_RATING_PERIOD_DEFAULT, startDate, endDate);
	}

	@TechnicalAnalysisMethod
	public static AverageDirectionalMovementIndexRatingRequestBean instanceAverageDirectionalMovementIndexRatingBean(
			String stockName, Date startDate, Date endDate, int period) {
		return new AverageDirectionalMovementIndexRatingRequestBean(stockName, period, startDate, endDate);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// AROON
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AroonRequestBean instanceAroonBean(String stockName, Date startDate, Date endDate) {
		return new AroonRequestBean(stockName, startDate, endDate, AROON_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static AroonRequestBean instanceAroonBean(String stockName, Date startDate, Date endDate, int period) {
		return new AroonRequestBean(stockName, startDate, endDate, period);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// AROON OSCILLATOR
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AroonOscillatorRequestBean instanceAroonOscillatorBean(String stockName, Date startDate, Date endDate) {
		return new AroonOscillatorRequestBean(stockName, startDate, endDate, AROON_OSCILLATOR_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static AroonOscillatorRequestBean instanceAroonOscillatorBean(String stockName, Date startDate, Date endDate, int period) {
		return new AroonOscillatorRequestBean(stockName, startDate, endDate, period);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// AVERAGE TRUE RANGE
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AverageTrueRangeRequestBean instanceAverageTrueRangeBean(String stockName, Date startDate, Date endDate) {
		return new AverageTrueRangeRequestBean(stockName, startDate, endDate, AVERAGE_TRUE_RANGE_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static AverageTrueRangeRequestBean instanceAverageTrueRangeBean(String stockName, Date startDate, Date endDate, int period) {
		return new AverageTrueRangeRequestBean(stockName, startDate, endDate, period);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// BALANCE OF POWER
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static BalanceOfPowerRequestBean instanceBalanceOfPowerBean(String stockName, Date startDate, Date endDate) {
		return new BalanceOfPowerRequestBean(stockName, startDate, endDate);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// COMODITY CHANNEL INDEX
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static CommodityChannelIndexRequestBean instanceCommodityChannelIndexBean(String stockName, Date startDate, Date endDate) {
		return new CommodityChannelIndexRequestBean(stockName, startDate, endDate, COMODITY_CHANNEL_INDEX_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static CommodityChannelIndexRequestBean instanceCommodityChannelIndexBean(String stockName, Date startDate, Date endDate, int period) {
		return new CommodityChannelIndexRequestBean(stockName, startDate, endDate, period);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// DIRECTIONAL MOVEMENT INDEX
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static DirectionalMovementIndexRequestBean instanceDirectionalMovementIndexBean(String stockName, Date startDate, Date endDate) {
		return new DirectionalMovementIndexRequestBean(stockName, startDate, endDate, DIRECTIONAL_MOVEMENT_INDEX_PERIOD_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static DirectionalMovementIndexRequestBean instanceDirectionalMovementIndexBean(String stockName, Date startDate, Date endDate, int period) {
		return new DirectionalMovementIndexRequestBean(stockName, startDate, endDate, period);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PARABOLIC SAR
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static ParabolicSARRequestBean instanceParabolicSARBean(String stockName, Date startDate, Date endDate) {
		return new ParabolicSARRequestBean(stockName, startDate, endDate, PARABOLIC_SAR_ACCELERATION_DEFAULT, PARABOLIC_SAR_MAXIMUM_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static ParabolicSARRequestBean instanceParabolicSARBean(String stockName, Date startDate, Date endDate, double acceleration, double maximum) {
		return new ParabolicSARRequestBean(stockName, startDate, endDate, acceleration, maximum);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// BOLLINGER BANDS
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static BollingerBandsRequestBean instanceBollingerBandsBean(String stockName, Date startDate, Date endDate) {
		return new BollingerBandsRequestBean(stockName, startDate, endDate, BOLLINGER_BANDS_PERIOD_DEFAULT, DATA_TYPE_DEFAULT,
				BOLLINGER_BANDS_MA_TYPE_DEFAULT, BOLLINGER_BANDS_DEVIATION_UP_DEFAULT, BOLLINGER_BANDS_DEVIATION_DOWN_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static BollingerBandsRequestBean instanceBollingerBandsBean(DataType dataType, String stockName, Date startDate, Date endDate) {
		return new BollingerBandsRequestBean(stockName, startDate, endDate, BOLLINGER_BANDS_PERIOD_DEFAULT, dataType,
				BOLLINGER_BANDS_MA_TYPE_DEFAULT, BOLLINGER_BANDS_DEVIATION_UP_DEFAULT, BOLLINGER_BANDS_DEVIATION_DOWN_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static BollingerBandsRequestBean instanceBollingerBandsBean(String stockName, Date startDate, Date endDate, int period, DataType dataType,
			BollingerBandsMAType bollingerBandsMAType, double devUp, double devDn) {
		return new BollingerBandsRequestBean(stockName, startDate, endDate, period, dataType, bollingerBandsMAType, devUp, devDn);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// ABSOLUTE PRICE OSCILLATOR
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static AbsolutePriceOscillatorRequestBean instanceAbsolutePriceOscillatorBean(String stockName, Date startDate, Date endDate) {
		return new AbsolutePriceOscillatorRequestBean(stockName, startDate, endDate, ABSOLUTE_PRICE_OSCILLATOR_SLOW_PERIOD_DEFAULT,
				ABSOLUTE_PRICE_OSCILLATOR_FAST_PERIOD_DEFAULT, ABSOLUTE_PRICE_OSCILLATOR_MA_TYPE_DEFAULT, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static AbsolutePriceOscillatorRequestBean instanceAbsolutePriceOscillatorBean(String stockName, Date startDate, Date endDate,
			int slowPeriod, int fastPeriod, AbsolutePriceOscillatorMAType maType, DataType dataType) {
		return new AbsolutePriceOscillatorRequestBean(stockName, startDate, endDate, slowPeriod, fastPeriod, maType, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// STOCHASTIC RSI
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static StochasticRSIRequestBean instanceStochasticRSIBean(String stockName, Date startDate, Date endDate) {
		return new StochasticRSIRequestBean(stockName, startDate, endDate, STOCHASTIC_RSI_TIME_PERIOD_DEFAULT, STOCHASTIC_RSI_FAST_K_PERIOD_DEFAULT,
				STOCHASTIC_RSI_FAST_D_PERIOD, STOCHASTIC_RSI_MA_TYPE_DEFAULT, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static StochasticRSIRequestBean instanceStochasticRSIBean(String stockName, Date startDate, Date endDate, DataType dataType) {
		return new StochasticRSIRequestBean(stockName, startDate, endDate, STOCHASTIC_RSI_TIME_PERIOD_DEFAULT, STOCHASTIC_RSI_FAST_K_PERIOD_DEFAULT,
				STOCHASTIC_RSI_FAST_D_PERIOD, STOCHASTIC_RSI_MA_TYPE_DEFAULT, dataType);
	}

	@TechnicalAnalysisMethod
	public static StochasticRSIRequestBean instanceStochasticRSIBean(String stockName, Date startDate, Date endDate, int timePeriod,
			int fastKPeriod, int fastDPeriod, StochasticRSIMAType maType, DataType dataType) {
		return new StochasticRSIRequestBean(stockName, startDate, endDate, timePeriod, fastKPeriod, fastDPeriod, maType, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// DOUBLE EXPONENTIAL MOVING AVERAGE
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static DoubleExponentialMovingAverageRequestBean instanceDoubleExponentialMovingAverageBean(String stockName,
			Date startDate, Date endDate) {
		return new DoubleExponentialMovingAverageRequestBean(stockName, startDate, endDate, DOUBLE_EXPONENTIAL_MOVING_AVERAGE_PERIOD_DEFAULT,
				DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static DoubleExponentialMovingAverageRequestBean instanceDoubleExponentialMovingAverageBean(DataType dataType, String stockName,
			Date startDate, Date endDate) {
		return new DoubleExponentialMovingAverageRequestBean(stockName, startDate, endDate, DOUBLE_EXPONENTIAL_MOVING_AVERAGE_PERIOD_DEFAULT, dataType);
	}

	@TechnicalAnalysisMethod
	public static DoubleExponentialMovingAverageRequestBean instanceDoubleExponentialMovingAverageBean(String stockName,
			Date startDate, Date endDate, int timePeriod, DataType dataType) {
		return new DoubleExponentialMovingAverageRequestBean(stockName, startDate, endDate, timePeriod, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// HILBERT TRANSFORM - DOMINANT CYCLE PERIOD
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static HilbertTransformDominantCyclePeriodRequestBean instanceHilbertTransformDominantCyclePeriodBean(
			String stockName, Date startDate, Date endDate) {
		return new HilbertTransformDominantCyclePeriodRequestBean(stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static HilbertTransformDominantCyclePeriodRequestBean instanceHilbertTransformDominantCyclePeriodBean(
			DataType dataType, String stockName, Date startDate, Date endDate) {
		return new HilbertTransformDominantCyclePeriodRequestBean(stockName, startDate, endDate, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// HILBERT TRANSFORM - DOMINANT CYCLE PHASE
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static HilbertTransformDominantCyclePhaseRequestBean instanceHilbertTransformDominantCyclePhaseBean(
			String stockName, Date startDate, Date endDate) {
		return new HilbertTransformDominantCyclePhaseRequestBean(stockName, startDate, endDate, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static HilbertTransformDominantCyclePhaseRequestBean instanceHilbertTransformDominantCyclePhaseBean(
			DataType dataType, String stockName, Date startDate, Date endDate) {
		return new HilbertTransformDominantCyclePhaseRequestBean(stockName, startDate, endDate, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// KAUFMAN ADAPTIVE MOVING AVERAGE
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static KaufmanAdaptiveMovingAverageRequestBean instanceKaufmanAdaptiveMovingAverageBean(
			String stockName, Date startDate, Date endDate) {
		return new KaufmanAdaptiveMovingAverageRequestBean(stockName, startDate, endDate,  KAUFMAN_ADAPTIVE_MOVING_AVERAGE_PERIOD_DEFAULT,
				DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static KaufmanAdaptiveMovingAverageRequestBean instanceKaufmanAdaptiveMovingAverageBean(
			DataType dataType, String stockName, Date startDate, Date endDate) {
		return new KaufmanAdaptiveMovingAverageRequestBean(stockName, startDate, endDate,  KAUFMAN_ADAPTIVE_MOVING_AVERAGE_PERIOD_DEFAULT,
				dataType);
	}

	@TechnicalAnalysisMethod
	public static KaufmanAdaptiveMovingAverageRequestBean instanceKaufmanAdaptiveMovingAverageBean(
			DataType dataType, String stockName, Date startDate, Date endDate, int timePeriod) {
		return new KaufmanAdaptiveMovingAverageRequestBean(stockName, startDate, endDate, timePeriod, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// LINEAR REGRESSION SLOPE
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static LinearRegressionSlopeRequestBean instanceLinearRegressionSlopeBean(String stockName, Date startDate,
			Date endDate) {
		return new LinearRegressionSlopeRequestBean(stockName, startDate, endDate, LINEAR_REGRESSION_SLOPE_PERIOD_DEFAULT, DATA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static LinearRegressionSlopeRequestBean instanceLinearRegressionSlopeBean(String stockName, Date startDate,
			Date endDate, int timePeriod, DataType dataType) {
		return new LinearRegressionSlopeRequestBean(stockName, startDate, endDate, timePeriod, dataType);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// STOCHASTIC
	//////////////////////////////////////////////////////////////////////////////////////

	@TechnicalAnalysisMethod
	public static StochasticRequestBean instanceStochasticBean(String stockName, Date startDate, Date endDate) {
		return new StochasticRequestBean(stockName, startDate, endDate, STOCHASTIC_FAST_K_PERIOD_DEFAULT,
				STOCHASTIC_FAST_D_PERIOD_DEFAULT, STOCHASTIC_SLOW_K_PERIOD_DEFAULT, STOCHASTIC_SLOW_K_MA_TYPE_DEFAULT,
				STOCHASTIC_SLOW_D_PERIOD_DEFAULT, STOCHASTIC_SLOW_D_MA_TYPE_DEFAULT);
	}

	@TechnicalAnalysisMethod
	public static StochasticRequestBean instanceStochasticBean(String stockName, Date startDate, Date endDate,
			int fastKPeriod, int fastDPeriod, int slowKPeriod, StochasticMAType slowKMAType, int slowDPeriod,
			StochasticMAType slowDMAType) {
		return new StochasticRequestBean(stockName, startDate, endDate, fastKPeriod, fastDPeriod, slowKPeriod, slowKMAType, slowDPeriod, slowDMAType);
	}

}
