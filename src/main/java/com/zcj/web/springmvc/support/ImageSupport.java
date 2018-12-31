package com.zcj.web.springmvc.support;

import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.zcj.util.UtilCollection;
import com.zcj.web.dto.ServiceResult;

@Deprecated
public class ImageSupport {

	/**
	 * 上传图片
	 * <p>
	 * 依赖 commons-io-*.jar、thumbnailator-*.jar
	 * <p>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param type
	 *            文件夹名称，支持“-”连接的多级文件夹结构
	 * @param basePath
	 *            文件保存的物理根路径
	 * @param waterPath
	 *            水印文件的相对地址，例：/upload/000000/a.png
	 * @param theMaxWidth
	 *            图片最大宽度
	 * @param theWidth
	 *            图片以固定宽度存储
	 * @param theHeight
	 *            图片以固定高度存储
	 * @param waterMinWidth
	 *            图片宽度达到指定值才添加水印
	 * @param waterMinHeight
	 *            图片高度达到指定值才添加水印
	 * @param allowUploadPaths
	 *            系统允许存储文件的相对basePath的目录集合；例："/upload/.*"、"/upload.*"
	 * @return ServiceResult对象。
	 *         <p>
	 *         操作失败：s=0；d=失败原因
	 *         <p>
	 *         操作成功（单文件上传）：s=1；d=UploadSuccessResult对象
	 *         <p>
	 *         操作成功（多文件上传）：s=1；d=UploadResult对象
	 */
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, String waterPath,
			String theMaxWidth, String theWidth, String theHeight, Integer waterMinWidth, Integer waterMinHeight,
			Set<String> allowUploadPaths) {
		if (allowUploadPaths == null || allowUploadPaths.size() == 0) {
			return ServiceResult.initError("请配置允许上传的目录");
		}
		return new Uploader.Builder(request).basePath(basePath).folder(type)
				.addAllowUploadPaths(allowUploadPaths.toArray(new String[] {})).waterPath(waterPath)
				.waterMinSize(waterMinWidth, waterMinHeight).imgMaxWidth(theMaxWidth).imgZoom(theWidth, theHeight)
				.build().go();
	}

	/**
	 * 上传图片
	 * <p>
	 * 依赖 commons-io-*.jar、thumbnailator-*.jar
	 * <p>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param type
	 *            文件夹名称，支持“-”连接的多级文件夹结构
	 * @param basePath
	 *            文件保存的物理根路径
	 * @param waterPath
	 *            水印文件的相对地址，例：/upload/000000/a.png
	 * @param maxWidth
	 *            图片最大宽度
	 * @param waterMinWidth
	 *            图片宽度达到指定值才添加水印
	 * @param waterMinHeight
	 *            图片高度达到指定值才添加水印
	 * @param allowUploadPaths
	 *            系统允许存储文件的相对basePath的目录集合；例："/upload/.*"、"/upload.*"
	 * @return ServiceResult对象。
	 *         <p>
	 *         操作失败：s=0；d=失败原因
	 *         <p>
	 *         操作成功（单文件上传）：s=1；d=UploadSuccessResult对象
	 *         <p>
	 *         操作成功（多文件上传）：s=1；d=UploadResult对象
	 */
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, String waterPath,
			String maxWidth, Integer waterMinWidth, Integer waterMinHeight, Set<String> allowUploadPaths) {
		return uploadImage(request, type, basePath, waterPath, maxWidth, null, null, waterMinWidth, waterMinHeight,
				allowUploadPaths);
	}

	/**
	 * 上传图片
	 * <p>
	 * 依赖 commons-io-*.jar、thumbnailator-*.jar
	 * <p>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param type
	 *            文件夹名称，支持“-”连接的多级文件夹结构
	 * @param basePath
	 *            文件保存的物理根路径
	 * @param theWidth
	 *            图片以固定宽度存储
	 * @param theHeight
	 *            图片以固定高度存储
	 * @param allowUploadPaths
	 *            系统允许存储文件的相对basePath的目录集合；例："/upload/.*"、"/upload.*"
	 * @return ServiceResult对象。
	 *         <p>
	 *         操作失败：s=0；d=失败原因
	 *         <p>
	 *         操作成功（单文件上传）：s=1；d=UploadSuccessResult对象
	 *         <p>
	 *         操作成功（多文件上传）：s=1；d=UploadResult对象
	 */
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, Integer theWidth,
			Integer theHeight, Set<String> allowUploadPaths) {
		return uploadImage(request, type, basePath, null, null, Objects.toString(theWidth, null),
				Objects.toString(theHeight, null), null, null, allowUploadPaths);
	}

	/**
	 * 存在安全性漏洞，必须马上替换成新方法（可上传文件到项目的任意目录）
	 */
	@Deprecated
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, String waterPath,
			String maxWidth, Integer waterMinWidth, Integer waterMinHeight) {
		return uploadImage(request, type, basePath, waterPath, maxWidth, null, null, waterMinWidth, waterMinHeight);
	}

	/**
	 * 存在安全性漏洞，必须马上替换成新方法（可上传文件到项目的任意目录）
	 */
	@Deprecated
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, Integer theWidth,
			Integer theHeight) {
		return uploadImage(request, type, basePath, null, null, Objects.toString(theWidth, null),
				Objects.toString(theHeight, null), null, null);
	}

	/**
	 * 存在安全性漏洞，必须马上替换成新方法（可上传文件到项目的任意目录）
	 */
	@Deprecated
	public static ServiceResult uploadImage(HttpServletRequest request, String type, String basePath, String waterPath,
			String theMaxWidth, String theWidth, String theHeight, Integer waterMinWidth, Integer waterMinHeight) {
		return uploadImage(request, type, basePath, waterPath, theMaxWidth, theWidth, theHeight, waterMinWidth,
				waterMinHeight, UtilCollection.initHashSet(".*"));
	}
}
