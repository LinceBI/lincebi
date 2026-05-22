package com.stratebi.lincebi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class EnvUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvUtils.class);

	public static long parseLongEnv(String name, long defaultValue) {
		String value = System.getenv(name);
		if (value == null || value.isEmpty()) return defaultValue;
		try {
			long parsed = Long.parseLong(value.trim());
			if (parsed < 0) throw new NumberFormatException("negative");
			return parsed;
		} catch (NumberFormatException ex) {
			EnvUtils.LOGGER.error("Invalid {} '{}', using default {}", name, value, defaultValue);
			return defaultValue;
		}
	}

	public static Duration parseTtlSecondsEnv(String name, Duration defaultValue) {
		return Duration.ofSeconds(EnvUtils.parseLongEnv(name, defaultValue.getSeconds()));
	}

}
