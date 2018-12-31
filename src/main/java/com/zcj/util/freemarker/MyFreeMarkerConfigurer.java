package com.zcj.util.freemarker;

import java.util.List;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;

public class MyFreeMarkerConfigurer extends FreeMarkerConfigurer {

	@Override
	protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {

		return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));

	}
}
