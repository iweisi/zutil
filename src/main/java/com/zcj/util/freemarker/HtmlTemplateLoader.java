package com.zcj.util.freemarker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;

import freemarker.cache.TemplateLoader;

/**
 * 处理输出时的HTML数据，如果不需要处理，则在输出两端加标签noescape，例：<#noescape>${(obj.content)!}</#noescape>
 * @author zouchongjin@sina.com
 * @data 2015年3月13日
 */
public class HtmlTemplateLoader implements TemplateLoader {

	private static final String HTML_ESCAPE_PREFIX = "<#escape x as x?html>";
	private static final String HTML_ESCAPE_SUFFIX = "</#escape>";

	private final TemplateLoader delegate;

	public HtmlTemplateLoader(TemplateLoader delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return delegate.findTemplateSource(name);
	}

	@Override
	public long getLastModified(Object templateSource) {
		return delegate.getLastModified(templateSource);
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		Reader reader = delegate.getReader(templateSource, encoding);
		String templateText = IOUtils.toString(reader);
		return new StringReader(HTML_ESCAPE_PREFIX + templateText + HTML_ESCAPE_SUFFIX);
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		delegate.closeTemplateSource(templateSource);
	}

}