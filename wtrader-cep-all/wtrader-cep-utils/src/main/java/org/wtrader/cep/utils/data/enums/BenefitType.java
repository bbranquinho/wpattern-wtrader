package org.wtrader.cep.utils.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum BenefitType {

	BONIFICATION(0, "bonificacao"),

	FISSION(1, "cisao"),

	SPLIT(2, "desdobramento"),

	INPLIT(3, "grupamento"),

	RETURN_CAPITAL(4, "restituicao de capital");

	private final int code;

	private final String name;

	private static final Map<Integer, BenefitType> mapCode;

	private static final Map<String, BenefitType> mapName;

	static {
		mapName = new HashMap<String, BenefitType>();
		mapCode = new HashMap<Integer, BenefitType>();

		for (BenefitType value : values()) {
			mapCode.put(value.code, value);
			mapName.put(value.name, value);
		}
	}

	private BenefitType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public static BenefitType parser(String name) {
		return mapName.get(name);
	}

	public static BenefitType parser(Integer code) {
		return mapCode.get(code);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

}
