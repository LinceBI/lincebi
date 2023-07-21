package com.stratebi.lincebi.integration.superset.template;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class SupersetTemplateResolver extends ClassLoaderTemplateResolver {

	public SupersetTemplateResolver() {
		this.setPrefix("/WEB-INF/templates/integration/superset/");
		this.setSuffix(".html");
		this.setTemplateMode(TemplateMode.HTML);
		this.setCharacterEncoding("UTF-8");
		this.setCacheable(false);
	}

}
