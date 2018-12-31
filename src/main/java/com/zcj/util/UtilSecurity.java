package com.zcj.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES双向加密
 * 
 * @author ZCJ
 * @data 2013-6-9
 */
@SuppressWarnings("restriction")
public class UtilSecurity {

	private static final String Algorithm = "DES/CBC/PKCS5Padding";
	private static final byte[] bytes = { '!', '(', '*', '*', '!', '!', ')', '%' };
	private static final byte[] key = { 'z', 'o', 'u', 'c', 'h', 'o', 'n', 'g' };
	public static SecretKey deskey = new SecretKeySpec(key, "DES");
	public static IvParameterSpec ivSpec = new IvParameterSpec(bytes);

	private UtilSecurity() {
	}

	/**
	 * 加密
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static String encryptMessage(String s) throws Exception {
		if (s == null)
			return null;
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey, ivSpec);
		byte[] cipherByte = c1.doFinal(s.getBytes());
		BASE64Encoder base64encoder = new BASE64Encoder();
		return base64encoder.encode(cipherByte);
	}

	/**
	 * 解密
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static String decryptMessage(String s) throws Exception {
		if (s == null)
			return null;
		BASE64Decoder base64decoder = new BASE64Decoder();
		byte cipherByte[] = base64decoder.decodeBuffer(s);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey, ivSpec);
		byte[] clearByte = c1.doFinal(cipherByte);
		String rs = new String(clearByte);
		return rs;
	}

	// public static void main(String[] args) {
	// try {
	// String pwd = UtilSecurity.encryptMessage("2");
	// System.out.println(pwd);
	// String rs = UtilSecurity.decryptMessage("Jbk1T6deXUI=");
	// System.out.println(rs);
	// } catch (Exception e) {
	// }
	// }
}
