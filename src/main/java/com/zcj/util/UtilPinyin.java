package com.zcj.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汉语转拼音工具类 需导入pinyin4j-2.5.0.jar包
 * 
 * @author ZCJ
 * @data 2012-9-10
 */
public class UtilPinyin {

	private static final Logger logger = LoggerFactory.getLogger(UtilPinyin.class);

	// public static void main(String[] args) {
	// System.out.println(UtilPinyin.cn2SpellFirst("北京市"));
	// }

	public static String cn2SpellFirst(String chinese) {
		if (chinese == null) {
			return null;
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不标声调
		format.setVCharType(HanyuPinyinVCharType.WITH_V);// u的声母替换为v
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < chinese.length(); i++) {
				char a = chinese.charAt(i);
				if (a > 128) {
					String[] array = PinyinHelper.toHanyuPinyinStringArray(a, format);
					if (array == null || array.length == 0) {
						continue;
					}
					String s = array[0];
					sb.append(s.charAt(0));
				} else {
					sb.append(a);
				}
			}
			String result = sb.toString();
			return result.toUpperCase();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String cn2Spell(String chinese) {
		if (chinese == null) {
			return null;
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不标声调
		format.setVCharType(HanyuPinyinVCharType.WITH_V);// u的声母替换为v
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < chinese.length(); i++) {
				char a = chinese.charAt(i);
				if (a > 128) {
					String[] array = PinyinHelper.toHanyuPinyinStringArray(a, format);
					if (array == null || array.length == 0) {
						continue;
					}
					String s = array[0];
					sb.append(s);
				} else {
					sb.append(a);
				}
			}
			String result = sb.toString();
			result = String.valueOf(result.charAt(0)).toUpperCase().concat(result.substring(1));
			return result;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
