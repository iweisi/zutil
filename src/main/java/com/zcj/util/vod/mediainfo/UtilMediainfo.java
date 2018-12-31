package com.zcj.util.vod.mediainfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilMediainfo {

	private static final Logger logger = LoggerFactory.getLogger(UtilMediainfo.class);

	public static MediainfoBean parser(final String softExePath, final String filePath) {
		return parser(softExePath, filePath, false);
	}

	public static MediainfoBean parser(final String softExePath, final String filePath, final boolean printLog) {
		if (StringUtils.isBlank(softExePath) || StringUtils.isBlank(filePath)) {
			return null;
		}
		String cmd = softExePath + " " + filePath;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (null != process) {
			return parser(process.getInputStream(), printLog);
		} else {
			return null;
		}
	}

	private static MediainfoBean parser(final InputStream stream, boolean printLog) {
		LineNumberReader in = null;
		MediainfoBean mediaInfo = new MediainfoBean();
		try {
			in = new LineNumberReader(new InputStreamReader(stream));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (printLog) {
					logger.debug(line);
				}
				mediaInfo.parse(line);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return mediaInfo;
	}

}
