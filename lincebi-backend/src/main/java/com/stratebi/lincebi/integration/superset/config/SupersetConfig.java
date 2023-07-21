package com.stratebi.lincebi.integration.superset.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration class
 */
public class SupersetConfig {

	private static final Map<String, SupersetConfig> CONFIGS = new HashMap<>();

	public String url = "";
	public String externalUrl = "";
	public String provider = "";
	public String username = "";
	public String password = "";
	public String guestUsername = "";
	public String guestFirstname = "";
	public String guestLastname = "";
	public int guestTokenExpSec = 300;

	static {
		Map<String, String> env = System.getenv();

		for (Map.Entry<String, String> var : env.entrySet()) {
			String varKey = var.getKey();
			Pattern varKeyPattern = Pattern.compile("^SUPERSET_([A-Z0-9_]+)_PROVIDER$");
			Matcher varKeyMatcher = varKeyPattern.matcher(varKey);

			if (varKeyMatcher.find() && varKeyMatcher.groupCount() > 0) {
				String name = varKeyMatcher.group(1);
				SupersetConfig config = new SupersetConfig();

				String url = env.get("SUPERSET_" + name + "_URL");
				if (url != null) config.url = url;

				String externalUrl = env.get("SUPERSET_" + name + "_EXTERNAL_URL");
				if (externalUrl != null) config.externalUrl = externalUrl;
				else config.externalUrl = config.url;

				String provider = env.get("SUPERSET_" + name + "_PROVIDER");
				if (provider != null) config.provider = provider;

				String username = env.get("SUPERSET_" + name + "_USERNAME");
				if (username != null) config.username = username;

				String password = env.get("SUPERSET_" + name + "_PASSWORD");
				if (password != null) config.password = password;

				String guestUsername = env.get("SUPERSET_" + name + "_GUEST_USERNAME");
				if (guestUsername != null) config.guestUsername = guestUsername;

				String guestFirstname = env.get("SUPERSET_" + name + "_GUEST_FIRSTNAME");
				if (guestFirstname != null) config.guestFirstname = guestFirstname;

				String guestLastname = env.get("SUPERSET_" + name + "_GUEST_LASTNAME");
				if (guestLastname != null) config.guestLastname = guestLastname;

				String guestTokenExpSec = env.get("SUPERSET_" + name + "_GUEST_TOKEN_EXP_SEC");
				if (guestTokenExpSec != null) config.guestTokenExpSec = Integer.parseInt(guestTokenExpSec);

				SupersetConfig.put(name.toLowerCase(), config);
			}
		}
	}

	public static SupersetConfig put(String name, SupersetConfig value) {
		return SupersetConfig.CONFIGS.put(name, value);
	}

	public static SupersetConfig get(String name) {
		return SupersetConfig.CONFIGS.get(name);
	}

	public static SupersetConfig remove(String name) {
		return SupersetConfig.CONFIGS.remove(name);
	}

}
