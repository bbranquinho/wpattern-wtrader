package org.wtrader.loader.utils;

import java.util.HashMap;
import java.util.Map;

public enum ApplicationType {

	CLIENT("client"),

	STANDALONE("standalone"),

	SERVER("server");

	private final String name;

	private final static Map<String, ApplicationType> map = new HashMap<String, ApplicationType>();

	static {
		for (ApplicationType value : values()) {
			map.put(value.name, value);
		}
	}

	private ApplicationType(String name) {
		this.name = name;
	}

	public static ApplicationType parse(String name) {
		ApplicationType value = null;

		if (name != null) {
			value = map.get(name.trim().toLowerCase());
		}

		if (value == null) {
			value = STANDALONE;
		}

		return value;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
