package com.zcj.web.springmvc.support;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zcj.ext.fastdfs.FastdfsManager;
import com.zcj.ext.oss.aliyun.AliyunOssConfig;
import com.zcj.ext.oss.aliyun.AliyunOssUtil;
import com.zcj.util.UtilCollection;
import com.zcj.util.UtilDate;
import com.zcj.util.UtilFile;
import com.zcj.util.UtilImage.ImageBuilder;
import com.zcj.util.UtilMedia;
import com.zcj.util.UtilString;
import com.zcj.util.filenameutils.FilenameUtils;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.dto.UploadErrorResult;
import com.zcj.web.dto.UploadResult;
import com.zcj.web.dto.UploadSuccessResult;

public class Uploader {
	
	private static final Logger logger = LoggerFactory.getLogger(Uploader.class);

	private final HttpServletRequest request;

	private final Integer uploadType;// 上传方式（1：本地上传[默认]；2：FastDFS上传；3：Aliyun-OSS上传）

	// 本地上传时的目录相关配置（uploadType = 1 || uploadType = 3）
	private final String basePath;// 文件保存的物理根路径
	private final String folder;// 文件夹名称，支持“-”连接的多级文件夹结构；例："upload-201712"
	private final Set<String> allowUploadPaths;// 系统允许存储文件的相对basePath的目录集合；例：{"/upload/.*"、"/upload.*"}

	// 本地上传图片相关的配置（uploadType = 1 || uploadType = 3）
	private final String waterPath;// 水印文件地址，例：E:/upload/000000/a.png
	private final Integer waterMinWidth;// 图片宽度达到指定值才添加水印
	private final Integer waterMinHeight;// 图片高度达到指定值才添加水印
	private final Integer imgMaxWidth;// 图片最大宽度
	private final Integer imgWidth;// 图片以固定宽度存储
	private final Integer imgHeight;// 图片以固定高度存储

	// FastDFS上传（uploadType = 2）
	private final Set<String> fastDFSServers;// 服务器地址及端口；例：{"192.168.1.111:22122","192.168.1.119:22122"}
	private final String fastDFSGroup;// FastDFS的group名；可为空
	private final Map<String, Object> fastDFSParam;// 上传附件时附带的参数；可为空
	
	// Aliyun-OSS上传（uploadType = 3）
	private final AliyunOssConfig aliyunOssConfig;
	private final String aliyunOssFolder;

	// 文件格式类型配置
	private final Set<String> suffixAllowArray;// 允许上传的文件后缀；例：{".doc",".html"}
	private final Set<String> suffixUnAllowArray;// 不允许上传的文件后缀；例：{".exe",".jsp"}

	// 保存的文件名称（uploadType = 1 || uploadType = 3）
	private final Boolean fileNameNoChange;// 上传时是否使用源文件名

	private Uploader(Builder builder) {
		request = builder.request;
		uploadType = builder.uploadType;
		basePath = builder.basePath;
		folder = builder.folder;
		allowUploadPaths = builder.allowUploadPaths;
		waterPath = builder.waterPath;
		waterMinWidth = builder.waterMinWidth;
		waterMinHeight = builder.waterMinHeight;
		imgMaxWidth = builder.imgMaxWidth;
		imgWidth = builder.imgWidth;
		imgHeight = builder.imgHeight;
		fastDFSServers = builder.fastDFSServers;
		fastDFSGroup = builder.fastDFSGroup;
		fastDFSParam = builder.fastDFSParam;
		aliyunOssConfig = builder.aliyunOssConfig;
		aliyunOssFolder = builder.aliyunOssFolder;
		suffixAllowArray = builder.suffixAllowArray;
		suffixUnAllowArray = builder.suffixUnAllowArray;
		fileNameNoChange = builder.fileNameNoChange;
	}

	public static class Builder {

		private final HttpServletRequest request;

		protected static final Integer UPLOAD_TYPE_LOCATION = 1;
		protected static final Integer UPLOAD_TYPE_FASTDFS = 2;
		protected static final Integer UPDATE_TYPE_ALIYUN = 3;

		private Integer uploadType = UPLOAD_TYPE_LOCATION;// 上传方式（1：本地上传[默认]；2：FastDFS上传）

