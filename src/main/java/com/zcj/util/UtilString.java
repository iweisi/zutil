package com.zcj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.random.IdWorker;

public class UtilString {

	private static Logger logger = LoggerFactory.getLogger(UtilString.class);

	public static final IdWorker WORKER = new IdWorker(8);

	public static boolean isNotBlank(String str) {
		return !UtilString.isBlank(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/** 相对路径转成可访问的完整URL路径 */
	public static String urlFill(String url, String website) {
		return UtilUri.urlFill(url, website);
	}

	/** 给URL添加参数 */
	public static String urlAddParam(String url, String key, String val) {
		return UtilUri.urlAddParam(url, key, val);
	}

	/** 32 进制字符串转成 10 进制数字 */
	public static Long unZipLong(String val) {
		return Long.parseLong(val, 32);
	}

	/** Long 转成 32 进制的字符串，缩短长度 */
	public static String zipLong(Long val) {
		return Long.toString(val, 32);
	}

	/** 获取一个15位的唯一标识 */
	public static Long getLongUUID() {
		return WORKER.nextId();
	}

	/** 通过MD5加密 */
	public static String getMd5(String str) {
		return UtilEncryption.toMD5(str);
	}

	/** 初始化一个32位的UUID */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/** 根据当前日期生成20位的唯一编码 */
	public static String getSoleCode() {
		return UtilString.getSoleCode(new Date());
	}

	/** 根据指定日期生成20位的唯一编码 */
	public static String getSoleCode(Date date) {
		SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = from.format(date);
		int tmp = UtilRandom.getRandom(100000, 999999);
		return time + tmp;
	}

	/** 提取字符串中的数字 */
	public static String getNumeric(String value) {
		if (isBlank(value)) {
			return "";
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(value);
		return m.replaceAll("").trim();
	}

	/** 验证车牌号（字母大小写和左右空格不影响验证） */
	public static boolean isCarNumber(String value) {
		if (isBlank(value)) {
			return false;
		}
		value = value.trim().toUpperCase();
		String regEx = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
		Pattern pattern = Pattern.compile(regEx);
		return pattern.matcher(value).matches();
	}

	/** 验证中文 */
	public static boolean isChinese(String value) {
		if (isBlank(value)) {
			return false;
		}
		String regEx = "^[\u4e00-\u9fa5]+$";
		Pattern pattern = Pattern.compile(regEx);
		return pattern.matcher(value).matches();
	}

	/** 验证包含中文 */
	public static boolean isContainChinese(String str) {
		if (isBlank(str)) {
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/** 验证身份证号 */
	public static boolean isIdcard(String idcard, boolean onlyEighteen) {
		return ValidatorIdcard.isValidatedAllIdcard(idcard, onlyEighteen);
	}

	/** 验证身份证号，支持15位的 */
	public static boolean isIdcard(String idcard) {
		return ValidatorIdcard.isValidatedAllIdcard(idcard);
	}

	/** 根据身份证号码获取生日 */
	public static Date getBirthdayByIdcard(String idcard) {
		if (isIdcard(idcard, true)) {
			try {
				return UtilDate.SDF_DATE_NOSEP.get().parse(idcard.substring(6, 14));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}
		return null;
	}

	/** 根据生日获取年龄 */
	public static Integer getAgeByBirthday(Date birthDay) {
		if (birthDay == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			logger.warn("获取年龄失败：身份证号内的生日在当前时间之后");
			return null;
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}

	/** 根据身份证获取年龄 */
	public static Integer getAgeByIdcard(String idcard) {
		Date birthDay = getBirthdayByIdcard(idcard);
		return getAgeByBirthday(birthDay);
	}

	/** 验证是否为IP地址 */
	public static boolean isIp(String ip) {
		return ValidatorIP.isIp(ip);
	}

	/** 验证ip是否在ipMap(key~value)区间中 */
	public static boolean isIpInner(String ip, Map<String, String> ipMap) {
		return ValidatorIP.isIPInner(ip, ipMap);
	}

	/** 验证是否为邮箱地址 */
	public static boolean isEmail(String email) {
		if (isBlank(email)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(email).matches();
	}

	/** 验证是否为MAC地址（支持“:”和“-”分隔符） */
	public static boolean isMAC(String mac) {
		if (isBlank(mac)) {
			return false;
		}
		String regEx = "^([0-9a-fA-F]{2})(([/\\s:-][0-9a-fA-F]{2}){5})$";
		String regEx2 = "^([0-9a-fA-F]{2})(([0-9a-fA-F]{2}){5})$";
		return Pattern.compile(regEx).matcher(mac).matches() || Pattern.compile(regEx2).matcher(mac).matches();
	}

	/** 验证是否为手机号码 */
	public static boolean isPhone(String phone) {
		try {
			if (!phone.substring(0, 1).equals("1")) {
				return false;
			}
			if (phone == null || phone.length() != 11) {
				return false;
			}
			String check = "^[0123456789]+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(phone);
			boolean isMatched = matcher.matches();
			if (isMatched) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/** 验证是否为中国移动手机号码 */
	public static boolean isPhoneMobile(String code) {
		if (isPhone(code)) {
			if (code.startsWith("134") || code.startsWith("135") || code.startsWith("136") || code.startsWith("137")
					|| code.startsWith("138") || code.startsWith("139") || code.startsWith("147")
					|| code.startsWith("150") || code.startsWith("151") || code.startsWith("152")
					|| code.startsWith("157") || code.startsWith("158") || code.startsWith("159")
					|| code.startsWith("178") || code.startsWith("182") || code.startsWith("183")
					|| code.startsWith("184") || code.startsWith("187") || code.startsWith("188")) {
				return true;
			}
		}
		return false;
	}

	/** 隐藏手机号码的中间四位 */
	public static String hidePhone(String sPhone) {
		if (sPhone.length() == 11) {
			return sPhone = sPhone.subSequence(0, 3) + "****" + sPhone.subSequence(7, 11);
		} else {
			return "****";
		}
	}

	/** 判断字符串格式是否正确，且字节数必须小于某值，且不能为空，且只能是数字、字母、下划线、中文 */
	public static boolean lengthVerify(String value, int length) {
		if (isBlank(value)) {
			return false;
		}

		char[] vs = value.toCharArray();
		int lenght = 0;

		Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]{1}$");
		Pattern pattern2 = Pattern.compile("^[\\dA-Za-z_]{1}$");

		for (char v : vs) {
			Matcher matcher = pattern.matcher(String.valueOf(v));
			Matcher matcher2 = pattern2.matcher(String.valueOf(v));
			if (matcher.matches()) {
				lenght = lenght + 2;
			} else if (matcher2.matches()) {
				lenght = lenght + 1;
			} else {
				return false;
			}
		}

		return (lenght <= length);
	}

	/**
	 * 移除原字符串中的某元素 UtilString.rejectElem("a,b,c","b")="a,c"
	 */
	public static String rejectElem(String src, String elem) {
		return rejectElem(src, elem, ",");
	}

	/**
	 * 移除原字符串中的某元素 UtilString.rejectElem("a,b,c","b")="a,c"
	 */
	public static String rejectElem(String src, String elem, String splite) {
		if (isBlank(src) || isBlank(elem)) {
			return src;
		}
		String temp = (splite + src + splite).replace(splite + elem + splite, splite);
		temp = temp.substring(1, temp.length() > 1 ? temp.length() - 1 : 1);
		return temp;
	}

	/**
	 * 把字符串中的某元素置顶 UtilString.topElem("a,b,c","b")="b,a,c"
	 */
	public static String topElem(String src, String elem) {
		if (isBlank(src) || isBlank(elem) || !src.contains(elem)) {
			return src;
		}
		String temp = rejectElem(src, elem);
		if (isBlank(temp)) {
			return elem;
		} else {
			temp = elem + "," + temp;
			return temp;
		}
	}

	/** 倒序 */
	public static <T> T[] sortDesc(T[] array) {
		Arrays.sort(array);
		T t;
		for (int i = 0; i < array.length / 2; i++) {
			t = array[i];
			array[i] = array[array.length - 1 - i];
			array[array.length - 1 - i] = t;
		}
		return array;
	}

	/** 截取字符串 */
	public static String subString(String str, int length) {
		if (isBlank(str)) {
			return "";
		}
		if (str.length() <= length) {
			return str;
		}
		return str.substring(0, length);
	}

	/** 首字母小写 */
	public static String firstLower(String str) {
		if (isBlank(str)) {
			return str;
		}
		char[] chars = new char[1];
		chars[0] = str.charAt(0);
		String temp = new String(chars);
		if (chars[0] >= 'A' && chars[0] <= 'Z') {
			return str.replaceFirst(temp, temp.toLowerCase());
		} else {
			return str;
		}
	}

	/** 特殊字符过滤：| & ; $ % @ ' " < > ( ) + CR LF , . script document eval */
	public static String keywordFilter(String keyword) {
		if (UtilString.isBlank(keyword)) {
			return keyword;
		}
		return keyword.replaceAll("\\||&|;|\\$|\\%|@|'|\"|<|>|\\(|\\)|\\+|CR|LF|,|\\.|script|document|eval", "");
	}

	/** 获取0的字符串 */
	private static String getZero(int length) {
		StringBuilder str = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			str.append("0");
		}
		return str.toString();
	}

	/** 左侧补零 */
	public static String leftZeroShift(String s, int length) {
		if (s == null || s.length() > length)
			return s;
		String str = getZero(length) + s;
		str = str.substring(str.length() - length);
		return str;
	}

	/** 右侧补零 */
	public static String rightZeroShift(String s, int length) {
		if (s == null || s.length() > length)
			return s;
		String str = s + getZero(length);
		str = str.substring(0, length);
		return str;
	}

	/** @see com.zcj.util.UtilRandom#getRandom(int, int) */
	@Deprecated
	public static int getRandom(final int min, final int max) {
		return UtilRandom.getRandom(min, max);
	}

	/** @see com.zcj.util.UtilCollection#getSubDoubleArray(List, int, int) */
	@Deprecated
	public static Double[] arraySubList(List<Double> list, int fromIndex, int toIndex) {
		return UtilCollection.getSubDoubleArray(list, fromIndex, toIndex);
	}

	/** @see com.zcj.util.UtilCollection#getSubDoubleArray(Double[], int, int) */
	@Deprecated
	public static Double[] arraySubList(Double[] array, int fromIndex, int toIndex) {
		return UtilCollection.getSubDoubleArray(array, fromIndex, toIndex);
	}

	/** @see #isIdcard(String, boolean) */
	@Deprecated
	public static boolean checkIDCard(String idcard, boolean onlyEighteen) {
		return isIdcard(idcard, onlyEighteen);
	}

	/** @see #isIdcard(String) */
	@Deprecated
	public static boolean CheckIDCard(String idcard) {
		return isIdcard(idcard);
	}

	/** @see com.zcj.util.UtilConvert#collection2String(Collection) */
	@Deprecated
	public static String collectionToString(Collection<String> collection) {
		return UtilConvert.collection2String(collection);
	}

	/** @see com.zcj.util.UtilConvert#collection2String(Collection, String) */
	@Deprecated
	public static String collectionToString(Collection<String> collection, String splite) {
		return UtilConvert.collection2String(collection, splite);
	}

	/** @see com.zcj.util.UtilHtml#delHTMLTag(String) */
	@Deprecated
	public static String delHTMLTag(String htmlStr) {
		return UtilHtml.delHTMLTag(htmlStr);
	}

	/** @see com.zcj.util.UtilMath#discount(Float, Float) */
	@Deprecated
	public static double discount(Float theOld, Float theNew) {
		return UtilMath.discount(theOld, theNew);
	}

	/** @see com.zcj.util.UtilConvert#mapKeyValueReplace(Map) */
	@Deprecated
	public static Map<String, Long> mapKeyValueReplace(Map<Long, String> map) {
		return UtilConvert.mapKeyValueReplace(map);
	}

	/** @see com.zcj.util.UtilConvert#map2Json(Map) */
	@Deprecated
	public static String mapToJson(Map<String, String> theMap) {
		return UtilConvert.map2Json(theMap);
	}

	/** @see com.zcj.util.UtilConvert#string2List(String) */
	@Deprecated
	public static List<String> stringToList(String value) {
		return UtilConvert.string2List(value);
	}

	/** @see com.zcj.util.UtilConvert#string2LongList(String) */
	@Deprecated
	public static List<Long> stringToLongList(String value) {
		return UtilConvert.string2LongList(value);
	}

}
