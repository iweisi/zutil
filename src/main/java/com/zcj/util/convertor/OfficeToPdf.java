package com.zcj.util.convertor;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件</br>
 * </br>
 * 建议使用com.zcj.util.convertor.OfficeToPdf2.java</br>
 * </br>
 * 使用条件：</br>JAR包：详见pom.xml</br>软件：OpenOffice 4
 * 
 * @author ZCJ
 * @data 2013年12月25日
 */
@Deprecated
public class OfficeToPdf {
	
	private static final Logger logger = LoggerFactory.getLogger(OfficeToPdf.class);

	private static String[] FIXLIST = new String[] { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf" };

	/**
	 * Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件
	 * 
	 * @param openOfficePath
	 *            openOffice安装的路径。<br>
	 *            Windows，如：C:/Program Files (x86)/OpenOffice 4<br>
	 *            Linux，如：/opt/openoffice.org3<br>
	 *            Mac，如：/Application/OpenOffice.org.app/Contents<br>
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            目标文件路径，如："e:/test.pdf"<br>
	 *            如果未空，则目标文件路径等同于源文件路径（只修改文件后缀为pdf）
	 * @return
	 */
	public static boolean office2pdf(String openOfficePath, String inputFilePath, String outputFilePath) {
		if (!Arrays.asList(FIXLIST).contains(getPostfix(inputFilePath))) {
			throw new RuntimeException("转pdf失败：不支持" + getPostfix(inputFilePath) + "格式的文件");
		}

		boolean flag = false;
		OfficeManager officeManager = getOfficeManager(openOfficePath);
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		long begin_time = new Date().getTime();
		if (null != inputFilePath) {
			File inputFile = new File(inputFilePath);
			if (null == outputFilePath) {
				String outputFilePath_end = getOutputFilePath(inputFilePath);
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath_end, inputFilePath, outputFilePath, converter);
					flag = true;
				} else {
					throw new RuntimeException("转pdf失败：文件" + inputFilePath + "不存在");
				}
			} else {
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath, inputFilePath, outputFilePath, converter);
					flag = true;
				} else {
					throw new RuntimeException("转pdf失败：文件" + inputFilePath + "不存在");
				}
			}
			officeManager.stop();
		} else {
			throw new RuntimeException("转pdf失败：文件不能为空");
		}
		long end_time = new Date().getTime();
		logger.debug("文件转换耗时：[" + (end_time - begin_time) + "]ms");
		return flag;
	}

	private static OfficeManager getOfficeManager(String openOfficePath) {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		config.setOfficeHome(openOfficePath);
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		return officeManager;
	}

	private static void converterFile(File inputFile, String outputFilePath_end, String inputFilePath, String outputFilePath,
			OfficeDocumentConverter converter) {
		File outputFile = new File(outputFilePath_end);
		// 假如目标路径不存在,则新建该路径
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		converter.convert(inputFile, outputFile);
		logger.debug("文件：" + inputFilePath + "转换为目标文件：" + outputFile + "成功！");
	}

	private static String getOutputFilePath(String inputFilePath) {
		String outputFilePath = inputFilePath.replaceAll("." + getPostfix(inputFilePath), ".pdf");
		return outputFilePath;
	}

	private static String getPostfix(String inputFilePath) {
		return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
	}
}
