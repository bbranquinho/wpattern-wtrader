package org.wtrader.loader.data.enums;

public enum ParserPositionEnum {

	DATE(2, 10),

	BDI(10, 12),

	STOCK_NAME(12, 24),

	START_PRICE(56, 69),

	HIGHEST_NEGOTIATION_PRICE(69, 82),

	LOWEST_NEGOTIATION_PRICE(82, 95),

	AVERAGE_NEGOTIATION_PRICE(95, 108),

	CLOSING_NEGOTIATION_PRICE(108, 121),

	HIGHEST_BUY_OFFER_PRICE(121, 134),

	LOWEST_SELL_OFFER_PRICE(134, 147),

	TOTAL_BUSINESS(147, 152),

	TOTAL_NEGOTIATION(152, 170),

	TOTAL_VOLUME(170, 188);

	private final int startPosition;

	private final int endPosition;

	private ParserPositionEnum(int startPosition, int endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	public int getStartPosition() {
		return this.startPosition;
	}

	public int getEndPosition() {
		return this.endPosition;
	}

}
