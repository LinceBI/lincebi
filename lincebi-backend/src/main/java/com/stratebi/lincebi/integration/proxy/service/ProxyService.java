package com.stratebi.lincebi.integration.proxy.service;

import com.stratebi.lincebi.integration.common.service.BIServerService;
import com.stratebi.lincebi.integration.proxy.config.ProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

public class ProxyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class);

	private static final Set<String> HTML_CONTENT_TYPES = Set.of("text/html", "application/xhtml+xml");
	private static final Set<String> CSS_CONTENT_TYPES = Set.of("text/css");

	private static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*[\"']?([^\"'\\s;]+)[\"']?", Pattern.CASE_INSENSITIVE);
	private static final Pattern HEAD_PATTERN = Pattern.compile("(<head[^>]*>)", Pattern.CASE_INSENSITIVE);
	private static final Pattern CSP_META_PATTERN = Pattern.compile("<meta\\s+[^>]*http-equiv\\s*=\\s*[\"']?Content-Security-Policy[\"']?[^>]*>", Pattern.CASE_INSENSITIVE);
	private static final Pattern BASE_HREF_PATTERN = Pattern.compile("(<base\\s+[^>]*href\\s*=\\s*[\"'])(/[^\"']*)([\"'][^>]*>)", Pattern.CASE_INSENSITIVE);
	private static final Pattern HTML_ATTR_PATTERN = Pattern.compile("((?:href|src|action|data-src|poster)\\s*=\\s*[\"'])(/(?!/)[^\"']+)([\"'])", Pattern.CASE_INSENSITIVE);
	private static final Pattern CSS_URL_PATTERN = Pattern.compile("(url\\s*\\(\\s*[\"']?)(/(?!/)[^\"')]+)([\"']?\\s*\\))", Pattern.CASE_INSENSITIVE);

	private static final Set<String> ALLOWED_RESPONSE_HEADERS = Set.of(
		"content-type", "content-language", "content-disposition",
		"content-range", "accept-ranges",
		"cache-control", "expires", "pragma", "last-modified", "etag",
		"location", "set-cookie", "vary", "date",
		"retry-after", "www-authenticate",
		"x-content-type-options", "referrer-policy",
		"accept-ch", "critical-ch",
		"access-control-allow-origin", "access-control-allow-credentials",
		"access-control-expose-headers", "access-control-allow-methods",
		"access-control-allow-headers", "access-control-max-age"
	);

	private static final Set<String> ALLOWED_REQUEST_HEADERS = Set.of(
		"accept", "accept-language",
		"content-type", "content-length",
		"cache-control", "pragma", "if-modified-since", "if-none-match", "if-range",
		"range", "authorization", "cookie", "user-agent",
		"origin", "referer", "x-requested-with",
		"sec-fetch-dest", "sec-fetch-mode", "sec-fetch-site", "sec-fetch-user",
		"sec-ch-ua", "sec-ch-ua-mobile", "sec-ch-ua-platform",
		"sec-ch-ua-platform-version", "sec-ch-ua-arch", "sec-ch-ua-model",
		"sec-ch-ua-full-version-list", "sec-ch-ua-bitness", "sec-ch-ua-wow64",
		"sec-ch-prefers-color-scheme", "sec-ch-prefers-reduced-motion",
		"sec-ch-prefers-reduced-transparency", "sec-ch-viewport-width",
		"sec-ch-viewport-height", "sec-ch-dpr", "sec-ch-device-memory"
	);

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
		.connectTimeout(Duration.ofSeconds(30))
		.followRedirects(HttpClient.Redirect.NEVER)
		.build();

	private static final Map<String, List<String>> ERROR_HEADERS;
	static {
		Map<String, List<String>> h = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		h.put("Content-Type", Collections.singletonList("text/plain; charset=UTF-8"));
		ERROR_HEADERS = Collections.unmodifiableMap(h);
	}

	public static ProxyResponse fetchUrl(String proxyPrefix, String reqMethod, String reqUrl, Map<String, List<String>> reqHeaders, byte[] reqBody) {
		ProxyService.LOGGER.debug("Fetching URL [{}]: {}", reqMethod, reqUrl);

		try {
			reqHeaders.keySet().removeIf(key -> !ALLOWED_REQUEST_HEADERS.contains(key.toLowerCase()));

			List<String> reqCookieHeaders = reqHeaders.get("Cookie");
			if (reqCookieHeaders != null && !reqCookieHeaders.isEmpty()) {
				String reqCookie = reqCookieHeaders.get(0).replaceAll("(?i)\\s*;?\\s*JSESSIONID\\s*=\\s*[^;]*", "").trim();
				if (!reqCookie.isEmpty()) reqHeaders.put("Cookie", Collections.singletonList(reqCookie));
				else reqHeaders.remove("Cookie");
			}

			String username = BIServerService.getUser();
			String sessionHash = BIServerService.getSessionHash();
			List<String> roles = BIServerService.getRoles();

			if (!ProxyConfig.secret.isEmpty()) {
				reqHeaders.put("X-Proxy-Secret", Collections.singletonList(ProxyConfig.secret));
			}

			reqHeaders.put("X-Forwarded-User", Collections.singletonList(ProxyConfig.userPattern
				.replace("{username}", username)
				.replace("{session_hash}", sessionHash)));
			reqHeaders.put("X-Forwarded-Preferred-Username", Collections.singletonList(ProxyConfig.preferredUsernamePattern
				.replace("{username}", username)
				.replace("{session_hash}", sessionHash)));
			reqHeaders.put("X-Forwarded-Email", Collections.singletonList(ProxyConfig.emailPattern
				.replace("{username}", username)
				.replace("{session_hash}", sessionHash)));
			reqHeaders.put("X-Forwarded-Groups", Collections.singletonList(roles.stream()
				.map(role -> role.replace(ProxyConfig.groupsDelimiter, ""))
				.collect(java.util.stream.Collectors.joining(ProxyConfig.groupsDelimiter))));

			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(new URI(reqUrl))
				.timeout(Duration.ofSeconds(60));

			reqHeaders.forEach((key, values) -> {
				if (values != null && !values.isEmpty()) {
					String lowerKey = key.toLowerCase();
					if (!lowerKey.equals("host") && !lowerKey.equals("content-length")) {
						requestBuilder.header(key, values.get(0));
					}
				}
			});

			HttpRequest.BodyPublisher bodyPublisher = reqBody != null && reqBody.length > 0
				? HttpRequest.BodyPublishers.ofByteArray(reqBody)
				: HttpRequest.BodyPublishers.noBody();

			requestBuilder.method(reqMethod, bodyPublisher);

			HttpRequest request = requestBuilder.build();

			HttpResponse<byte[]> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
			return processResponse(response, proxyPrefix, reqMethod);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			ProxyService.LOGGER.debug("Request interrupted: {}", reqUrl);
			return new ProxyResponse(499, ERROR_HEADERS, "Request interrupted".getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			ProxyService.LOGGER.error("Error fetching URL: {}", reqUrl, ex);
			return new ProxyResponse(502, ERROR_HEADERS, ("Proxy error: " + ex.getMessage()).getBytes(StandardCharsets.UTF_8));
		}
	}

	private static ProxyResponse processResponse(HttpResponse<byte[]> response, String proxyPrefix, String reqMethod) {
		int resStatusCode = response.statusCode();

		Map<String, List<String>> resHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		String resContentType = response.headers().firstValue("content-type")
			.orElse("application/octet-stream").toLowerCase();

		byte[] resBody = response.body() != null ? response.body() : new byte[0];

		boolean willRewriteContent = resStatusCode != 206
			&& !"HEAD".equalsIgnoreCase(reqMethod) && !"OPTIONS".equalsIgnoreCase(reqMethod)
			&& ProxyService.matchesContentType(resContentType, ProxyService.HTML_CONTENT_TYPES, ProxyService.CSS_CONTENT_TYPES);

		response.headers().map().forEach((key, values) -> {
			if (ProxyService.ALLOWED_RESPONSE_HEADERS.contains(key.toLowerCase())) {
				if (key.equalsIgnoreCase("location")) {
					List<String> rewrittenValues = new ArrayList<>();
					for (String v : values) {
						String s = ProxyService.rewriteLocation(v, proxyPrefix);
						rewrittenValues.add(s);
					}
					resHeaders.put(key, rewrittenValues);
				} else if (key.equalsIgnoreCase("set-cookie")) {
					List<String> rewrittenValues = new ArrayList<>();
					for (String v : values) {
						String s = ProxyService.rewriteCookie(v, proxyPrefix);
						rewrittenValues.add(s);
					}
					resHeaders.put(key, rewrittenValues);
				} else if (willRewriteContent && (key.equalsIgnoreCase("etag") || key.equalsIgnoreCase("last-modified"))) {
					// Skip cache validators for rewritten content, they're no longer valid
				} else {
					resHeaders.put(key, values);
				}
			}
		});

		if (willRewriteContent) {
			Charset resCharset = ProxyService.extractCharset(resContentType);
			String resContent = new String(resBody, resCharset);
			resContent = ProxyService.rewriteContent(resContent, resContentType, proxyPrefix);
			resBody = resContent.getBytes(resCharset);
		}

		resHeaders.put("Content-Length", Collections.singletonList(String.valueOf(resBody.length)));

		return new ProxyResponse(resStatusCode, resHeaders, resBody);
	}

	@SafeVarargs
	private static boolean matchesContentType(String contentType, Set<String>... expectedContentTypes) {
		int semicolonIndex = contentType.indexOf(';');
		String baseMimeType = semicolonIndex != -1 ? contentType.substring(0, semicolonIndex).trim() : contentType.trim();
		for (Set<String> s : expectedContentTypes) {
			if (s.contains(baseMimeType)) return true;
		}
		return false;
	}

	private static Charset extractCharset(String contentType) {
		Matcher matcher = CHARSET_PATTERN.matcher(contentType);
		if (matcher.find()) {
			try {
				return Charset.forName(matcher.group(1));
			} catch (Exception ignored) {}
		}
		return StandardCharsets.UTF_8;
	}

	private static String rewriteLocation(String location, String proxyPrefix) {
		String[] parts = proxyPrefix.split("/");
		String origin = parts[parts.length - 3] + "://" + parts[parts.length - 2] + ":" + parts[parts.length - 1];
		// Rewrite absolute URLs for the same origin
		if (location.startsWith(origin)) {
			return proxyPrefix + location.substring(origin.length());
		}
		// Rewrite root-relative URLs
		if (location.startsWith("/") && !location.startsWith("//")) {
			return proxyPrefix + location;
		}
		return location;
	}

	private static String rewriteCookie(String cookie, String proxyPrefix) {
		return cookie
			// Remove Domain attribute to make cookie work with proxy domain
			.replaceAll("(?i);\\s*Domain\\s*=\\s*[^;]*", "")
			// Rewrite Path attribute to use proxy path
			.replaceAll("(?i);\\s*Path\\s*=\\s*[^;]*", "; Path=" + proxyPrefix);
	}

	private static String rewriteContent(String content, String contentType, String proxyPrefix) {
		if (ProxyService.matchesContentType(contentType, ProxyService.HTML_CONTENT_TYPES)) {
			content = ProxyService.CSP_META_PATTERN.matcher(content).replaceAll("");
			content = ProxyService.rewriteWithPattern(content, ProxyService.BASE_HREF_PATTERN, proxyPrefix);
			content = ProxyService.rewriteWithPattern(content, ProxyService.HTML_ATTR_PATTERN, proxyPrefix);
			content = ProxyService.rewriteWithPattern(content, ProxyService.CSS_URL_PATTERN, proxyPrefix);
			content = ProxyService.injectProxyInterceptor(content, proxyPrefix);
		} else if (ProxyService.matchesContentType(contentType, ProxyService.CSS_CONTENT_TYPES)) {
			content = ProxyService.rewriteWithPattern(content, ProxyService.CSS_URL_PATTERN, proxyPrefix);
		}

		return content;
	}

	private static String rewriteWithPattern(String content, Pattern pattern, String proxyPrefix) {
		StringBuilder result = new StringBuilder();

		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String prefix = matcher.group(1);
			String path = matcher.group(2);
			String suffix = matcher.group(3);
			matcher.appendReplacement(result, Matcher.quoteReplacement(prefix + proxyPrefix + path + suffix));
		}
		matcher.appendTail(result);

		return result.toString();
	}

	private static String injectProxyInterceptor(String content, String proxyPrefix) {
		String script = "<script>" +
			"\n(function() {" +
			"\n  const proxyPrefix = '" + proxyPrefix + "';" +
			"\n  function rewriteUrl(url) {" +
			"\n    if (typeof url !== 'string') return url;" +
			"\n    if (url.startsWith('/') && !url.startsWith('//') && !url.startsWith(proxyPrefix)) return proxyPrefix + url;" +
			"\n    return url;" +
			"\n  }" +
			"\n  function rewriteSrcset(srcset) {" +
			"\n    if (typeof srcset !== 'string') return srcset;" +
			"\n    return srcset.split(',').map(function(entry) {" +
			"\n      const parts = entry.trim().split(/\\s+/);" +
			"\n      parts[0] = rewriteUrl(parts[0]);" +
			"\n      return parts.join(' ');" +
			"\n    }).join(', ');" +
			"\n  }" +
			// Intercept fetch
			"\n  const originalFetch = window.fetch;" +
			"\n  window.fetch = function(url, opts) {" +
			"\n    if (url instanceof Request) url = new Request(rewriteUrl(url.url), url);" +
			"\n    else url = rewriteUrl(url);" +
			"\n    return originalFetch.call(this, url, opts);" +
			"\n  };" +
			// Intercept XMLHttpRequest.open
			"\n  const originalXHROpen = XMLHttpRequest.prototype.open;" +
			"\n  XMLHttpRequest.prototype.open = function(method, url) {" +
			"\n    return originalXHROpen.apply(this, [method, rewriteUrl(url)].concat(Array.prototype.slice.call(arguments, 2)));" +
			"\n  };" +
			// Intercept setAttribute for src/href/srcset/xlink:href
			"\n  const originalSetAttribute = Element.prototype.setAttribute;" +
			"\n  Element.prototype.setAttribute = function(name, value) {" +
			"\n    const ln = name.toLowerCase();" +
			"\n    if (ln === 'src' || ln === 'href' || ln === 'xlink:href' || ln === 'action' || ln === 'data-src' || ln === 'poster') value = rewriteUrl(value);" +
			"\n    else if (ln === 'srcset') value = rewriteSrcset(value);" +
			"\n    return originalSetAttribute.call(this, name, value);" +
			"\n  };" +
			// Intercept property setters for src/href
			"\n  ['HTMLImageElement', 'HTMLScriptElement', 'HTMLLinkElement', 'HTMLAnchorElement', 'HTMLIFrameElement', 'HTMLSourceElement', 'HTMLMediaElement'].forEach(function(tagName) {" +
			"\n    const proto = window[tagName] && window[tagName].prototype;" +
			"\n    if (!proto) return;" +
			"\n    ['src', 'href'].forEach(function(prop) {" +
			"\n      const desc = Object.getOwnPropertyDescriptor(proto, prop);" +
			"\n      if (desc && desc.set) {" +
			"\n        const originalSetter = desc.set;" +
			"\n        Object.defineProperty(proto, prop, {" +
			"\n          set: function(v) { originalSetter.call(this, rewriteUrl(v)); }," +
			"\n          get: desc.get," +
			"\n          configurable: true" +
			"\n        });" +
			"\n      }" +
			"\n    });" +
			"\n  });" +
			// MutationObserver to rewrite src/href/srcset on dynamically added elements
			"\n  new MutationObserver(function(mutations) {" +
			"\n    mutations.forEach(function(m) {" +
			"\n      m.addedNodes.forEach(function(node) {" +
			"\n        if (node.nodeType === 1) {" +
			"\n          ['src', 'href', 'xlink:href', 'action', 'data-src', 'poster'].forEach(function(attr) {" +
			"\n            const val = node.getAttribute && node.getAttribute(attr);" +
			"\n            if (val) { const nv = rewriteUrl(val); if (nv !== val) node.setAttribute(attr, nv); }" +
			"\n          });" +
			"\n          const srcset = node.getAttribute && node.getAttribute('srcset');" +
			"\n          if (srcset) { const nv = rewriteSrcset(srcset); if (nv !== srcset) node.setAttribute('srcset', nv); }" +
			"\n          node.querySelectorAll('[src], [href], [srcset], [action], [data-src], [poster]').forEach(function(el) {" +
			"\n            ['src', 'href', 'xlink:href', 'action', 'data-src', 'poster'].forEach(function(attr) {" +
			"\n              const val = el.getAttribute(attr);" +
			"\n              if (val) { const nv = rewriteUrl(val); if (nv !== val) el.setAttribute(attr, nv); }" +
			"\n            });" +
			"\n            const ss = el.getAttribute('srcset');" +
			"\n            if (ss) { const nv = rewriteSrcset(ss); if (nv !== ss) el.setAttribute('srcset', nv); }" +
			"\n          });" +
			"\n        }" +
			"\n      });" +
			"\n    });" +
			"\n  }).observe(document.documentElement, { childList: true, subtree: true });" +
			// Block Service Worker registration (SW would bypass proxy interceptors)
			"\n  if ('serviceWorker' in navigator) {" +
			"\n    const noop = function() { return Promise.reject(new Error('Service Workers are disabled in proxy mode')); };" +
			"\n    navigator.serviceWorker.register = noop;" +
			"\n    Object.defineProperty(navigator, 'serviceWorker', {" +
			"\n      get: function() { return { register: noop, getRegistration: noop, getRegistrations: function() { return Promise.resolve([]); } }; }" +
			"\n    });" +
			"\n  }" +
			"\n})();" +
			"\n</script>";

		Matcher matcher = ProxyService.HEAD_PATTERN.matcher(content);
		if (matcher.find()) {
			return matcher.replaceFirst(matcher.group(1) + script);
		}

		return script + content;
	}

	public static class ProxyResponse {
		private final int statusCode;
		private final Map<String, List<String>> headers;
		private final byte[] body;

		public ProxyResponse(int statusCode, Map<String, List<String>> headers, byte[] body) {
			this.statusCode = statusCode;
			this.headers = headers;
			this.body = body;
		}

		public int statusCode() {
			return statusCode;
		}

		public Map<String, List<String>> headers() {
			return headers;
		}

		public byte[] body() {
			return body;
		}
	}

}
