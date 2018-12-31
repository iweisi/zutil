package com.zcj.ext.amap.api.locate.dto;

public class ApiLocateUrl {

	private static final String URL = "http://apilocate.amap.com/position";

	private static final String GSM_2G_URL = URL
			+ "?key=%s&accesstype=0&imei=%s&cdma=0&network=GSM&bts=%s&nearbts=%s&macs=%s";

	private static final String GSM_2G_URL_IMSI = URL
			+ "?key=%s&accesstype=0&imei=%s&imsi=%s&cdma=0&network=GSM&bts=%s&nearbts=%s&macs=%s";

	/**
	 * 移动联通2G网络
	 * 
	 * @param key
	 * @param imei
	 * @param bts
	 *            接入基站信息，格式：mcc,mnc,lac,cellid,signal<br>
	 *            460,1,18204,35638,7<br>
	 *            460,0,26743,24030,46
	 * @param nearbts
	 *            周边基站信息，格式：基站信息 1|基站信息 2|基站信息 3
	 * @param macs
	 *            WIFI的MAC信息，格式：f0:7d:68:9e:7d:18,-41,TPLink|f0:7d:68:9e:7d:18,-41,TPLink
	 * @return
	 */
	public static String init2GGSMUrl(String key, String imei, String bts, String nearbts, String macs) {
		return String.format(GSM_2G_URL, key, imei, bts, nearbts, macs);
	}

	/**
	 * 移动联通2G网络
	 * 
	 * @param key
	 * @param imei
	 * @param imsi
	 * @param bts
	 *            格式：mcc,mnc,lac,cellid,signal<br>
	 *            460,1,18204,35638,7<br>
	 *            460,0,26743,24030,46
	 * @param nearbts
	 *            格式：基站信息 1|基站信息 2|基站信息 3
	 * @param macs
	 *            格式：f0:7d:68:9e:7d:18,-41,TPLink|f0:7d:68:9e:7d:18,-41,TPLink
	 * @return
	 */
	public static String init2GGSMUrl(String key, String imei, String imsi, String bts, String nearbts, String macs) {
		return String.format(GSM_2G_URL_IMSI, key, imei, imsi, bts, nearbts, macs);
	}

}
