package org.wtrader.cep.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum DataType {

	AVERAGE_PRICE("Averrage Price", 0),

	CLOSING_NEGOTIATION_PRICE("Closing Negotiation Price", 1),

	HIGHEST_NEGOTIATION_PRICE("Highest Negotiation Price", 2),

	LOWEST_NEGOTIATION_PRICE("Lowest Negotiation Price", 3),

	START_PRICE("Start Price", 4),

	VOLUME("Volume", 5);

	private String text;

	private int code;

	private static final Map<Integer, DataType> map = new HashMap<Integer, DataType>();

	static {
		for (DataType dataType : values()) {
			map.put(dataType.code, dataType);
		}
	}

	private DataType(String text, int code) {
		this.text = text;
		this.code = code;
	}

	@Override
	public String toString() {
		return this.text;
	}

	public static final DataType parse(int code) {
		return map.get(code);
	}

	public int getCode() {
		return this.code;
	}

}
