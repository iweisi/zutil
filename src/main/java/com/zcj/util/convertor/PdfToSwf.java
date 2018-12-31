package com.zcj.util.convertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.UtilString;

/**
 * 使用条件：</br>软件：SWFTools
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月13日
 */
public class PdfToSwf {

	private static final Logger logger = LoggerFactory.getLogger(PdfToSwf.class);

	private static String getFilePath(String file) {
		String result = file.substring(0, file.lastIndexOf("/"));
		if (file.substring(2, 3) == "/") {
			result = file.substring(0, file.lastIndexOf("/"));
		} else if (file.substring(2, 3) == "\\") {
			result = file.substring(0, file.lastIndexOf("\\"));
		}
		return result;
	}

	private static void newFolder(String folderPath) {
		try {
			File myFolderPath = new File(folderPath.toString());
			if (!myFolderPath.exists()) {
				myFolderPath.mkdir();
			}
		} catch (Exception e) {
			throw new RuntimeException("转swf失败：新建目录" + folderPath + "失败");
		}
	}

	/**
	 * pdf转swf
	 * 
	 * @param swfToolsPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param languagedir
	 *            languagedir的路径，可以为NULL，如：D:\\xpdf\\xpdf-chinese-simplified
	 * @param sourcePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param destPath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean pdf2swf(String swfToolsPath, String languagedir, String sourcePath, String destPath) {
		try {
			// 如果目标文件的路径是新的，则新建路径
			newFolder(getFilePath(destPath));

			// 源文件不存在
			File source = new File(sourcePath);
			if (!source.exists()) {
				throw new RuntimeException("转swf失败：源文件" + sourcePath + "不存在");
			}

			// 调用pdf2swf命令进行转换
			String command = swfToolsPath + "/pdf2swf.exe  -t \"" + sourcePath + "\" -o  \"" + destPath
					+ "\" -s flashversion=9 ";

			if (UtilString.isNotBlank(languagedir)) {
				File file = new File(languagedir);
				if (file.exists()) {
					command = command + " -s languagedir=" + languagedir;
				}
			}

			logger.debug("命令操作:" + command + "开始转换...");

			// 调用外部程序
			Process process = Runtime.getRuntime().exec(command);
			final InputStream is1 = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					try {
						while (br.readLine() != null)
							;
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
			InputStream is2 = process.getErrorStream();
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
			// 保存输出结果流
			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = br2.readLine()) != null)
				// 循环等待ffmpeg进程结束
				buf.append(line);
			while (br2.readLine() != null)
				;
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			logger.info("转换结束..");
			return process.exitValue() == 0;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * pdf转swf
	 * 
	 * @param swfToolsPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourcePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param destPath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean pdf2swf(String swfToolsPath, String sourcePath, String destPath) {
		return pdf2swf(swfToolsPath, null, sourcePath, destPath);
	}

}
