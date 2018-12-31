package com.zcj.web.springmvc.support;

import com.zcj.util.UtilCollection;
import com.zcj.util.UtilString;
import com.zcj.util.filenameutils.FilenameUtils;
import com.zcj.web.dto.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Downloader {

	private static final Logger logger = LoggerFactory.getLogger(Downloader.class);
	
	private final HttpServletResponse response;
	private final String basePath;// 绝对根路径
	private final String filePath;// 文件相对basePath的路径
	private final String showName;// 下载后显示的文件名称
	private final Set<String> allowFilePaths;// 允许下载的相对basePath的路径集合；例："/download/.*"

	private Downloader(Builder builder) {
		response = builder.response;
		basePath = builder.basePath;
		filePath = builder.filePath;
		showName = builder.showName;
		allowFilePaths = builder.allowFilePaths;
	}

	public static class Builder {

		private final HttpServletResponse response;

		private String basePath = "";// 绝对根路径
		private String filePath = "";// 文件相对basePath的路径
		private String showName = "";// 下载后显示的文件名称
		private Set<String> allowFilePaths = new HashSet<String>();// 允许下载的相对basePath的路径集合；例："/download/.*"

		/**
		 * 构建下载组件
		 * 
		 * @param response
		 */
		public Builder(HttpServletResponse response) {
			this.response = response;
		}

		/**
		 * 设置物理根路径
		 * 
		 * @param basePath
		 *            例：Configuration.getRealPath()
		 * @return
		 */
		public Builder basePath(String basePath) {
			this.basePath = basePath;
			return this;
		}

		/**
		 * 设置文件相对basePath的路径
		 * 
		 * @param filePath
		 *            例：/upload/201712/123.png
		 * @return
		 */
		public Builder filePath(String filePath) {
			this.filePath = filePath;
			return this;
		}

		/**
		 * 设置文件下载后显示的文件名称
		 * 
		 * @param showName
		 *            如需支持中文，需要设置容器（Tomcat）的编码
		 * @return
		 */
		public Builder showName(String showName) {
			this.showName = showName;
			return this;
		}

		/**
		 * 添加允许下载的相对basePath的路径规则
		 * 
		 * @param allowFilePaths
		 *            相对basePath的目录
		 *            <p>
		 *            例："/download/.*"
		 * @return
		 */
		public Builder addAllowFilePaths(String... allowFilePaths) {
			if (allowFilePaths != null && allowFilePaths.length > 0) {
				for (String a : allowFilePaths) {
					if (UtilString.isNotBlank(a)) {
						this.allowFilePaths.add(a);
					}
				}
			}
			return this;
		}

		public Downloader build() {
			if (basePath == null) {
				basePath = "";
			}
			if (UtilString.isBlank(showName) && UtilString.isNotBlank(filePath)) {
				showName = FilenameUtils.getName(filePath);
			}
			if (allowFilePaths == null || allowFilePaths.size() == 0) {
				allowFilePaths = UtilCollection.initHashSet(".*");
			}
			return new Downloader(this);
		}
	}

	public ServiceResult go() {

		// 判断是否允许下载
		boolean allowDownload = false;
		if (UtilString.isNotBlank(filePath) && allowFilePaths != null && allowFilePaths.size() > 0) {
			String filePathMatch = filePath.replaceAll("\\\\", "/");
			for (String exc : allowFilePaths) {
				if (Pattern.matches(exc, filePathMatch)) {
					allowDownload = true;
					break;
				}
			}
		}
		if (!allowDownload) {
			try {
				OutputStream ous = new BufferedOutputStream(response.getOutputStream());
				ous.write("路径不存在！".getBytes());
				ous.flush();
				ous.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return ServiceResult.initError("路径不存在！");
		}

		try {
			File f = new File(basePath + filePath);

			InputStream ins = new BufferedInputStream(new FileInputStream(basePath + filePath));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();

			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(showName, "UTF-8"));
			response.addHeader("Content-Length", "" + f.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			ous.write(buffer);
			ous.flush();
			ous.close();
			return ServiceResult.initSuccess(null);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return ServiceResult.initError(null);
	}
}
