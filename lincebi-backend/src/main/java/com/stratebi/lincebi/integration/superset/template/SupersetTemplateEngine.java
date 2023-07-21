package com.stratebi.lincebi.integration.superset.template;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class SupersetTemplateEngine extends TemplateEngine {

	public SupersetTemplateEngine() {
		this(new SupersetTemplateResolver());
	}

	public SupersetTemplateEngine(ITemplateResolver templateResolver) {
		this.setTemplateResolver(templateResolver);
	}

}
