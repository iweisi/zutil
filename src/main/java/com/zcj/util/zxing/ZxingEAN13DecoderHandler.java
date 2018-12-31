package com.zcj.util.zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ZxingEAN13DecoderHandler {

	private static final Logger logger = LoggerFactory.getLogger(ZxingEAN13DecoderHandler.class);

	public static String decode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (NotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
