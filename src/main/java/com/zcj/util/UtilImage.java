package com.zcj.util;

import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.filenameutils.FilenameUtils;

//<dependency>
//	<groupId>net.coobird</groupId>
//	<artifactId>thumbnailator</artifactId>
//	<version>0.4.8</version>
//</dependency>
public class UtilImage {

	public static class ImageBuilder {

		private static final Logger logger = LoggerFactory.getLogger(ImageBuilder.class);

		private String srcPath;
		private String waterPath;
		private Integer waterMinWidth;// 图片宽度达到指定值才添加水印
		private Integer waterMinHeight;// 图片高度达到指定值才添加水印
		private String outPath;
		private String outFormat;
		private Integer width;// 设置目标宽度
		private Integer height;// 设置目标高度
		private Integer maxWidth;// 设置目标最大宽度

		public boolean go() {
			if (UtilString.isBlank(srcPath)) {
				return false;
			}
			try {
				Builder<?> builder = Thumbnails.of(srcPath);

				// 原始图片的宽和高（有必要时才获取）
				Integer[] srcSize = new Integer[] { null, null };
				if (maxWidth != null
						|| (UtilString.isNotBlank(waterPath) && (waterMinWidth != null || waterMinHeight != null))) {
					srcSize = UtilImageBase.getWidthAndHeight(srcPath);
				}

				// 根据最大宽度设置目标宽度
				if (maxWidth != null) {
					Integer srcWidth = srcSize[0];
					if (srcWidth == null || srcWidth <= 0 || srcWidth > maxWidth) {
						width = maxWidth;
					}
				}

				// 根据水印条件设置水印图片
				if (UtilString.isNotBlank(waterPath) && (waterMinWidth != null || waterMinHeight != null)) {
					if (width != null && waterMinWidth != null && width < waterMinWidth) {// 目标宽度达不到水印要求
						waterPath = null;
					} else if (width == null && srcSize[0] != null && waterMinWidth != null
							&& srcSize[0] < waterMinWidth) {// 原始宽度达不到水印要求
						waterPath = null;
					} else if (height != null && waterMinHeight != null && height < waterMinHeight) {// 目标高度打不打水印要求
						waterPath = null;
					} else if (height == null && srcSize[1] != null && waterMinHeight != null
							&& srcSize[1] < waterMinHeight) {// 原始高度达不到水印要求
						waterPath = null;
					}
				}

				if (width == null && height == null) {
					builder.scale(1);
				} else if (width == null) {
					builder.height(height);
				} else if (height == null) {
					builder.width(width);
				} else {
					builder.size(width, height);
				}
				if (UtilString.isNotBlank(outFormat)) {
					builder.outputFormat(outFormat);
				}
				if (UtilString.isNotBlank(waterPath)) {
					builder.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(waterPath)), 1f);
				}
				if (UtilString.isBlank(outPath)) {
					builder.toFiles(Rename.NO_CHANGE);
				} else {
					File newFile = new File(FilenameUtils.getFullPath(outPath));
					if (!newFile.exists()) {
						newFile.mkdirs();
					}
					builder.toFile(outPath);
				}
				return true;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}

		/**
		 * 设置源图片地址，例：E://1.png
		 * 
		 * @param srcPath
		 * @return
		 */
		public ImageBuilder srcPath(String srcPath) {
			this.srcPath = srcPath;
			return this;
		}

		/**
		 * 设置水印地址，例：E://1.png
		 * 
		 * @param waterPath
		 * @return
		 */
		public ImageBuilder waterPath(String waterPath) {
			this.waterPath = waterPath;
			return this;
		}

		/**
		 * 图片宽度达到指定值才添加水印
		 * 
		 * @param waterMinWidth
		 * @return
		 */
		public ImageBuilder waterMinWidth(Integer waterMinWidth) {
			this.waterMinWidth = waterMinWidth;
			return this;
		}

		/**
		 * 图片高度达到指定值才添加水印
		 * 
		 * @param waterMinHeight
		 * @return
		 */
		public ImageBuilder waterMinHeight(Integer waterMinHeight) {
			this.waterMinHeight = waterMinHeight;
			return this;
		}

		/**
		 * 设置处理后保存的路径，例：E://2.png；NULL 表示覆盖源文件
		 * 
		 * @param outPath
		 * @return
		 */
		public ImageBuilder outPath(String outPath) {
			this.outPath = outPath;
			return this;
		}

		/**
		 * 设置压缩格式；NULL 表示按原格式压缩；例：jpg
		 * 
		 * @param outFormat
		 * @return
		 */
		public ImageBuilder outFormat(String outFormat) {
			this.outFormat = outFormat;
			return this;
		}

		/**
		 * 设置目标宽度；NULL 表示按目标高度等比缩放
		 * 
		 * @param width
		 * @return
		 */
		public ImageBuilder width(Integer width) {
			this.width = width;
			return this;
		}

		/**
		 * 设置目标高度；NULL 表示按目标宽度等比缩放
		 * 
		 * @param height
		 * @return
		 */
		public ImageBuilder height(Integer height) {
			this.height = height;
			return this;
		}

		/**
		 * 设置目标最大宽度
		 * 
		 * @param maxWidth
		 * @return
		 */
		public ImageBuilder maxWidth(Integer maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}
	}

	/**
	 * 缩放图片<br>
	 * 依赖JAR包：thumbnailator-0.4.8.jar<br>
	 * 已废弃，用new ImageBuilder().xxx().go()替代
	 * 
	 * @param filePath
	 *            源图片地址，例：E://1.png
	 * @param width
	 *            目标宽度；NULL 表示按目标高度等比缩放
	 * @param height
	 *            目标高度；NULL 表示按目标宽度等比缩放
	 * @param newFilePath
	 *            缩放后保存的路径，例：E://2.png；NULL 表示覆盖源文件
	 * @return
	 */
	@Deprecated
	public static boolean resize(String filePath, Integer width, Integer height, String newFilePath) {
		return new ImageBuilder().srcPath(filePath).width(width).height(height).outPath(newFilePath).go();
	}

	/**
	 * 缩放图片<br>
	 * 依赖JAR包：thumbnailator-0.4.8.jar<br>
	 * 已废弃，用new ImageBuilder().xxx().go()替代
	 * 
	 * @param filePath
	 *            源图片地址，例：E://1.png
	 * @param width
	 *            目标宽度；NULL 表示按目标高度等比缩放
	 * @param height
	 *            目标高度；NULL 表示按目标宽度等比缩放
	 * @param newFilePath
	 *            缩放后保存的路径，例：E://2.png；NULL 表示覆盖源文件
	 * @param outputFormat
	 *            压缩格式；NULL 表示按原格式压缩；例：jpg
	 * @return
	 */
	@Deprecated
	public static boolean resize(String filePath, Integer width, Integer height, String newFilePath, String outputFormat) {
		return new ImageBuilder().srcPath(filePath).width(width).height(height).outPath(newFilePath)
				.outFormat(outputFormat).go();
	}

}
