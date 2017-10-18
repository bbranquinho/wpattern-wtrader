package org.wtrader.cep.utils.ta.interfaces;

import org.wtrader.cep.utils.ta.MInteger;
import org.wtrader.cep.utils.ta.enums.CandleSettingType;
import org.wtrader.cep.utils.ta.enums.Compatibility;
import org.wtrader.cep.utils.ta.enums.FuncUnstId;
import org.wtrader.cep.utils.ta.enums.MAType;
import org.wtrader.cep.utils.ta.enums.RangeType;
import org.wtrader.cep.utils.ta.enums.RetCode;


public interface ITACore {

	public RetCode SetCandleSettings(CandleSettingType settingType,
			RangeType rangeType, int avgPeriod, double factor);

	public RetCode RestoreCandleDefaultSettings(
			CandleSettingType settingType);

	public RetCode SetUnstablePeriod(FuncUnstId id, int period);

	public int GetUnstablePeriod(FuncUnstId id);

	public void SetCompatibility(Compatibility compatibility);

	public Compatibility getCompatibility();

	/**** START GENCODE SECTION 1 - DO NOT DELETE THIS LINE ****/
	public int accbandsLookback(int optInTimePeriod);

	public RetCode accbands(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement,
			double outRealUpperBand[], double outRealMiddleBand[],
			double outRealLowerBand[]);

	public RetCode accbands(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement,
			double outRealUpperBand[], double outRealMiddleBand[],
			double outRealLowerBand[]);


	public int acosLookback();