		// 本地上传时的目录相关配置（uploadType = 1）
		private String basePath = null;// 文件保存的物理根路径
		private String folder = null;// 文件夹名称，支持“-”连接的多级文件夹结构；例："upload-201712"
		private Set<String> allowUploadPaths = new HashSet<String>();// 系统允许存储文件的相对basePath的目录集合；例：{"/upload/.*"、"/upload.*"}

		// 本地上传图片相关的配置（uploadType = 1）
		private String waterPath = null;// 水印文件相对basePath的地址，例：/upload/000000/a.png
		private Integer waterMinWidth = null;// 图片宽度达到指定值才添加水印
		private Integer waterMinHeight = null;// 图片高度达到指定值才添加水印
		private Integer imgMaxWidth = null;// 图片最大宽度
		private Integer imgWidth = null;// 图片以固定宽度存储
		private Integer imgHeight = null;// 图片以固定高度存储

		// FastDFS上传（uploadType = 2）
		private Set<String> fastDFSServers = new HashSet<String>();// 服务器地址及端口；例：{"192.168.1.111:22122","192.168.1.119:22122"}
		private String fastDFSGroup = null;// FastDFS的group名；可为空
		private Map<String, Object> fastDFSParam = null;// 上传附件时附带的参数；可为空
		
		// Aliyun-OSS上传（uploadType = 3）
		private AliyunOssConfig aliyunOssConfig = null;
		private String aliyunOssFolder = null;

		// 文件格式类型配置
		private Set<String> suffixAllowArray = new HashSet<String>();// 允许上传的文件后缀；例：{".doc",".html"}
		private Set<String> suffixUnAllowArray = new HashSet<String>();// 不允许上传的文件后缀；例：{".exe",".jsp"}

		// 保存的文件名称
		private Boolean fileNameNoChange = null;// 上传时是否使用源文件名

		/**
		 * 构建上传组件
		 * 
		 * @param request
		 */
		public Builder(HttpServletRequest request) {
			this.request = request;
		}

		/**
		 * 保存文件时使用原始的文件名称
		 * 
		 * @return
		 */
		public Builder fileNameNoChange() {
			this.fileNameNoChange = true;
			return this;
		}

		/**
		 * 设置文件保存的物理根路径
		 * <p>
		 * 依赖 commons-io-*.jar
		 * <p>
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
		 * 设置文件夹名称，支持“-”连接的多级文件夹结构
		 * <p>
		 * 例："upload-201712"
		 * 
		 * @param type
		 * @return
		 */
		public Builder folder(String folder) {
			this.folder = folder;
			return this;
		}

		/**
		 * 控制服务器上存储文件的目录（如果能保证folder方法设置的目录安全独立，则无需设置此属性）
		 * 
		 * @param allowUploadPaths
		 *            相对basePath的目录
		 *            <p>
		 *            例："/upload/.*"、"/upload.*"
		 * @return
		 */
		public Builder addAllowUploadPaths(String... allowUploadPaths) {
			if (allowUploadPaths != null && allowUploadPaths.length > 0) {
				for (String a : allowUploadPaths) {
					if (UtilString.isNotBlank(a)) {
						this.allowUploadPaths.add(a);
					}
				}
			}
			return this;
		}

		/**
		 * 设置FastDFS的服务器地址和端口
		 * <p>
		 * 依赖：fastdfs-client-java-20141207.jar
		 * <p>
		 * 
		 * @param servers
		 *            例："192.168.1.111:22122"
		 * @return
		 */
		public Builder fastDFSAddServers(String... servers) {
			this.uploadType = UPLOAD_TYPE_FASTDFS;
			if (servers != null && servers.length > 0) {
				for (String s : servers) {
					if (UtilString.isNotBlank(s)) {
						this.fastDFSServers.add(s);
					}
				}
			}
			return this;
		}

		/**
		 * 设置FastDFS的Group名称
		 * 
		 * @param group
		 * @return
		 */
		public Builder fastDFSGroup(String group) {
			this.fastDFSGroup = group;
			return this;
		}

