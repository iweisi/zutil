package com.zcj.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.zcj.util.filenameutils.FilenameUtils;

/**
 * 文件相关操作（图片截图、压缩图片、下载文件、保存文件、ZIP打包、删除文件和文件夹、获取MIME类型、文件名操作）<br>
 * 
 * 依赖：commons-io-*.jar
 * 
 * @author zouchongjin@sina.com
 * @data 2015年11月19日
 */
@SuppressWarnings("restriction")
public class UtilFile {

	private static final Logger logger = LoggerFactory.getLogger(UtilFile.class);

	/** 图片文件类型数组 */
	public static final String[] imageFileType = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	/** 视频文件类型数组 */
	public static final String[] videoFileType = { ".swf", ".wmv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg", ".ogg",
			".mov", ".wmv", ".mp4" };
	/** 附件类型数组 */
	public static final String[] fileType = { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".avi",
			".rm", ".rmvb", ".mpeg", ".mpg", ".ogg", ".mov", ".wmv", ".mp4" };

	/** 可能影响服务器安全的文件后缀 */
	public static final String[] dangerFileTpye = { ".com", ".exe", ".bat", ".lnk", ".jsp", ".html", ".dll", ".drv",
			".sys", ".pif" };

	public static Map<String, String> MIMETYPE_MAP = new HashMap<String, String>();

	static {
		MIMETYPE_MAP.put("doc", "application/msword");
		MIMETYPE_MAP.put("js", "application/x-javascript");
		MIMETYPE_MAP.put("css", "text/css");
		MIMETYPE_MAP.put("png", "image/png");
		MIMETYPE_MAP.put("apk", "application/vnd.android.package-archive");
	}

	/** 根据路径创建文件夹 */
	public static boolean createDirectory(String path) {
		if (UtilString.isBlank(path)) {
			return false;
		}
		String absolutePath = FilenameUtils.getFullPath(path);
		File dir = new File(absolutePath);
		if (!dir.exists()) {
			return dir.mkdirs();
		}
		return true;
	}

