package com.zcj.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密类
 * 
 * @author ZCJ
 * @data 2013-4-9
 */
public class UtilEncryption {

	private static final Logger logger = LoggerFactory.getLogger(UtilEncryption.class);

	private final static String ALGORITHM = "MD5";

	private final static char HEXDIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public final static String toMD5(String s) {
		if (UtilString.isBlank(s)) {
			return s;
		}
		byte[] strArray = s.getBytes();
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("加密出错");
		}
		messageDigest.update(strArray);
		byte[] md = messageDigest.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = HEXDIGITS[byte0 >>> 4 & 0xf];
			str[k++] = HEXDIGITS[byte0 & 0xf];
		}
		return new String(str);
	}

	// public static void main(String args[]) {
	// System.out.println(UtilEncryption.toMD5("123456"));
	// }
}