		/**
		 * 设置FastDFS上传时附带的参数
		 * 
		 * @param param
		 * @return
		 */
		public Builder fastDFSParam(Map<String, Object> param) {
			fastDFSParam = param;
			return this;
		}
		
		public Builder aliyunOssConfig(AliyunOssConfig config) {
			this.uploadType = UPDATE_TYPE_ALIYUN;
			aliyunOssConfig = config;
			return this;
		}
		
		public Builder aliyunOssFolder(String folder) {
			this.aliyunOssFolder = folder;
			return this;
		}

		/**
		 * 添加允许上传的文件后缀
		 * <p>
		 * 如果同时设置了允许上传的后缀和不允许上传的后缀，不允许的配置会被忽略
		 * 
		 * @param allowSuffixs
		 *            允许上传的文件后缀
		 *            <p>
		 *            例：".doc"、".html"
		 * @return
		 */
		public Builder addAllowsSuffixs(String... allowSuffixs) {
			if (allowSuffixs != null && allowSuffixs.length > 0) {
				for (String s : allowSuffixs) {
					if (UtilString.isNotBlank(s)) {
						this.suffixAllowArray.add(s.toLowerCase());
					}
				}
			}
			return this;
		}

		/**
		 * 添加不允许上传的文件后缀
		 * <p>
		 * 如果同时设置了允许上传的后缀和不允许上传的后缀，不允许的配置会被忽略
		 * 
		 * @param unAllowSuffixs
		 *            不允许上传的文件后缀
		 *            <p>
		 *            例：".jsp"、".exe"
		 * @return
		 */
		public Builder addUnAllowsSuffixs(String... unAllowSuffixs) {
			if (unAllowSuffixs != null && unAllowSuffixs.length > 0) {
				for (String s : unAllowSuffixs) {
					if (UtilString.isNotBlank(s)) {
						this.suffixUnAllowArray.add(s.toLowerCase());
					}
				}
			}
			return this;
		}

		/**
		 * 设置图片上传后打上的水印
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param waterPath
		 *            水印图片相对basePath的路径
		 *            <p>
		 *            例：/upload/000000/a.png
		 * @return
		 */
		public Builder waterPath(String waterPath) {
			this.waterPath = waterPath;
			return this;
		}

		/**
		 * 设置上传的图片尺寸达到指定值才添加水印
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param width
		 *            图片宽度达到指定值才添加水印
		 * @param height
		 *            图片高度达到指定值才添加水印
		 * @return
		 */
		public Builder waterMinSize(Integer width, Integer height) {
			this.waterMinWidth = width;
			this.waterMinHeight = height;
			return this;
		}

		/**
		 * 设置图片最大宽度
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param imgMaxWidth
		 * @return
		 */
		public Builder imgMaxWidth(Integer imgMaxWidth) {
			this.imgMaxWidth = imgMaxWidth;
			return this;
		}

		/**
		 * 设置图片最大宽度
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param imgMaxWidth
		 * @return
		 */
		public Builder imgMaxWidth(String imgMaxWidth) {
			if (UtilString.isNotBlank(imgMaxWidth) && imgMaxWidth.matches("[0-9]+")) {
				this.imgMaxWidth = Integer.valueOf(imgMaxWidth);
			}
			return this;
		}

		/**
		 * 设置图片以固定尺寸存储
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param width
		 *            图片以固定宽度存储
		 * @param height
		 *            图片以固定高度存储
		 * @return
		 */
		public Builder imgZoom(Integer width, Integer height) {
			this.imgWidth = width;
			this.imgHeight = height;
			return this;
		}

		/**
		 * 设置图片以固定尺寸存储
		 * <p>
		 * 依赖 commons-io-*.jar、thumbnailator-*.jar
		 * <p>
		 * 
		 * @param width
		 *            图片以固定宽度存储
		 * @param height
		 *            图片以固定高度存储
		 * @return
		 */
		public Builder imgZoom(String width, String height) {
			if (UtilString.isNotBlank(width) && width.matches("[0-9]+")) {
				this.imgWidth = Integer.valueOf(width);
			}
			if (UtilString.isNotBlank(height) && height.matches("[0-9]+")) {
				this.imgHeight = Integer.valueOf(height);
			}
			return this;
		}

