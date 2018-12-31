package com.zcj.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilImageBase {

	private static final Logger logger = LoggerFactory.getLogger(UtilImageBase.class);

	/** 根据图片物理路径获取图片的宽和高 */
	public static Integer[] getWidthAndHeight(String filePath) {
		if (UtilString.isNotBlank(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				try {
					BufferedImage bi = ImageIO.read(file);
					return new Integer[] { bi.getWidth(), bi.getHeight() };
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return new Integer[] { null, null };
	}

	/** 根据图片输入流获取图片的宽和高 */
	public static Integer[] getWidthAndHeight(InputStream inputStream) {
		if (inputStream != null) {
			try {
				BufferedImage bi = ImageIO.read(inputStream);
				return new Integer[] { bi.getWidth(), bi.getHeight() };
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return new Integer[] { null, null };
	}

}
