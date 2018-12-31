package com.zcj.util.freemarker.keywordfilter;

import java.util.List;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;

public class HtmlAndKeywordFreeMarkerConfigurer extends FreeMarkerConfigurer {

	private String keywordFilePath;

	@Override
	protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
		String keyword = KeywordRegex.getInstance(keywordFilePath).getKeyword();
		return new HtmlAndKeywordTemplateLoader(super.getAggregateTemplateLoader(templateLoaders), keyword);
	}

	public String getKeywordFilePath() {
		return keywordFilePath;
	}

	public void setKeywordFilePath(String keywordFilePath) {
		this.keywordFilePath = keywordFilePath;
	}

}
