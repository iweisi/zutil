package com.zcj.util.zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

//<dependency>
//	<groupId>com.google.zxing</groupId>
//	<artifactId>core</artifactId>
//	<version>3.0.0</version>
//</dependency>
//<dependency>
//	<groupId>com.google.zxing</groupId>
//	<artifactId>javase</artifactId>
//	<version>3.0.0</version>
//</dependency>
public class UtilZxing {

	private static final Logger logger = LoggerFactory.getLogger(UtilZxing.class);

	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            文本内容
	 * @param filePath
	 *            二维码图片存储的文件夹路径；例："E:/"
	 * @param fileName
	 *            二维码文件名；例："a.png"
	 * @param width
	 *            二维码图片的宽度
	 * @param height
	 *            二维码图片的高度
	 * @return 返回true表示生成成功；返回false表示生成失败
	 */
	public static boolean toQrCodeImg(String content, String filePath, String fileName, int width, int height) {
		try {
			String format = "png";// 图像类型
			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
			Path path = FileSystems.getDefault().getPath(filePath, fileName);
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 解析二维码
	 * 
	 * @param filePath
	 *            二维码图片的物理路径
	 * @return 返回NULL表示解析失败
	 */
	public static String fromQrCodeImg(String filePath) {
		try {
			BufferedImage image = ImageIO.read(new File(filePath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
			return result.getText();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 生成条码
	 * 
	 * @param contents
	 *            文本内容
	 * @param width
	 *            条码图片的宽度
	 * @param height
	 *            条码图片的高度
	 * @param path
	 *            条码保存的物理路径
	 * @return
	 */
	public static boolean toBarCodeImg(String contents, int width, int height, String path) {
		return ZxingEAN13EncoderHandler.encode(contents, width, height, path);
	}

	/**
	 * 生成条码
	 * 
	 * @param contents
	 *            文本内容
	 * @param width
	 *            条码图片的宽度
	 * @param height
	 *            条码图片的高度
	 * @param stream
	 *            输出流
	 * @return
	 */
	public static boolean toBarCodeImg(String contents, int width, int height, OutputStream stream) {
		return ZxingEAN13EncoderHandler.encode(contents, width, height, stream);
	}

	/**
	 * 解析条码
	 * 
	 * @param imgPath
	 *            条码文件物理路径
	 * @return 返回NULL表示解析失败
	 */
	public static String fromBarCodeImg(String imgPath) {
		return ZxingEAN13DecoderHandler.decode(imgPath);
	}

}