	/** 通过 InputStream 保存文件（依赖 commons-io-*.jar） */
	public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
		FileUtils.copyInputStreamToFile(source, destination);
	}

	/** 修改文件后缀 */
	public static String replaceSuffix(String path, String newSuffix) {
		if (UtilString.isNotBlank(path) && UtilString.isNotBlank(newSuffix)) {
			String oldSuffix = FilenameUtils.getExtension(path);
			if (UtilString.isNotBlank(oldSuffix)) {
				return path.substring(0, path.length() - oldSuffix.length()) + newSuffix;
			}
		}
		return path;
	}

	/**
	 * 截图
	 * 
	 * @param srcPath
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 */
	public static void cut(String srcPath, int startX, int startY, int width, int height) {
		try {
			BufferedImage bufImg = ImageIO.read(new File(srcPath));
			BufferedImage subImg = bufImg.getSubimage(startX, startY, width, height);
			ImageIO.write(subImg, FilenameUtils.getExtension(srcPath), new File(srcPath));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 按最大长宽压缩，不变形，补白
	 * 
	 * @param fi
	 * @param w
	 * @param h
	 */
	public static void doCompressByWhite(String filePath, int width, int height) {
		Image src;
		try {
			src = javax.imageio.ImageIO.read(new File(filePath)); // 构造Image对象

			int old_w = src.getWidth(null); // 得到源图宽
			int old_h = src.getHeight(null);
			int new_w = 0;
			int new_h = 0; // 得到源图长

			double w2 = (old_w * 1.00) / (width * 1.00);
			double h2 = (old_h * 1.00) / (height * 1.00);

			// 图片跟据长宽留白，成一个正方形图。
			BufferedImage oldpic;
			if (old_w > old_h) {
				oldpic = new BufferedImage(old_w, old_w, BufferedImage.TYPE_INT_RGB);
			} else {
				if (old_w < old_h) {
					oldpic = new BufferedImage(old_h, old_h, BufferedImage.TYPE_INT_RGB);
				} else {
					oldpic = new BufferedImage(old_w, old_h, BufferedImage.TYPE_INT_RGB);
				}
			}
			Graphics2D g = oldpic.createGraphics();
			g.setColor(Color.white);
			if (old_w > old_h) {
				g.fillRect(0, 0, old_w, old_w);
				g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h, Color.white, null);
			} else {
				if (old_w < old_h) {
					g.fillRect(0, 0, old_h, old_h);
					g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h, Color.white, null);
				} else {
					// g.fillRect(0,0,old_h,old_h);
					g.drawImage(src.getScaledInstance(old_w, old_h, Image.SCALE_SMOOTH), 0, 0, null);
				}
			}
			g.dispose();
			src = oldpic;
			// 图片调整为方形结束
			if (old_w > width)
				new_w = (int) Math.round(old_w / w2);
			else
				new_w = old_w;
			if (old_h > height)
				new_h = (int) Math.round(old_h / h2);// 计算新图长宽
			else
				new_h = old_h;
			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			// tag.getGraphics().drawImage(src,0,0,new_w,new_h,null); //绘制缩小后的图
			tag.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream newimage = new FileOutputStream(filePath); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/* 压缩质量 */
			jep.setQuality(1, true);
			encoder.encode(tag, jep);
			// encoder.encode(tag); //近JPEG编码
			newimage.close();
		} catch (IOException ex) {

		}
	}

	/**
	 * 下载文件（下载到项目运行环境的硬盘里）
	 * 
	 * @param imgUrl
	 *            图片地址，如http://www.baidu.com/a.jpg
	 * @param path
	 *            保存的地址，如C:\\ABC.jpg
	 * @throws IOException
	 */
	public static void download(String fileUrl, String path) throws IOException {
		File file = new File(path);
		URL url = new URL(fileUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		DataInputStream in = new DataInputStream(conn.getInputStream());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		byte[] buffer = new byte[1024 * 4];
		int num = 0;
		while ((num = in.read(buffer)) != -1) {
			out.write(buffer, 0, num);
		}
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 打包文件夹
	 * 
	 * @param inputFileName
	 *            源文件夹目录 如D:\\TEMP\\ABC
	 * @param zipFileName
	 *            保存压缩文件的目录 如C:\\ABC.zip
	 * @throws Exception
	 */
	public static void zip(String inputFileName, String zipFileName) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		File f = new File(inputFileName);
		zip(out, f, FilenameUtils.getBaseName(zipFileName));
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	/**
	 * 解压（文件和文件夹的名称不能为中文）
	 * 
	 * @param zipFilePath
	 *            源压缩包文件路径，如 C:\\ABC.zip
	 * @param outputFilePath
	 *            解压到指定的目录，如 C:\\ABC
	 * @throws Exception
	 */
	public static void unZip(String zipFilePath, String outputFilePath) throws Exception {
		ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));
		BufferedInputStream bin = new BufferedInputStream(zin);
		File Fout = null;
		ZipEntry entry;
		while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
			Fout = new File(outputFilePath, entry.getName());
			if (!Fout.exists()) {
				(new File(Fout.getParent())).mkdirs();
			}
			FileOutputStream out = new FileOutputStream(Fout);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			int b;
			while ((b = bin.read()) != -1) {
				bout.write(b);
			}
			bout.close();
			out.close();
		}
		bin.close();
		zin.close();
	}

	/**
	 * 删除文件夹或文件
	 * 
	 * @param sPath
	 *            绝对路径
	 * @return
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (!file.exists()) {
			return flag;
		} else {
			if (file.isFile()) {
				return deleteFile(sPath);
			} else {
				return deleteDirectory(sPath);
			}
		}
	}

	private static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static File getFileByBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return file;
	}

	/**
	 * 获取文件的MIME类型
	 * 
	 * @param filename
	 *            带后缀的文件名。支持：foo.txt、a/b/c.jpg等格式
	 * @return
	 */
	public static String getMimeType(String filename) {
		if (UtilString.isNotBlank(filename)) {
			String ext = FilenameUtils.getExtension(filename);
			if (UtilString.isNotBlank(ext)) {
				ext = ext.toLowerCase();
				return MIMETYPE_MAP.get(ext);
			}
		}
		return null;
	}

	/**
	 * 压缩图片方法
	 * 
	 * @deprecated
	 * 
	 * @param oldFile
	 *            将要压缩的图片的物理地址
	 * @param width
	 *            压缩宽
	 * @param height
	 *            压缩高
	 * @param quality
	 *            压缩清晰度 <b>建议为1.0</b>
	 * @param smallIcon
	 *            压缩图片后,添加的扩展名（在图片后缀名前添加）
	 * @param percentage
	 *            是否等比压缩 若true宽高比率将将自动调整
	 * @return 如果处理正确返回压缩后的文件名 null则参数可能有误
	 */
	public static String doCompress(String oldFile, int width, int height, float quality, String smallIcon,
			boolean percentage) {
		if (oldFile != null && width > 0 && height > 0) {
			Image srcFile = null;
			String newImage = null;
			try {
				File file = new File(oldFile);
				// 文件不存在
				if (!file.exists()) {
					return null;
				}
				/* 读取图片信息 */
				srcFile = ImageIO.read(file);
				int new_w = width;
				int new_h = height;
				if (percentage) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) srcFile.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) srcFile.getHeight(null)) / (double) height + 0.1;
					double rate = rate1 > rate2 ? rate1 : rate2;
					new_w = (int) (((double) srcFile.getWidth(null)) / rate);
					new_h = (int) (((double) srcFile.getHeight(null)) / rate);
				}
				/* 宽高设定 */
				BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);
				/* 压缩后的文件名 */
				String filePrex = oldFile.substring(0, oldFile.lastIndexOf('.'));
				newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
				/* 压缩之后临时存放位置 */
				FileOutputStream out = new FileOutputStream(newImage);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
				/* 压缩质量 */
				jep.setQuality(quality, true);
				encoder.encode(tag, jep);
				out.close();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				srcFile.flush();
			}
			return newImage;
		} else {
			return null;
		}
	}

}