		public Uploader build() {
			if (UtilString.isNotBlank(basePath) && !basePath.endsWith(File.separator)) {
				basePath += File.separator;
			}
			if (UtilString.isBlank(folder)) {
				folder = "upload-" + UtilDate.SDF_DATE_MONTH_NOSEP.get().format(new Date());
			}
			if (UtilString.isBlank(aliyunOssFolder)) {
				aliyunOssFolder = "temp";
			}
			if (allowUploadPaths == null || allowUploadPaths.size() == 0) {
				allowUploadPaths = UtilCollection.initHashSet(".*");
			}
			if (suffixAllowArray == null) {
				suffixAllowArray = new HashSet<>();
			}
			if (suffixUnAllowArray == null) {
				suffixUnAllowArray = new HashSet<>();
			}
			if (suffixAllowArray.size() > 0 && suffixUnAllowArray.size() > 0) {
				suffixUnAllowArray.clear();
			}
			if (UtilString.isNotBlank(waterPath) && UtilMedia.isPhoto(FilenameUtils.getExtension(waterPath))) {
				waterPath = basePath + waterPath;
			} else {
				waterPath = null;
			}
			if (fileNameNoChange == null) {
				fileNameNoChange = false;
			}
			return new Uploader(this);
		}
	}

	// 验证后缀是否允许
	private boolean validSuffix(String suffix) {
		if (suffixAllowArray != null && suffixAllowArray.size() > 0) {
			return UtilString.isNotBlank(suffix) && suffixAllowArray.contains("." + suffix);
		}
		if (suffixUnAllowArray != null && suffixUnAllowArray.size() > 0) {
			return UtilString.isBlank(suffix) || !suffixUnAllowArray.contains("." + suffix);
		}
		return true;
	}

	public ServiceResult go() {

		if (uploadType == Builder.UPLOAD_TYPE_LOCATION || uploadType == Builder.UPDATE_TYPE_ALIYUN) {// 验证本地上传时的参数
			if (UtilString.isBlank(basePath)) {
				return ServiceResult.initError("请配置文件存储的根路径");
			}

			boolean allowUpload = false;
			String filePathMatch = "/" + folder.replace("-", "/");
			for (String exc : allowUploadPaths) {
				if (Pattern.matches(exc, filePathMatch)) {
					allowUpload = true;
					break;
				}
			}
			if (!allowUpload) {
				return ServiceResult.initError("文件上传失败：目录" + filePathMatch + "不允许上传文件");
			}
		}
		if (uploadType == Builder.UPLOAD_TYPE_FASTDFS) {// 验证FastDFS上传时的参数
			if (fastDFSServers == null || fastDFSServers.size() == 0) {
				return ServiceResult.initError("请配置FastDFS的服务器地址");
			}
		} else if (uploadType == Builder.UPDATE_TYPE_ALIYUN) {
			if (aliyunOssConfig == null || UtilString.isBlank(aliyunOssConfig.getAccessKeyId()) || UtilString.isBlank(aliyunOssConfig.getAccessSecret())
					|| UtilString.isBlank(aliyunOssConfig.getEndpoint()) || UtilString.isBlank(aliyunOssConfig.getBucket())) {
				return ServiceResult.initError("请配置阿里云OSS参数");
			}
		}

		// 验证是否存在文件
		if (request == null) {
			return ServiceResult.initError("参数错误");
		}
		if (!(request instanceof MultipartHttpServletRequest)) {
			return ServiceResult.initError("请设置正确的上传方式");
		}
		MultipartHttpServletRequest rm = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> mfs = rm.getFileMap();
		if (mfs == null || mfs.size() == 0) {
			return ServiceResult.initError("请确定上传的内容中包含文件域");
		}
		int fileCount = 0;
		for (Map.Entry<String, MultipartFile> entry : mfs.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				fileCount++;
			}
		}
		if (fileCount == 0) {
			return ServiceResult.initError("请选择文件后上传");
		}

