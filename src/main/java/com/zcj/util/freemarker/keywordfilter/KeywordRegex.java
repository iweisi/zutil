package com.zcj.util.freemarker.keywordfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.zcj.util.UtilString;

public class KeywordRegex {

	private static final Logger logger = LoggerFactory.getLogger(KeywordRegex.class);

	private static KeywordRegex singleton;

	private String keyword;

	private KeywordRegex() {

	}

	public static synchronized KeywordRegex getInstance(String keywordFilePath) {
		if (singleton == null) {
			singleton = new KeywordRegex();
			singleton.keyword = getAllFileContent(keywordFilePath);
		}
		return singleton;
	}

	private static String getAllFileContent(String keywordFilePath) {
		if (UtilString.isBlank(keywordFilePath)) {
			return "";
		}
		// 所有文件里的内容
		String keywordTemp = "";
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] metaInfResources = resourcePatternResolver.getResources(keywordFilePath);
			for (Resource r : metaInfResources) {
				// 单个文件里的内容
				String ctemp = getFileContent(r.getURL().getPath());
				if (UtilString.isNotBlank(ctemp)) {
					if (UtilString.isNotBlank(keywordTemp)) {
						keywordTemp += "|";
					}
					keywordTemp += ctemp;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return keywordTemp;
	}

	private static String getFileContent(String filePath) {
		String result = "";
		File file = new File(filePath);
		if (file.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					if (UtilString.isNotBlank(result)) {
						result += "|";
					}
					result += tempString;
				}
				reader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return result;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
