package com.zcj.util.freemarker.keywordfilter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;

import com.zcj.util.UtilString;

import freemarker.cache.TemplateLoader;

/**
 * 处理输出时的HTML数据并过滤关键字
 * 	如果不需要转义处理，则在输出两端加标签noescape
 * 		例：<#noescape>${(obj.content)!}</#noescape>
 *	如果不需要转义处理并不过滤关键字，则在输出两端加两个noescape
 * 		例：<#noescape><#noescape>${(obj.content)!}</#noescape></#noescape>
 * 
 * @author zouchongjin@sina.com
 * @data 2016年5月13日
 */
public class HtmlAndKeywordTemplateLoader implements TemplateLoader {

	private final TemplateLoader delegate;
	private final String keyword;

	public HtmlAndKeywordTemplateLoader(TemplateLoader delegate, String keyword) {
		this.delegate = delegate;
		this.keyword = keyword;
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
		String s = "<#escape x as x?html>" + templateText + "</#escape>";
		if (UtilString.isNotBlank(keyword)) {
			s = "<#escape x as x?replace('" + keyword + "', '*', 'r')>" + s + "</#escape>";
		} else {
			s = "<#escape x as x>" + s + "</#escape>";
		}
		return new StringReader(s);
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		delegate.closeTemplateSource(templateSource);
	}

}