package com.stratebi.lincebi.urlaudit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.repository2.unified.IUnifiedRepository;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.data.simple.SimpleRepositoryFileData;
import org.pentaho.platform.engine.core.audit.AuditHelper;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.solution.BaseContentGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

public class UrlContentGenerator extends BaseContentGenerator {

	private static final long serialVersionUID = 7679422728499629810L;

	private static final Log LOGGER = LogFactory.getLog(UrlContentGenerator.class);

	private static final String MESSAGE_TYPE = "url_file_open";
	private static final String BASE_OBJECT_TYPE = UrlContentGenerator.class.getName();
	private static final String INTEGRATION_PATH_PREFIX = "plugin/lincebi/api/integration/";
	private static final Map<String, String> INTEGRATION_SUFFIXES = Map.of(
		"powerbi", "PowerBI",
		"superset", "Superset",
		"proxy", "Proxy"
	);

	@Override
	public void createContent() throws Exception {
		IParameterProvider pathParams = this.parameterProviders.get("path");
		if (pathParams == null) {
			throw new IllegalStateException("Path parameter provider not available");
		}

		RepositoryFile file = (RepositoryFile)pathParams.getParameter("file");
		if (file == null) {
			throw new IllegalArgumentException("Repository file not provided");
		}

		IUnifiedRepository repository = PentahoSystem.get(IUnifiedRepository.class);
		if (repository == null) {
			throw new IllegalStateException("Could not get IUnifiedRepository");
		}

		String repoPath = file.getPath();

		String targetUrl = this.extractUrl(repository, file);
		if (targetUrl.isEmpty()) {
			throw new IllegalArgumentException("Could not extract URL from file: " + repoPath);
		}

		String redirectUrl = targetUrl;
		if (!targetUrl.startsWith("http://") && !targetUrl.startsWith("https://") && !targetUrl.startsWith("/")) {
			redirectUrl = PentahoSystem.getApplicationContext().getFullyQualifiedServerURL() + targetUrl;
		}

		this.auditUrlFileAccess(repoPath, targetUrl);

		HttpServletResponse response = (HttpServletResponse)pathParams.getParameter("httpresponse");
		if (response != null) {
			response.sendRedirect(redirectUrl);
		} else {
			OutputStream out = this.outputHandler.getOutputContentItem("response", "content", this.instanceId, "text/html").getOutputStream(this.itemName);
			String html = String.format(
				"<!DOCTYPE html><html><head><meta http-equiv=\"refresh\" content=\"0;url=%s\"></head><body>Redirecting...</body></html>",
				redirectUrl.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;")
			);
			out.write(html.getBytes(StandardCharsets.UTF_8));
			out.flush();
			out.close();
		}
	}

	private String extractUrl(IUnifiedRepository repository, RepositoryFile file) {
		try {
			SimpleRepositoryFileData data = repository.getDataForRead(file.getId(), SimpleRepositoryFileData.class);
			if (data == null) {
				return "";
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(data.getInputStream(), writer, StandardCharsets.UTF_8);
			String props = writer.toString();

			StringTokenizer tokenizer = new StringTokenizer(props, "\n");
			while (tokenizer.hasMoreTokens()) {
				String line = tokenizer.nextToken();
				int pos = line.indexOf('=');
				if (pos > 0) {
					String key = line.substring(0, pos);
					String value = line.substring(pos + 1);
					if (!value.isEmpty() && value.charAt(value.length() - 1) == '\r') {
						value = value.substring(0, value.length() - 1);
					}
					if ("URL".equalsIgnoreCase(key)) {
						return value;
					}
				}
			}

			return "";
		} catch (Exception e) {
			UrlContentGenerator.LOGGER.error("Error extracting URL from file: " + file.getPath(), e);
			return "";
		}
	}

	private void auditUrlFileAccess(String filePath, String targetUrl) {
		try {
			IPentahoSession session = PentahoSessionHolder.getSession();
			String sessionId = session != null ? session.getId() : UUID.randomUUID().toString();
			String userId = session != null ? session.getName() : "anonymous";
			String processId = UUID.randomUUID().toString();
			String messageId = UUID.randomUUID().toString();
			String messageValue = "URL=" + targetUrl;
			String objectType = this.resolveObjectType(targetUrl);
			AuditHelper.audit(
				sessionId,    // inst_id
				userId,       // actor
				filePath,     // obj_id
				objectType,   // obj_type
				processId,    // job_id
				MESSAGE_TYPE, // message_type
				messageId,    // message_name
				messageValue, // message_text_value
				0.0f,         // duration
				this          // ILogger
			);
		} catch (Exception e) {
			UrlContentGenerator.LOGGER.error("Failed to create audit entry for URL file: " + filePath, e);
		}
	}

	private String resolveObjectType(String targetUrl) {
		if (targetUrl != null) {
			int idx = targetUrl.indexOf(INTEGRATION_PATH_PREFIX);
			if (idx >= 0) {
				String remainder = targetUrl.substring(idx + INTEGRATION_PATH_PREFIX.length());
				int slashIdx = remainder.indexOf('/');
				String integration = slashIdx > 0 ? remainder.substring(0, slashIdx) : remainder;
				String suffix = INTEGRATION_SUFFIXES.get(integration);
				if (suffix != null) {
					return BASE_OBJECT_TYPE + "$" + suffix;
				}
			}
		}
		return BASE_OBJECT_TYPE;
	}

	@Override
	public Log getLogger() {
		return UrlContentGenerator.LOGGER;
	}
}
