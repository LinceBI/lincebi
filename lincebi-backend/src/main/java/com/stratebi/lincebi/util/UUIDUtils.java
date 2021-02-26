package com.stratebi.lincebi.util;

import java.util.regex.Pattern;

public class UUIDUtils {

	private static final Pattern UUID_PATTERN = Pattern.compile("^[a-f0-9]{8}-?[a-f0-9]{4}-?[a-f0-9]{4}-?[a-f0-9]{4}-?[a-f0-9]{12}$", Pattern.CASE_INSENSITIVE);

	public static boolean isUUID(String str) {
		return (str != null) && UUIDUtils.UUID_PATTERN.matcher(str).matches();
	}

	public static boolean isUUID(Iterable<String> strs) {
		for (String str : strs) if (!UUIDUtils.isUUID(str)) return false;
		return true;
	}

	public static boolean isGUID(String str) {
		return UUIDUtils.isUUID(str);
	}

	public static boolean isGUID(Iterable<String> strs) {
		return UUIDUtils.isUUID(strs);
	}

}
