package com.zcj.util.convertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用条件：</br>软件：SWFTools
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月13日
 */
public class FileToSwf {

	private static final Logger logger = LoggerFactory.getLogger(FileToSwf.class);

	private static final String CONVERTFILETYPE = "pdf,jpg,jpeg,font,gif,png,wav";

	/**
	 * pdf,jpg,jpeg,font,gif,png,wav转swf
	 * 
	 * @param toolPath
	 *            swftools软件的安装目录，如：C:/Program Files (x86)/SWFTools
	 * @param sourceFilePath
	 *            源文件的目录，如：D:/4444.pdf
	 * @param swfFilePath
	 *            目标文件的目录，如：D:/4444.swf
	 * @return
	 */
	public static boolean fileToSwf(String toolPath, String sourceFilePath, String swfFilePath) {
		if (toolPath == null || toolPath == "") {
			throw new RuntimeException("转swf失败：转换工具的安装路径不能为空");
		}
		String filetype = sourceFilePath.substring(sourceFilePath.lastIndexOf(".") + 1);
		if (CONVERTFILETYPE.indexOf(filetype.toLowerCase()) == -1) {
			throw new RuntimeException("转swf失败：只支持" + CONVERTFILETYPE + "格式转换成swf");
		}
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists()) {
			throw new RuntimeException("转swf失败：" + sourceFilePath + "文件不存在");
		}
		if (!toolPath.endsWith(File.separator)) {
			toolPath += File.separator;
		}
		File swfFile = new File(swfFilePath);
		if (!swfFile.getParentFile().exists()) {
			swfFile.getParentFile().mkdirs();
		}
		if (filetype.toLowerCase().equals("jpg")) {
			filetype = "jpeg";
		}
		List<String> command = new ArrayList<String>();
		command.add(toolPath + "\\" + filetype.toLowerCase() + "2swf.exe");// 从配置文件里读取
		command.add("-z");
		command.add("-s");
		command.add("flashversion=9");
		command.add("-s");
		command.add("poly2bitmap");// 加入poly2bitmap的目的是为了防止出现大文件或图形过多的文件转换时的出错，没有生成swf文件的异常
		command.add(sourceFilePath);
		command.add("-o");
		command.add(swfFilePath);
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(command);
			Process process = processBuilder.start();
			dealWith(process);
			try {
				process.waitFor();// 等待子进程的结束，子进程就是系统调用文件转换这个新进程
			} catch (InterruptedException e) {
				logger.warn(e.getMessage(), e);
			}
			File swf = new File(swfFilePath);
			if (!swf.exists()) {
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException("转swf失败");
		}
		return true;
	}

	private static void dealWith(final Process pro) {
		// 下面是处理堵塞的情况
		try {
			new Thread() {
				public void run() {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(pro.getInputStream()));
					String text;
					try {
						while ((text = br1.readLine()) != null) {
							logger.info(text);
						}
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(new InputStreamReader(pro.getErrorStream()));// 这定不要忘记处理出理时产生的信息，不然会堵塞不前的
					String text;
					try {
						while ((text = br2.readLine()) != null) {
							System.err.println(text);
						}
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
