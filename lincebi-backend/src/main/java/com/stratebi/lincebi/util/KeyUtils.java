package com.stratebi.lincebi.util;

import java.util.TreeSet;

public class KeyUtils {

	private static final String SEGMENT_DELIMITER = "\u001F";
	private static final String ITERABLE_DELIMITER = "\u001E";
	private static final String NULL_MARKER = "\u0000";

	public static String getKeyName(Object... objs) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < objs.length; i++) {
			if (i > 0) sb.append(KeyUtils.SEGMENT_DELIMITER);
			Object obj = objs[i];

			if (obj == null) {
				sb.append(KeyUtils.NULL_MARKER);
			} else if (obj instanceof Iterable<?>) {
				TreeSet<String> parts = new TreeSet<>();
				for (Object e : (Iterable<?>) obj) {
					parts.add(e == null ? KeyUtils.NULL_MARKER : e.toString());
				}
				sb.append(String.join(KeyUtils.ITERABLE_DELIMITER, parts));
			} else {
				sb.append(obj.toString());
			}
		}

		return sb.toString();
	}

}
