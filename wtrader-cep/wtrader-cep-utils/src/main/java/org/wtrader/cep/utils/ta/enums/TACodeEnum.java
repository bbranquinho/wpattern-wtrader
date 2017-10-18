package org.wtrader.cep.utils.ta.enums;

import java.util.HashMap;
import java.util.Map;

public enum TACodeEnum {

	SMA (0),

	EMA (1),

	MACD (2),

	RSI (3);

	private final int code;

	private static Map<Integer, TACodeEnum> codes;

	static {
		codes = new HashMap<Integer, TACodeEnum>();

		for (TACodeEnum value : values()) {
			codes.put(value.code, value);
		}
	}

	private TACodeEnum(int code) {
		this.code = code;
	}

	public int code() {
		return this.code;
	}

	public static TACodeEnum parser(int code) {
		return codes.get(code);
	}

}
