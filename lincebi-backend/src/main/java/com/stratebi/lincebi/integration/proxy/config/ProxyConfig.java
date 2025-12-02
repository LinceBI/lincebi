package com.stratebi.lincebi.integration.proxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ProxyConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConfig.class);

	public static String secret;
	public static String userPattern;
	public static String preferredUsernamePattern;
	public static String emailPattern;
	public static String groupsDelimiter;
	public static List<Pattern> allowedOrigins = new ArrayList<>();

	static {
		String secret = System.getenv("PROXY_SECRET");
		ProxyConfig.secret = secret != null ? secret : "";

		String userPattern = System.getenv("PROXY_USER_PATTERN");
		ProxyConfig.userPattern = userPattern != null && !userPattern.isEmpty() ? userPattern : "{username}";

		String preferredUsernamePattern = System.getenv("PROXY_PREFERRED_USERNAME_PATTERN");
		ProxyConfig.preferredUsernamePattern = preferredUsernamePattern != null && !preferredUsernamePattern.isEmpty() ? preferredUsernamePattern : "{username}";

		String emailPattern = System.getenv("PROXY_EMAIL_PATTERN");
		ProxyConfig.emailPattern = emailPattern != null && !emailPattern.isEmpty() ? emailPattern : "{username}@lincebi.internal";

		String groupsDelimiter = System.getenv("PROXY_GROUPS_DELIMITER");
		ProxyConfig.groupsDelimiter = groupsDelimiter != null && !groupsDelimiter.isEmpty() ? groupsDelimiter : ",";

		String allowedOrigins = System.getenv("PROXY_ALLOWED_ORIGINS");
		String[] patterns = allowedOrigins != null && !allowedOrigins.isEmpty() ? allowedOrigins.split("\\s*,\\s*") : new String[]{};
		for (String pattern : patterns) {
			try {
				ProxyConfig.allowedOrigins.add(Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE));
			} catch (PatternSyntaxException e) {
				ProxyConfig.LOGGER.error("Invalid regex '{}': {}", pattern, e.getMessage());
			}
		}
	}

}
