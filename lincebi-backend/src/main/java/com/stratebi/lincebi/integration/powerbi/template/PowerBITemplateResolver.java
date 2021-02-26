package com.stratebi.lincebi.integration.powerbi.template;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class PowerBITemplateResolver extends ClassLoaderTemplateResolver {

	public PowerBITemplateResolver() {
		this.setPrefix("/templates/integration/powerbi/");
		this.setSuffix(".html");
		this.setTemplateMode(TemplateMode.HTML);
		this.setCharacterEncoding("UTF-8");
		this.setCacheable(false);
	}

}
