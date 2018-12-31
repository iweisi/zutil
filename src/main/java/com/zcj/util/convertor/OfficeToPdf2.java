package com.zcj.util.convertor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zcj.util.UtilFile;
import com.zcj.util.UtilString;
import com.zcj.util.filenameutils.FilenameUtils;

/**
 * 使用条件：</br>JAR包：jacob-1.18.jar[详见pom.xml]</br>软件：Office2010</br>DLL文件复制到System32下：jacob-1.18-x64.dll</br>JDK版本：1.7及以上
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月13日
 */
public class OfficeToPdf2 {

	private static final int xlTypePDF = 0;
	private static final int ppSaveAsPDF = 32;

	private static final Logger logger = LoggerFactory.getLogger(OfficeToPdf2.class);

	/**
	 * office格式转pdf格式（支持的格式：doc/docx/xls/xlsx/ppt/pptx）
	 * 
	 * @param inputFile
	 *            源文件的目录，如：D:/4444.doc
	 * @return 转换成功的pdf格式文件目录，NULL表示转换失败
	 */
	public static synchronized String toPdf(String inputFile) {
		if (UtilString.isBlank(inputFile)) {
			logger.debug("转Pdf文件失败:文件路径为空");
			return null;
		}
		File file = new File(inputFile);
		if (!file.exists()) {
			logger.debug("转Pdf文件失败:输入的路径“" + inputFile + "”不存在");
			return null;
		}
		String pdfFile = UtilFile.replaceSuffix(inputFile, "pdf");

		boolean ok = false;
		String format = FilenameUtils.getExtension(inputFile).toLowerCase();
		if (format.endsWith("doc") || format.endsWith("docx")) {
			ok = word2Pdf(inputFile, pdfFile);
		} else if (format.endsWith("xls") || format.endsWith("xlsx")) {
			ok = excel2PDF(inputFile, pdfFile);
		} else if (format.endsWith("ppt") || format.endsWith("pptx")) {
			ok = ppt2PDF(inputFile, pdfFile);
		}
		if (ok) {
			return pdfFile;
		}
		return null;
	}

	private static synchronized boolean word2Pdf(String inputFile, String pdfFile) {
		boolean result = false;
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] { inputFile, new Variant(false), new Variant(true) },
					new int[1]).toDispatch();
			File tofile = new File(pdfFile);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { pdfFile, new Variant(17) }, new int[1]);
			result = true;
		} catch (Exception e) {
			logger.error("转换文件:" + inputFile + ">>失败，错误信息：" + e.getMessage(), e); 
		} finally {
			if (doc != null) {
				Dispatch.call(doc, "Close", false);
			}
			if (app != null)
				app.invoke("Quit", new Variant[] {});
			// 如果没有这句话,winword.exe进程将不会关闭
			ComThread.Release();
		}
		return result;
	}

	private static synchronized boolean excel2PDF(String inputFile, String pdfFile) {
		boolean result = false;
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Excel.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch excels = app.getProperty("Workbooks").toDispatch();
			doc = Dispatch.call(excels, "Open", inputFile, false, true).toDispatch();
			File tofile = new File(pdfFile);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(doc, "ExportAsFixedFormat", xlTypePDF, pdfFile);
			result = true;
		} catch (Exception e) {
			logger.error("转换文件:" + inputFile + ">>失败，错误信息：" + e.getMessage(), e); 
		} finally {
			if (doc != null) {
				Dispatch.call(doc, "Close", false);
			}
			if (app != null)
				app.invoke("Quit", new Variant[] {});
			// 如果没有这句话,winword.exe进程将不会关闭
			ComThread.Release();
		}
		return result;
	}

	private static synchronized boolean ppt2PDF(String inputFile, String pdfFile) {
		boolean result = false;
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("PowerPoint.Application");
			// app.setProperty("Visible", new Variant(false));
			Dispatch ppts = app.getProperty("Presentations").toDispatch();
			doc = Dispatch.call(ppts, "Open", inputFile, true,// ReadOnly
					true,// Untitled指定文件是否有标题
					false// WithWindow指定文件是否可见
					).toDispatch();
			File tofile = new File(pdfFile);
			if (tofile.exists()) {
				tofile.delete();
			}
			// Dispatch.call(doc, "SaveAs", pdfFile, ppSaveAsPDF);
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { pdfFile, new Variant(ppSaveAsPDF) }, new int[1]);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (doc != null) {
				Dispatch.call(doc, "Close");
			}
			if (app != null)
				app.invoke("Quit", new Variant[] {});
			// 如果没有这句话,winword.exe进程将不会关闭
			ComThread.Release();
		}
		return result;
	}

}