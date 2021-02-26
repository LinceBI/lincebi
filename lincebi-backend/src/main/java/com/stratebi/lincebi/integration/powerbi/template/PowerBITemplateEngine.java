package com.stratebi.lincebi.integration.powerbi.template;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class PowerBITemplateEngine extends TemplateEngine {

	public PowerBITemplateEngine() {
		this(new PowerBITemplateResolver());
	}

	public PowerBITemplateEngine(ITemplateResolver templateResolver) {
		this.setTemplateResolver(templateResolver);
	}

}
