package com.zcj.util.convertor;

/**
 * 文件格式转换
 * 
 * @author zouchongjin@sina.com
 * @data 2015年8月25日
 */
@Deprecated
public class UtilConvert {

	/**
	 * pdf转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean pdf2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * jpg转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.jpg
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean jpg2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * jpeg转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean jpeg2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * font转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean font2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * gif转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean gif2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * png转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean png2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

	/**
	 * wav转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean wav2swf(String toolPath, String sourceFilePath, String swfFilePath) {
		return FileToSwf.fileToSwf(toolPath, sourceFilePath, swfFilePath);
	}

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
		return OfficeToPdf.office2pdf(openOfficePath, inputFilePath, outputFilePath);
	}
}
