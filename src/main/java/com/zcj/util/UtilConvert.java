package com.zcj.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 类型转换和格式转换 */
public class UtilConvert {

	/** 替换Map的key和value的位置 */
	public static <V, K> Map<V, K> mapKeyValueReplace(Map<K, V> map) {
		if (map == null) {
			return null;
		}
		Map<V, K> result = new HashMap<V, K>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			result.put(entry.getValue(), entry.getKey());
		}
		return result;
	}

	/** Map 转 JSON */
	public static String map2Json(Map<String, String> theMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean isFirst = true;
		for (Map.Entry<String, String> entry : theMap.entrySet()) {
			if (isFirst) {
				sb.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
			} else {
				sb.append(",");
				sb.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
			}
			isFirst = false;
		}
		sb.append("}");
		return sb.toString();
	}

	/** Collection格式转换成用英文逗号分隔的String */
	public static <T> String collection2String(Collection<T> collection) {
		return collection2String(collection, ",");
	}

	/** Collection格式转换成用指定分隔符splite分隔的String */
	public static <T> String collection2String(Collection<T> collection, String splite) {
		if (collection == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T a : collection) {
			if (isFirst) {
				sb.append(a);
			} else {
				sb.append(splite);
				sb.append(a);
			}
			isFirst = false;
		}
		return sb.toString();
	}

	/** 英文逗号分隔的字符串转换成List格式 */
	public static List<String> string2List(String value) {
		if (UtilString.isBlank(value)) {
			return new ArrayList<String>();
		} else {
			return Arrays.asList(value.split(","));
		}
	}

	/** 英文逗号分隔的字符串转换成List格式 */
	public static List<Long> string2LongList(String value) {
		List<String> stringList = string2List(value);
		List<Long> result = new ArrayList<Long>();
		for (String v : stringList) {
			result.add(Long.parseLong(v));
		}
		return result;
	}

	public static Long[] string2Long(String[] value) {
		if (value == null) {
			return null;
		}
		Long[] result = new Long[value.length];
		for (int i = 0; i < value.length; i++) {
			result[i] = Long.valueOf(value[i]);
		}
		return result;
	}

	public static Double[] string2Double(String[] value) {
		if (value == null) {
			return null;
		}
		Double[] result = new Double[value.length];
		for (int i = 0; i < value.length; i++) {
			result[i] = Double.valueOf(value[i]);
		}
		return result;
	}

	public static Integer[] string2Integer(String[] value) {
		if (value == null) {
			return null;
		}
		Integer[] result = new Integer[value.length];
		for (int i = 0; i < value.length; i++) {
			result[i] = Integer.valueOf(value[i]);
		}
		return result;
	}

	public static String array2String(Object[] arrays) {
		return array2String(arrays, ",");
	}

	public static String array2String(Object[] arrays, String splite) {
		if (UtilString.isBlank(splite)) {
			splite = ",";
		}
		if (arrays == null || arrays.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (Object a : arrays) {
			if (isFirst) {
				sb.append(a);
			} else {
				sb.append(splite);
				sb.append(a);
			}
			isFirst = false;
		}
		return sb.toString();
	}

	/** 格式化Double的值 */
	public static String double2String(Double value, String pattern) {
		if (value == null || UtilString.isBlank(pattern)) {
			return null;
		}
		return new DecimalFormat(pattern).format(value);
	}

	/** Double的值保留两位小数点 */
	public static String double2String(Double value) {
		return double2String(value, "0.00");
	}

	/** Double的值保留两位小数点 */
	public static Double formatDouble(Double value) {
		String result = double2String(value);
		if (UtilString.isBlank(result)) {
			return null;
		}
		return Double.valueOf(result);
	}

	/** byte数组转换成十六进制字符串 */
	public static String byteArray2HexStr(byte[] b) {
		if (b == null) {
			return null;
		} else if (b.length == 0) {
			return "";
		}
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}

	/** @see #array2String(Object[]) */
	@Deprecated
	public static String arrayToString(Object[] arrays) {
		return array2String(arrays);
	}

	/** @see #array2String(Object[], String) */
	@Deprecated
	public static String arrayToString(Object[] arrays, String splite) {
		return array2String(arrays, splite);
	}

}
