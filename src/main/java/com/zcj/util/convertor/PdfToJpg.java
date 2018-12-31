package com.zcj.util.convertor;

import com.zcj.util.UtilFile;
import com.zcj.util.UtilString;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * 使用条件：</br>JAR包：icepdf-core.jar[详见pom.xml]
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月13日
 */
public class PdfToJpg {

	private static final Logger logger = LoggerFactory.getLogger(PdfToJpg.class);

	/**
	 * 把pdf文件的第一页转成图片
	 * 
	 * @param inputFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @return 转换成功的JPG格式文件目录，NULL表示转换失败
	 */
	public static String pdf2Jpg(String inputFilePath) {
		if (UtilString.isBlank(inputFilePath)) {
			return null;
		}
		File file = new File(inputFilePath);
		if (!file.exists()) {
			return null;
		}
		String outputFilePath = UtilFile.replaceSuffix(inputFilePath, "jpg");
		if (pdf2Jpg(inputFilePath, outputFilePath)) {
			return outputFilePath;
		} else {
			return null;
		}
	}

	private static boolean pdf2Jpg(String inputFilePath, String outputFilePath) {
		boolean result = false;
		Document document = new Document();
		try {
			document.setFile(inputFilePath);
			// int pages = document.getNumberOfPages();

			BufferedImage image = null;

			RenderedImage rendImage = null;
			// 第一页
			int i = 0;

			image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_TRIMBOX, 0f,
					1.5f);
			;
			rendImage = image;

			File file = new File(outputFilePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			ImageIO.write(rendImage, "jpg", file);

			image.flush();

			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			document.dispose();
		}
		return result;
	}
}
