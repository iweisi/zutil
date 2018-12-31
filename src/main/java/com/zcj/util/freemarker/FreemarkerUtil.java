package com.zcj.util.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker 工具类</br>
 *		FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, "/WEB-INF/ftl/www/template/index.ftl", "D:/index.jsp");</br>
 * @author zouchongjin@sina.com
 * @data 2014年6月12日
 */
public class FreemarkerUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtil.class);

	private static final String ENCODE = "UTF-8";
	private static FreemarkerUtil fu = null;
	private Configuration conf = null;
	private FreeMarkerConfigurer freemarkerConfig = null;

	private FreemarkerUtil() {
	}

	public static synchronized FreemarkerUtil getInstance(FreeMarkerConfigurer freemarkerConfig) {
		if (fu == null) {
			fu = new FreemarkerUtil();
		}
		if (fu.getConf() == null || fu.getFreemarkerConfig() == null) {
			fu.setFreemarkerConfig(freemarkerConfig);
			Configuration c = fu.getFreemarkerConfig().getConfiguration();
			c.setObjectWrapper(new DefaultObjectWrapper());
			fu.setConf(c);
		}
		return fu;
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}
	
	/**
	 * 静态化成页面
	 * @param datas
	 * @param file 模板文件的相对路径
	 * @param outFile 保存的绝对路径
	 * @return
	 */
	public synchronized boolean htmlFile(Map<String, Object> datas, String file, String outFile) {
		FileWriterWithEncoding out = null;
		try {
			Template temp = conf.getTemplate(file, ENCODE);
			
			File outF = new File(outFile);
			if (!outF.exists()) {
				(new File(outF.getParent())).mkdirs();
			}
			
			out = new FileWriterWithEncoding(outF, ENCODE);
			
			temp.process(datas, out);
			
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (TemplateException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {					
					out.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return false;
	}

	public FreeMarkerConfigurer getFreemarkerConfig() {
		return freemarkerConfig;
	}

	public void setFreemarkerConfig(FreeMarkerConfigurer freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}
	
}
