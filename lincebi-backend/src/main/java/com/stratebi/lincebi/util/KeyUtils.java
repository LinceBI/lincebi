package com.stratebi.lincebi.util;

import java.util.TreeSet;

public class KeyUtils {

	public static String getKeyName(Object... objs) {
		TreeSet<String> keys = new TreeSet<>();

		for (Object obj : objs) {
			if (obj instanceof Iterable<?>) {
				((Iterable<?>) obj).forEach(e -> keys.add(e.toString()));
			} else if (obj != null) {
				keys.add(obj.toString());
			}
		}

		return String.join("|", keys);
	}

}
