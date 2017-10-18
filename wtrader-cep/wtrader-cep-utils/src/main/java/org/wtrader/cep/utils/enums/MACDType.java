package org.wtrader.cep.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum MACDType {

	//SMA("Simple Moving Average", 0),

	EMA("Exponential Moving Average", 1);

	private String text;

	private int code;

	private static final Map<Integer, MACDType> map = new HashMap<Integer, MACDType>();

	static {
		for (MACDType macdType : values()) {
			map.put(macdType.code, macdType);
		}
	}

	private MACDType(String text, int code) {
		this.text = text;
		this.code = code;
	}

	@Override
	public String toString() {
		return this.text;
	}

	public static final MACDType parse(int code) {
		return map.get(code);
	}

	public int getCode() {
		return this.code;
	}

}
