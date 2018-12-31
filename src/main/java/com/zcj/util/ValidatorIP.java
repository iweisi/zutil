package com.zcj.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorIP {

	/** 初始化一个测试的IP区间 */
	public static Map<String, String> initTestIpMap() {
		Map<String, String> ipMap = new HashMap<String, String>();
		ipMap.put("10.110.159.1", "10.110.168.255");
		ipMap.put("192.168.1.1", "192.168.1.255");
		return ipMap;
	}

	/**
	 * 验证ipAddress是否在ipMap(key~value)区间中
	 * 
	 * @param ipAddress
	 * @param ipMap
	 * @return
	 */
	public static boolean isIPInner(String ipAddress, Map<String, String> ipMap) {
		if (UtilString.isBlank(ipAddress) || !isIp(ipAddress) || ipMap == null) {
			return false;
		}
		long ipNum = getIpNum(ipAddress);

		for (Map.Entry<String, String> entry : ipMap.entrySet()) {
			if (isInner(ipNum, getIpNum(entry.getKey()), getIpNum(entry.getValue()))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isIp(String ip) {
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	private static long getIpNum(String ipAddress) {
		if (UtilString.isBlank(ipAddress) || !isIp(ipAddress)) {
			return 0;
		}
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		if (userIp == 0 || begin == 0 || end == 0)
			return false;
		return (userIp >= begin) && (userIp <= end);
	}

}
