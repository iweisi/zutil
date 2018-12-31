package com.zcj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilProperties {

	private static final Logger logger = LoggerFactory.getLogger(UtilProperties.class);

	public static boolean contains(String filePath, String key) {
		Properties props = get(filePath);
		return props.containsKey(key);
	}

	public static String get(String filePath, String key) {
		Properties props = get(filePath);
		return (props != null) ? props.getProperty(key) : null;
	}

	private static Properties get(String filePath) {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			fis = new FileInputStream(filePath);
			props.load(fis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return props;
	}

	public static void set(String filePath, String key, String value) {
		Properties props = get(filePath);
		props.setProperty(key, value);
		set(filePath, props);
	}

	public static void add(String filePath, Properties ps) {
		Properties props = get(filePath);
		props.putAll(ps);
		set(filePath, props);
	}

	private static void set(String filePath, Properties p) {
		FileOutputStream fos = null;
		try {
			File conf = new File(filePath);
			fos = new FileOutputStream(conf);
			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void remove(String filePath, String... key) {
		Properties props = get(filePath);
		for (String k : key)
			props.remove(k);
		set(filePath, props);
	}

}
