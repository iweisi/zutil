package com.zcj.web.springmvc.support;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.UtilCollection;
import com.zcj.web.dto.ServiceResult;

@Deprecated
public class FileSupport {

	private static final Logger logger = LoggerFactory.getLogger(FileSupport.class);
	
	/**
	 * 上传附件
	 * <p>
	 * 依赖 commons-io-*.jar
	 * <p>
	 * 
	 * @param request
	 * @param type
	 *            文件夹名称，支持“-”连接的多级文件夹结构；例："upload-201712"
	 * @param basePath
	 *            文件保存的物理根路径
	 * @param allowUploadPaths
	 *            系统允许存储文件的相对basePath的目录集合；例："/upload/.*"、"/upload.*"
	 * @param suffixAllow
	 *            后缀规则（true：允许的后缀；false：不允许的后缀），具体后缀列表在suffixArray参数中配置
	 * @param suffixArray
	 *            文件后缀集合，使用小写；例：{".exe",".jsp"}、UtilCollection.initHashSet(
	 *            UtilFile.dangerFileTpye)
	 * @return ServiceResult对象。
	 *         <p>
	 *         操作失败：s=0；d=失败原因
	 *         <p>
	 *         操作成功（单文件上传）：s=1；d=UploadSuccessResult对象
	 *         <p>
	 *         操作成功（多文件上传）：s=1；d=UploadResult对象
	 */
	public static ServiceResult uploadFile(HttpServletRequest request, String type, String basePath,
			Set<String> allowUploadPaths, boolean suffixAllow, Set<String> suffixArray) {
		if (allowUploadPaths == null || allowUploadPaths.size() == 0) {
			return ServiceResult.initError("请配置允许上传的目录");
		}
		if (suffixArray == null || suffixArray.size() == 0) {
			return ServiceResult.initError("请配置允许上传的文件后缀");
		}
		Uploader.Builder builder = new Uploader.Builder(request).basePath(basePath).folder(type)
				.addAllowUploadPaths(allowUploadPaths.toArray(new String[] {}));
		if (suffixAllow) {
			builder = builder.addAllowsSuffixs(suffixArray.toArray(new String[] {}));
		} else {
			builder = builder.addUnAllowsSuffixs(suffixArray.toArray(new String[] {}));
		}
		return builder.build().go();
	}

	/**
	 * 通过FastDFS上传附件，并指定Group
	 * <p>
	 * 依赖：fastdfs-client-java-20141207.jar
	 * <p>
	 * 
	 * @param request
	 * @param szTrackerServers
	 *            FastDFS服务器地址及端口
	 *            <p>
	 *            例：{"192.168.1.111:22122","192.168.1.119:22122"}
	 * @param param
	 *            上传附件时附带的参数
	 * @param group
	 *            FastDFS的group名，可为空
	 * @return ServiceResult对象。
	 *         <p>
	 *         操作失败：s=0；d=失败原因
	 *         <p>
	 *         操作成功（单文件上传）：s=1；d=UploadSuccessResult对象
	 *         <p>
	 *         操作成功（多文件上传）：s=1；d=UploadResult对象
	 */
	public static ServiceResult uploadFileFastDFS(HttpServletRequest request, String[] szTrackerServers,
			Map<String, Object> param, String group) {
		return new Uploader.Builder(request).fastDFSAddServers(szTrackerServers).fastDFSParam(param)
				.fastDFSGroup(group).build().go();
	}

	/**
	 * 根据文件路径下载文件
	 * 
	 * @param response
	 * @param basePath
	 *            绝对路径的前缀
	 * @param filePath
	 *            文件相对basePath的路径
	 * @param showName
	 *            显示的文件名称
	 * @param allowPaths
	 *            必填；系统允许下载的相对basePath的目录集合；例："/download/.*"
	 * @return
	 */
	public static ServiceResult downloadFile(HttpServletResponse response, String basePath, String filePath,
			String showName, Set<String> allowPaths) {
		return new Downloader.Builder(response).basePath(basePath).filePath(filePath).showName(showName)
				.addAllowFilePaths(allowPaths.toArray(new String[] {})).build().go();
	}

	/**
	 * 存在安全性漏洞，必须马上替换成新方法（可根据路径随意下载项目内的文件）
	 */
	@Deprecated
	public static ServiceResult downloadFile(HttpServletResponse response, String filePath, String showName) {
		try {
			File f = new File(filePath);

			InputStream ins = new BufferedInputStream(new FileInputStream(filePath));
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

	/**
	 * 存在安全性漏洞，必须马上替换成新方法（可上传文件到项目的任意目录）
	 */
	@Deprecated
	public static ServiceResult uploadFile(HttpServletRequest request, String type, String basePath) {
		return uploadFile(request, type, basePath, UtilCollection.initHashSet(".*"), false,
				UtilCollection.initHashSet(".jsp", ".exe"));
	}

}