		// 本地上传时的目录地址
		String absolutePath = null;
		if (uploadType == Builder.UPLOAD_TYPE_LOCATION || uploadType == Builder.UPDATE_TYPE_ALIYUN) {
			absolutePath = basePath + folder.replace("-", File.separator) + File.separator;
			File dir = new File(absolutePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}

		if (fileCount == 1) {
			for (Map.Entry<String, MultipartFile> entry : mfs.entrySet()) {
				MultipartFile file = entry.getValue();
				if (!file.isEmpty()) {
					String suffix = FilenameUtils.getExtension(file.getOriginalFilename());// 文件后缀
					if (UtilString.isBlank(suffix)) {
						suffix = "tmp";
					}
					if (!validSuffix(suffix)) {
						return ServiceResult.initError("文件上传失败：不允许上传" + suffix + "格式的文件");
					}
					try {
						if (uploadType == Builder.UPLOAD_TYPE_LOCATION) {
							String newFilename = null;// 保存后的文件名
							if (fileNameNoChange) {
								newFilename = file.getOriginalFilename();
							} else {
								newFilename = UtilString.getSoleCode() + "." + suffix;
							}
							UtilFile.copyInputStreamToFile(file.getInputStream(), new File(absolutePath + newFilename));
							if (UtilMedia.isPhoto(suffix) && UtilString.isNotBlank(waterPath) || imgMaxWidth != null
									|| imgWidth != null || imgHeight != null) {
								new ImageBuilder().srcPath(absolutePath + newFilename).waterPath(waterPath)
										.waterMinHeight(waterMinHeight).waterMinWidth(waterMinWidth)
										.maxWidth(imgMaxWidth).width(imgWidth).height(imgHeight).go();
							}
							return ServiceResult.initSuccess(new UploadSuccessResult(file.getOriginalFilename(), file
									.getSize(), file.getContentType(), entry.getKey(), suffix, newFilename, "/"
									+ folder.replace("-", "/") + "/" + newFilename));
						} else if (uploadType == Builder.UPLOAD_TYPE_FASTDFS) {
							String fileId = FastdfsManager.getInstance(fastDFSServers.toArray(new String[] {}))
									.uploadFile(file.getBytes(), suffix, fastDFSParam, fastDFSGroup);
							return ServiceResult.initSuccess(new UploadSuccessResult(file.getOriginalFilename(), file
									.getSize(), file.getContentType(), entry.getKey(), suffix, FilenameUtils
									.getName(fileId), fileId));
						} else if (uploadType == Builder.UPDATE_TYPE_ALIYUN) {
							String newFilename = null;// 保存后的文件名
							if (fileNameNoChange) {
								newFilename = file.getOriginalFilename();
							} else {
								newFilename = UtilString.getSoleCode() + "." + suffix;
							}
							UtilFile.copyInputStreamToFile(file.getInputStream(), new File(absolutePath + newFilename));
							if (UtilMedia.isPhoto(suffix) && UtilString.isNotBlank(waterPath) || imgMaxWidth != null
									|| imgWidth != null || imgHeight != null) {
								new ImageBuilder().srcPath(absolutePath + newFilename).waterPath(waterPath)
										.waterMinHeight(waterMinHeight).waterMinWidth(waterMinWidth)
										.maxWidth(imgMaxWidth).width(imgWidth).height(imgHeight).go();
							}
							String fileUrl = "http://" + aliyunOssConfig.getBucket() + "." + aliyunOssConfig.getEndpoint() 
								+ "/" + aliyunOssFolder.replace("-", "/") + "/" + newFilename;
							AliyunOssUtil.uploadImgAliyun(aliyunOssConfig, aliyunOssFolder.replace("-", "/") + "/",
									new File(absolutePath + newFilename), newFilename);
							UtilFile.deleteFile(absolutePath + newFilename);
							return ServiceResult.initSuccess(new UploadSuccessResult(file.getOriginalFilename(), file.getSize(), 
									file.getContentType(), entry.getKey(), suffix, newFilename, fileUrl));
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						return ServiceResult.initError("文件上传失败");
					}
				}
			}
			return ServiceResult.initError("读取文件失败");
		} else {
			UploadResult uploadResult = new UploadResult();
			for (Map.Entry<String, MultipartFile> entry : mfs.entrySet()) {
				MultipartFile file = entry.getValue();
				if (!file.isEmpty()) {
					String suffix = FilenameUtils.getExtension(file.getOriginalFilename());// 文件后缀
					if (UtilString.isBlank(suffix)) {
						suffix = "tmp";
					}
					if (!validSuffix(suffix)) {
						uploadResult.getError().add(
								new UploadErrorResult(file.getOriginalFilename(), file.getSize(), entry.getKey(), file
										.getContentType(), FilenameUtils.getExtension(file.getOriginalFilename()),
										"文件上传失败：不允许上传" + suffix + "格式的文件"));
					} else {
						try {
							if (uploadType == Builder.UPLOAD_TYPE_LOCATION) {
								String newFilename = null;// 保存后的文件名
								if (fileNameNoChange) {
									newFilename = file.getOriginalFilename();
								} else {
									newFilename = UtilString.getSoleCode() + "." + suffix;
								}
								UtilFile.copyInputStreamToFile(file.getInputStream(), new File(absolutePath
										+ newFilename));
								if (UtilMedia.isPhoto(suffix) && UtilString.isNotBlank(waterPath)
										|| imgMaxWidth != null || imgWidth != null || imgHeight != null) {
									new ImageBuilder().srcPath(absolutePath + newFilename).waterPath(waterPath)
											.waterMinHeight(waterMinHeight).waterMinWidth(waterMinWidth)
											.maxWidth(imgMaxWidth).width(imgWidth).height(imgHeight).go();
								}
								uploadResult.getSuccess().add(
										(new UploadSuccessResult(file.getOriginalFilename(), file.getSize(), file
												.getContentType(), entry.getKey(), suffix, newFilename, "/"
												+ folder.replace("-", "/") + "/" + newFilename)));
							} else if (uploadType == Builder.UPLOAD_TYPE_FASTDFS) {
								String fileId = FastdfsManager.getInstance(fastDFSServers.toArray(new String[] {}))
										.uploadFile(file.getBytes(), suffix, fastDFSParam, fastDFSGroup);
								uploadResult.getSuccess().add(
										(new UploadSuccessResult(file.getOriginalFilename(), file.getSize(), file
												.getContentType(), entry.getKey(), suffix, FilenameUtils
												.getName(fileId), fileId)));
							} else if (uploadType == Builder.UPDATE_TYPE_ALIYUN) {
								String newFilename = null;// 保存后的文件名
								if (fileNameNoChange) {
									newFilename = file.getOriginalFilename();
								} else {
									newFilename = UtilString.getSoleCode() + "." + suffix;
								}
								UtilFile.copyInputStreamToFile(file.getInputStream(), new File(absolutePath + newFilename));
								if (UtilMedia.isPhoto(suffix) && UtilString.isNotBlank(waterPath) || imgMaxWidth != null
										|| imgWidth != null || imgHeight != null) {
									new ImageBuilder().srcPath(absolutePath + newFilename).waterPath(waterPath)
											.waterMinHeight(waterMinHeight).waterMinWidth(waterMinWidth)
											.maxWidth(imgMaxWidth).width(imgWidth).height(imgHeight).go();
								}
								String fileUrl = "http://" + aliyunOssConfig.getBucket() + "." + aliyunOssConfig.getEndpoint() 
									+ "/" + aliyunOssFolder.replace("-", "/") + "/" + newFilename;
								AliyunOssUtil.uploadImgAliyun(aliyunOssConfig, aliyunOssFolder.replace("-", "/") + "/", 
										new File(absolutePath + newFilename), newFilename);
								UtilFile.deleteFile(absolutePath + newFilename);
								uploadResult.getSuccess().add(new UploadSuccessResult(file.getOriginalFilename(), file.getSize(), 
										file.getContentType(), entry.getKey(), suffix, newFilename, fileUrl));
							}
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							uploadResult.getError().add(
									new UploadErrorResult(file.getOriginalFilename(), file.getSize(), entry.getKey(),
											file.getContentType(), FilenameUtils.getExtension(file
													.getOriginalFilename()), "文件上传失败"));
						}
					}
				}
			}
			return ServiceResult.initSuccess(uploadResult);
		}
	}

}