	public RetCode acos(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode acos(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int adLookback();

	public RetCode ad(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], double inVolume[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode ad(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], float inVolume[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int addLookback();

	public RetCode add(int startIdx, int endIdx, double inReal0[],
			double inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode add(int startIdx, int endIdx, float inReal0[],
			float inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int adOscLookback(int optInFastPeriod, int optInSlowPeriod);

	public RetCode adOsc(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], double inVolume[],
			int optInFastPeriod, int optInSlowPeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode adOsc(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], float inVolume[],
			int optInFastPeriod, int optInSlowPeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int adxLookback(int optInTimePeriod);

	public RetCode adx(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode adx(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int adxrLookback(int optInTimePeriod);

	public RetCode adxr(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode adxr(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int apoLookback(int optInFastPeriod, int optInSlowPeriod,
			MAType optInMAType);

	public RetCode apo(int startIdx, int endIdx, double inReal[],
			int optInFastPeriod, int optInSlowPeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode apo(int startIdx, int endIdx, float inReal[],
			int optInFastPeriod, int optInSlowPeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int aroonLookback(int optInTimePeriod);

	public RetCode aroon(int startIdx, int endIdx, double inHigh[],
			double inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outAroonDown[], double outAroonUp[]);

	public RetCode aroon(int startIdx, int endIdx, float inHigh[],
			float inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outAroonDown[], double outAroonUp[]);


	public int aroonOscLookback(int optInTimePeriod);

	public RetCode aroonOsc(int startIdx, int endIdx, double inHigh[],
			double inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode aroonOsc(int startIdx, int endIdx, float inHigh[],
			float inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int asinLookback();

	public RetCode asin(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode asin(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int atanLookback();

	public RetCode atan(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode atan(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int atrLookback(int optInTimePeriod);

	public RetCode atr(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode atr(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int avgPriceLookback();

	public RetCode avgPrice(int startIdx, int endIdx, double inOpen[],
			double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode avgPrice(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int avgDevLookback(int optInTimePeriod);

	public RetCode avgDev(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode avgDev(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int bbandsLookback(int optInTimePeriod,
			double optInNbDevUp, double optInNbDevDn, MAType optInMAType);

	public RetCode bbands(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, double optInNbDevUp, double optInNbDevDn,
			MAType optInMAType, MInteger outBegIdx, MInteger outNBElement,
			double outRealUpperBand[], double outRealMiddleBand[],
			double outRealLowerBand[]);

	public RetCode bbands(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, double optInNbDevUp, double optInNbDevDn,
			MAType optInMAType, MInteger outBegIdx, MInteger outNBElement,
			double outRealUpperBand[], double outRealMiddleBand[],
			double outRealLowerBand[]);


	public int betaLookback(int optInTimePeriod);

	public RetCode beta(int startIdx, int endIdx, double inReal0[],
			double inReal1[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode beta(int startIdx, int endIdx, float inReal0[],
			float inReal1[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int bopLookback();

	public RetCode bop(int startIdx, int endIdx, double inOpen[],
			double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode bop(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int cciLookback(int optInTimePeriod);

	public RetCode cci(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode cci(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int cdl2CrowsLookback();

	public RetCode cdl2Crows(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl2Crows(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdl3BlackCrowsLookback();

	public RetCode cdl3BlackCrows(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3BlackCrows(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdl3InsideLookback();

	public RetCode cdl3Inside(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3Inside(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdl3LineStrikeLookback();

	public RetCode cdl3LineStrike(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3LineStrike(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdl3OutsideLookback();

	public RetCode cdl3Outside(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3Outside(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdl3StarsInSouthLookback();

	public RetCode cdl3StarsInSouth(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3StarsInSouth(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdl3WhiteSoldiersLookback();

	public RetCode cdl3WhiteSoldiers(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdl3WhiteSoldiers(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlAbandonedBabyLookback(double optInPenetration);

	public RetCode cdlAbandonedBaby(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlAbandonedBaby(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlAdvanceBlockLookback();

	public RetCode cdlAdvanceBlock(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlAdvanceBlock(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlBeltHoldLookback();

	public RetCode cdlBeltHold(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlBeltHold(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlBreakawayLookback();

	public RetCode cdlBreakaway(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlBreakaway(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlClosingMarubozuLookback();

	public RetCode cdlClosingMarubozu(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlClosingMarubozu(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlConcealBabysWallLookback();

	public RetCode cdlConcealBabysWall(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlConcealBabysWall(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlCounterAttackLookback();

	public RetCode cdlCounterAttack(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlCounterAttack(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlDarkCloudCoverLookback(double optInPenetration);

	public RetCode cdlDarkCloudCover(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlDarkCloudCover(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlDojiLookback();

	public RetCode cdlDoji(int startIdx, int endIdx, double inOpen[],
			double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlDoji(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlDojiStarLookback();

	public RetCode cdlDojiStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlDojiStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlDragonflyDojiLookback();

	public RetCode cdlDragonflyDoji(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlDragonflyDoji(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlEngulfingLookback();

	public RetCode cdlEngulfing(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlEngulfing(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlEveningDojiStarLookback(double optInPenetration);

	public RetCode cdlEveningDojiStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlEveningDojiStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlEveningStarLookback(double optInPenetration);

	public RetCode cdlEveningStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlEveningStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlGapSideSideWhiteLookback();

	public RetCode cdlGapSideSideWhite(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlGapSideSideWhite(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlGravestoneDojiLookback();

	public RetCode cdlGravestoneDoji(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlGravestoneDoji(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHammerLookback();

	public RetCode cdlHammer(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHammer(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlHangingManLookback();

	public RetCode cdlHangingMan(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHangingMan(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHaramiLookback();

	public RetCode cdlHarami(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHarami(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlHaramiCrossLookback();

	public RetCode cdlHaramiCross(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHaramiCross(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHignWaveLookback();

	public RetCode cdlHignWave(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHignWave(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHikkakeLookback();

	public RetCode cdlHikkake(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHikkake(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHikkakeModLookback();

	public RetCode cdlHikkakeMod(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHikkakeMod(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlHomingPigeonLookback();

	public RetCode cdlHomingPigeon(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlHomingPigeon(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlIdentical3CrowsLookback();

	public RetCode cdlIdentical3Crows(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlIdentical3Crows(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlInNeckLookback();

	public RetCode cdlInNeck(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlInNeck(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlInvertedHammerLookback();

	public RetCode cdlInvertedHammer(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlInvertedHammer(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlKickingLookback();

	public RetCode cdlKicking(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlKicking(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlKickingByLengthLookback();

	public RetCode cdlKickingByLength(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlKickingByLength(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlLadderBottomLookback();

	public RetCode cdlLadderBottom(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlLadderBottom(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlLongLeggedDojiLookback();

	public RetCode cdlLongLeggedDoji(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlLongLeggedDoji(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlLongLineLookback();

	public RetCode cdlLongLine(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlLongLine(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlMarubozuLookback();

	public RetCode cdlMarubozu(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlMarubozu(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlMatchingLowLookback();

	public RetCode cdlMatchingLow(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlMatchingLow(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlMatHoldLookback(double optInPenetration);

	public RetCode cdlMatHold(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlMatHold(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlMorningDojiStarLookback(double optInPenetration);

	public RetCode cdlMorningDojiStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlMorningDojiStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlMorningStarLookback(double optInPenetration);

	public RetCode cdlMorningStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode cdlMorningStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			double optInPenetration, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int cdlOnNeckLookback();

	public RetCode cdlOnNeck(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlOnNeck(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlPiercingLookback();

	public RetCode cdlPiercing(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlPiercing(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlRickshawManLookback();

	public RetCode cdlRickshawMan(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlRickshawMan(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlRiseFall3MethodsLookback();

	public RetCode cdlRiseFall3Methods(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlRiseFall3Methods(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlSeperatingLinesLookback();

	public RetCode cdlSeperatingLines(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlSeperatingLines(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlShootingStarLookback();

	public RetCode cdlShootingStar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlShootingStar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlShortLineLookback();

	public RetCode cdlShortLine(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlShortLine(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlSpinningTopLookback();

	public RetCode cdlSpinningTop(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlSpinningTop(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlStalledPatternLookback();

	public RetCode cdlStalledPattern(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlStalledPattern(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlStickSandwhichLookback();

	public RetCode cdlStickSandwhich(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlStickSandwhich(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlTakuriLookback();

	public RetCode cdlTakuri(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlTakuri(int startIdx, int endIdx, float inOpen[],
			float inHigh[], float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, int outInteger[]);


	public int cdlTasukiGapLookback();

	public RetCode cdlTasukiGap(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlTasukiGap(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlThrustingLookback();

	public RetCode cdlThrusting(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlThrusting(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlTristarLookback();

	public RetCode cdlTristar(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlTristar(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlUnique3RiverLookback();

	public RetCode cdlUnique3River(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlUnique3River(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlUpsideGap2CrowsLookback();

	public RetCode cdlUpsideGap2Crows(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlUpsideGap2Crows(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int cdlXSideGap3MethodsLookback();

	public RetCode cdlXSideGap3Methods(int startIdx, int endIdx,
			double inOpen[], double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);

	public RetCode cdlXSideGap3Methods(int startIdx, int endIdx,
			float inOpen[], float inHigh[], float inLow[], float inClose[],
			MInteger outBegIdx, MInteger outNBElement, int outInteger[]);


	public int ceilLookback();

	public RetCode ceil(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode ceil(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int cmoLookback(int optInTimePeriod);

	public RetCode cmo(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode cmo(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int correlLookback(int optInTimePeriod);

	public RetCode correl(int startIdx, int endIdx, double inReal0[],
			double inReal1[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode correl(int startIdx, int endIdx, float inReal0[],
			float inReal1[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int cosLookback();

	public RetCode cos(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode cos(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int coshLookback();

	public RetCode cosh(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode cosh(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int demaLookback(int optInTimePeriod);

	public RetCode dema(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode dema(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int divLookback();

	public RetCode div(int startIdx, int endIdx, double inReal0[],
			double inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode div(int startIdx, int endIdx, float inReal0[],
			float inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int dxLookback(int optInTimePeriod);

	public RetCode dx(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode dx(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int emaLookback(int optInTimePeriod);

	public RetCode ema(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode TA_INT_EMA(int startIdx, int endIdx,
			double[] inReal, int optInTimePeriod, double optInK_1,
			MInteger outBegIdx, MInteger outNBElement, double[] outReal);

	public RetCode ema(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode TA_INT_EMA(int startIdx, int endIdx,
			float[] inReal, int optInTimePeriod, double optInK_1,
			MInteger outBegIdx, MInteger outNBElement, double[] outReal);


	public int expLookback();

	public RetCode exp(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode exp(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int floorLookback();

	public RetCode floor(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode floor(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int htDcPeriodLookback();

	public RetCode htDcPeriod(int startIdx, int endIdx,
			double inReal[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode htDcPeriod(int startIdx, int endIdx,
			float inReal[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int htDcPhaseLookback();

	public RetCode htDcPhase(int startIdx, int endIdx,
			double inReal[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode htDcPhase(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int htPhasorLookback();

	public RetCode htPhasor(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outInPhase[],
			double outQuadrature[]);

	public RetCode htPhasor(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outInPhase[],
			double outQuadrature[]);


	public int htSineLookback();

	public RetCode htSine(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outSine[],
			double outLeadSine[]);

	public RetCode htSine(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outSine[],
			double outLeadSine[]);


	public int htTrendlineLookback();

	public RetCode htTrendline(int startIdx, int endIdx,
			double inReal[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode htTrendline(int startIdx, int endIdx,
			float inReal[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int htTrendModeLookback();

	public RetCode htTrendMode(int startIdx, int endIdx,
			double inReal[], MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode htTrendMode(int startIdx, int endIdx,
			float inReal[], MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int imiLookback(int optInTimePeriod);

	public RetCode imi(int startIdx, int endIdx, double inOpen[],
			double inClose[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode imi(int startIdx, int endIdx, float inOpen[],
			float inClose[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int kamaLookback(int optInTimePeriod);

	public RetCode kama(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode kama(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int linearRegLookback(int optInTimePeriod);

	public RetCode linearReg(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode linearReg(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int linearRegAngleLookback(int optInTimePeriod);

	public RetCode linearRegAngle(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode linearRegAngle(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int linearRegInterceptLookback(int optInTimePeriod);

	public RetCode linearRegIntercept(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode linearRegIntercept(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int linearRegSlopeLookback(int optInTimePeriod);

	public RetCode linearRegSlope(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode linearRegSlope(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int lnLookback();

	public RetCode ln(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode ln(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int log10Lookback();

	public RetCode log10(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode log10(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int movingAverageLookback(int optInTimePeriod,
			MAType optInMAType);

	public RetCode movingAverage(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode movingAverage(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int macdLookback(int optInFastPeriod, int optInSlowPeriod,
			int optInSignalPeriod);

	public RetCode macd(int startIdx, int endIdx, double inReal[],
			int optInFastPeriod, int optInSlowPeriod, int optInSignalPeriod,
			MInteger outBegIdx, MInteger outNBElement, double outMACD[],
			double outMACDSignal[], double outMACDHist[]);

	public RetCode macd(int startIdx, int endIdx, float inReal[],
			int optInFastPeriod, int optInSlowPeriod, int optInSignalPeriod,
			MInteger outBegIdx, MInteger outNBElement, double outMACD[],
			double outMACDSignal[], double outMACDHist[]);


	public int macdExtLookback(int optInFastPeriod,
			MAType optInFastMAType, int optInSlowPeriod,
			MAType optInSlowMAType, int optInSignalPeriod,
			MAType optInSignalMAType);

	public RetCode macdExt(int startIdx, int endIdx, double inReal[],
			int optInFastPeriod, MAType optInFastMAType, int optInSlowPeriod,
			MAType optInSlowMAType, int optInSignalPeriod,
			MAType optInSignalMAType, MInteger outBegIdx,
			MInteger outNBElement, double outMACD[], double outMACDSignal[],
			double outMACDHist[]);

	public RetCode macdExt(int startIdx, int endIdx, float inReal[],
			int optInFastPeriod, MAType optInFastMAType, int optInSlowPeriod,
			MAType optInSlowMAType, int optInSignalPeriod,
			MAType optInSignalMAType, MInteger outBegIdx,
			MInteger outNBElement, double outMACD[], double outMACDSignal[],
			double outMACDHist[]);


	public int macdFixLookback(int optInSignalPeriod);

	public RetCode macdFix(int startIdx, int endIdx, double inReal[],
			int optInSignalPeriod, MInteger outBegIdx, MInteger outNBElement,
			double outMACD[], double outMACDSignal[], double outMACDHist[]);

	public RetCode macdFix(int startIdx, int endIdx, float inReal[],
			int optInSignalPeriod, MInteger outBegIdx, MInteger outNBElement,
			double outMACD[], double outMACDSignal[], double outMACDHist[]);


	public int mamaLookback(double optInFastLimit,
			double optInSlowLimit);

	public RetCode mama(int startIdx, int endIdx, double inReal[],
			double optInFastLimit, double optInSlowLimit, MInteger outBegIdx,
			MInteger outNBElement, double outMAMA[], double outFAMA[]);

	public RetCode mama(int startIdx, int endIdx, float inReal[],
			double optInFastLimit, double optInSlowLimit, MInteger outBegIdx,
			MInteger outNBElement, double outMAMA[], double outFAMA[]);


	public int movingAverageVariablePeriodLookback(int optInMinPeriod,
			int optInMaxPeriod, MAType optInMAType);

	public RetCode movingAverageVariablePeriod(int startIdx,
			int endIdx, double inReal[], double inPeriods[],
			int optInMinPeriod, int optInMaxPeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode movingAverageVariablePeriod(int startIdx,
			int endIdx, float inReal[], float inPeriods[], int optInMinPeriod,
			int optInMaxPeriod, MAType optInMAType, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int maxLookback(int optInTimePeriod);

	public RetCode max(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode max(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int maxIndexLookback(int optInTimePeriod);

	public RetCode maxIndex(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode maxIndex(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int medPriceLookback();

	public RetCode medPrice(int startIdx, int endIdx, double inHigh[],
			double inLow[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode medPrice(int startIdx, int endIdx, float inHigh[],
			float inLow[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int mfiLookback(int optInTimePeriod);

	public RetCode mfi(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], double inVolume[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode mfi(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], float inVolume[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int midPointLookback(int optInTimePeriod);

	public RetCode midPoint(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode midPoint(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int midPriceLookback(int optInTimePeriod);

	public RetCode midPrice(int startIdx, int endIdx, double inHigh[],
			double inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode midPrice(int startIdx, int endIdx, float inHigh[],
			float inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int minLookback(int optInTimePeriod);

	public RetCode min(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode min(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int minIndexLookback(int optInTimePeriod);

	public RetCode minIndex(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);

	public RetCode minIndex(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			int outInteger[]);


	public int minMaxLookback(int optInTimePeriod);

	public RetCode minMax(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outMin[], double outMax[]);

	public RetCode minMax(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outMin[], double outMax[]);


	public int minMaxIndexLookback(int optInTimePeriod);

	public RetCode minMaxIndex(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, int outMinIdx[], int outMaxIdx[]);

	public RetCode minMaxIndex(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, int outMinIdx[], int outMaxIdx[]);


	public int minusDILookback(int optInTimePeriod);

	public RetCode minusDI(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode minusDI(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int minusDMLookback(int optInTimePeriod);

	public RetCode minusDM(int startIdx, int endIdx, double inHigh[],
			double inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode minusDM(int startIdx, int endIdx, float inHigh[],
			float inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int momLookback(int optInTimePeriod);

	public RetCode mom(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode mom(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int multLookback();

	public RetCode mult(int startIdx, int endIdx, double inReal0[],
			double inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode mult(int startIdx, int endIdx, float inReal0[],
			float inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int natrLookback(int optInTimePeriod);

	public RetCode natr(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode natr(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int obvLookback();

	public RetCode obv(int startIdx, int endIdx, double inReal[],
			double inVolume[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode obv(int startIdx, int endIdx, float inReal[],
			float inVolume[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int plusDILookback(int optInTimePeriod);

	public RetCode plusDI(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode plusDI(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int plusDMLookback(int optInTimePeriod);

	public RetCode plusDM(int startIdx, int endIdx, double inHigh[],
			double inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode plusDM(int startIdx, int endIdx, float inHigh[],
			float inLow[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int ppoLookback(int optInFastPeriod, int optInSlowPeriod,
			MAType optInMAType);

	public RetCode ppo(int startIdx, int endIdx, double inReal[],
			int optInFastPeriod, int optInSlowPeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode ppo(int startIdx, int endIdx, float inReal[],
			int optInFastPeriod, int optInSlowPeriod, MAType optInMAType,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int rocLookback(int optInTimePeriod);

	public RetCode roc(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode roc(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int rocPLookback(int optInTimePeriod);

	public RetCode rocP(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode rocP(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int rocRLookback(int optInTimePeriod);

	public RetCode rocR(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode rocR(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int rocR100Lookback(int optInTimePeriod);

	public RetCode rocR100(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode rocR100(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int rsiLookback(int optInTimePeriod);

	public RetCode rsi(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode rsi(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int sarLookback(double optInAcceleration,
			double optInMaximum);

	public RetCode sar(int startIdx, int endIdx, double inHigh[],
			double inLow[], double optInAcceleration, double optInMaximum,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode sar(int startIdx, int endIdx, float inHigh[],
			float inLow[], double optInAcceleration, double optInMaximum,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int sarExtLookback(double optInStartValue,
			double optInOffsetOnReverse, double optInAccelerationInitLong,
			double optInAccelerationLong, double optInAccelerationMaxLong,
			double optInAccelerationInitShort, double optInAccelerationShort,
			double optInAccelerationMaxShort);

	public RetCode sarExt(int startIdx, int endIdx, double inHigh[],
			double inLow[], double optInStartValue,
			double optInOffsetOnReverse, double optInAccelerationInitLong,
			double optInAccelerationLong, double optInAccelerationMaxLong,
			double optInAccelerationInitShort, double optInAccelerationShort,
			double optInAccelerationMaxShort, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode sarExt(int startIdx, int endIdx, float inHigh[],
			float inLow[], double optInStartValue, double optInOffsetOnReverse,
			double optInAccelerationInitLong, double optInAccelerationLong,
			double optInAccelerationMaxLong, double optInAccelerationInitShort,
			double optInAccelerationShort, double optInAccelerationMaxShort,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int sinLookback();

	public RetCode sin(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode sin(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int sinhLookback();

	public RetCode sinh(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode sinh(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int smaLookback(int optInTimePeriod);

	public RetCode sma(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode sma(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int sqrtLookback();

	public RetCode sqrt(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode sqrt(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int stdDevLookback(int optInTimePeriod, double optInNbDev);

	public RetCode stdDev(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, double optInNbDev, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode stdDev(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, double optInNbDev, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int stochLookback(int optInFastK_Period,
			int optInSlowK_Period, MAType optInSlowK_MAType,
			int optInSlowD_Period, MAType optInSlowD_MAType);

	public RetCode stoch(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInFastK_Period,
			int optInSlowK_Period, MAType optInSlowK_MAType,
			int optInSlowD_Period, MAType optInSlowD_MAType,
			MInteger outBegIdx, MInteger outNBElement, double outSlowK[],
			double outSlowD[]);

	public RetCode stoch(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInFastK_Period,
			int optInSlowK_Period, MAType optInSlowK_MAType,
			int optInSlowD_Period, MAType optInSlowD_MAType,
			MInteger outBegIdx, MInteger outNBElement, double outSlowK[],
			double outSlowD[]);


	public int stochFLookback(int optInFastK_Period,
			int optInFastD_Period, MAType optInFastD_MAType);

	public RetCode stochF(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInFastK_Period,
			int optInFastD_Period, MAType optInFastD_MAType,
			MInteger outBegIdx, MInteger outNBElement, double outFastK[],
			double outFastD[]);

	public RetCode stochF(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInFastK_Period,
			int optInFastD_Period, MAType optInFastD_MAType,
			MInteger outBegIdx, MInteger outNBElement, double outFastK[],
			double outFastD[]);


	public int stochRsiLookback(int optInTimePeriod,
			int optInFastK_Period, int optInFastD_Period,
			MAType optInFastD_MAType);

	public RetCode stochRsi(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, int optInFastK_Period, int optInFastD_Period,
			MAType optInFastD_MAType, MInteger outBegIdx,
			MInteger outNBElement, double outFastK[], double outFastD[]);

	public RetCode stochRsi(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, int optInFastK_Period, int optInFastD_Period,
			MAType optInFastD_MAType, MInteger outBegIdx,
			MInteger outNBElement, double outFastK[], double outFastD[]);


	public int subLookback();

	public RetCode sub(int startIdx, int endIdx, double inReal0[],
			double inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode sub(int startIdx, int endIdx, float inReal0[],
			float inReal1[], MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int sumLookback(int optInTimePeriod);

	public RetCode sum(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode sum(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int t3Lookback(int optInTimePeriod, double optInVFactor);

	public RetCode t3(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, double optInVFactor, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode t3(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, double optInVFactor, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int tanLookback();

	public RetCode tan(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode tan(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int tanhLookback();

	public RetCode tanh(int startIdx, int endIdx, double inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode tanh(int startIdx, int endIdx, float inReal[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int temaLookback(int optInTimePeriod);

	public RetCode tema(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode tema(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int trueRangeLookback();

	public RetCode trueRange(int startIdx, int endIdx,
			double inHigh[], double inLow[], double inClose[],
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode trueRange(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int trimaLookback(int optInTimePeriod);

	public RetCode trima(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode trima(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int trixLookback(int optInTimePeriod);

	public RetCode trix(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode trix(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int tsfLookback(int optInTimePeriod);

	public RetCode tsf(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode tsf(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);


	public int typPriceLookback();

	public RetCode typPrice(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode typPrice(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int ultOscLookback(int optInTimePeriod1,
			int optInTimePeriod2, int optInTimePeriod3);

	public RetCode ultOsc(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod1,
			int optInTimePeriod2, int optInTimePeriod3, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode ultOsc(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod1,
			int optInTimePeriod2, int optInTimePeriod3, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int varianceLookback(int optInTimePeriod, double optInNbDev);

	public RetCode variance(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, double optInNbDev, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode TA_INT_VAR(int startIdx, int endIdx,
			double inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode variance(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, double optInNbDev, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode TA_INT_VAR(int startIdx, int endIdx,
			float inReal[], int optInTimePeriod, MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int wclPriceLookback();

	public RetCode wclPrice(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);

	public RetCode wclPrice(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], MInteger outBegIdx,
			MInteger outNBElement, double outReal[]);


	public int willRLookback(int optInTimePeriod);

	public RetCode willR(int startIdx, int endIdx, double inHigh[],
			double inLow[], double inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);

	public RetCode willR(int startIdx, int endIdx, float inHigh[],
			float inLow[], float inClose[], int optInTimePeriod,
			MInteger outBegIdx, MInteger outNBElement, double outReal[]);


	public int wmaLookback(int optInTimePeriod);

	public RetCode wma(int startIdx, int endIdx, double inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	public RetCode wma(int startIdx, int endIdx, float inReal[],
			int optInTimePeriod, MInteger outBegIdx, MInteger outNBElement,
			double outReal[]);

	/**** END GENCODE SECTION 1 - DO NOT DELETE THIS LINE ****/

}