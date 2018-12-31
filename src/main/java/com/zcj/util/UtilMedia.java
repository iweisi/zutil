package com.zcj.util;

import java.util.HashMap;
import java.util.Map;

import com.zcj.util.UtilString;

public class UtilMedia {

	private static Map<String, String> MEDIA_TYPE = new HashMap<String, String>();

	static {
		// 音频
		MEDIA_TYPE.put("mp3", "audio");
		MEDIA_TYPE.put("mid", "audio");
		MEDIA_TYPE.put("midi", "audio");
		MEDIA_TYPE.put("asf", "audio");
		MEDIA_TYPE.put("wm", "audio");
		MEDIA_TYPE.put("wma", "audio");
		MEDIA_TYPE.put("wmd", "audio");
		MEDIA_TYPE.put("amr", "audio");
		MEDIA_TYPE.put("wav", "audio");
		MEDIA_TYPE.put("3gpp", "audio");
		MEDIA_TYPE.put("mod", "audio");
		MEDIA_TYPE.put("mpc", "audio");
		MEDIA_TYPE.put("aac", "audio");
		MEDIA_TYPE.put("m4a", "audio");

		// 视频
		MEDIA_TYPE.put("fla", "video");
		MEDIA_TYPE.put("flv", "video");
		MEDIA_TYPE.put("wav", "video");
		MEDIA_TYPE.put("wmv", "video");
		MEDIA_TYPE.put("avi", "video");
		MEDIA_TYPE.put("rm", "video");
		MEDIA_TYPE.put("rmvb", "video");
		MEDIA_TYPE.put("3gp", "video");
		MEDIA_TYPE.put("mp4", "video");
		MEDIA_TYPE.put("mov", "video");
		MEDIA_TYPE.put("swf", "video");

		// 图片
		MEDIA_TYPE.put("jpg", "photo");
		MEDIA_TYPE.put("jpeg", "photo");
		MEDIA_TYPE.put("png", "photo");
		MEDIA_TYPE.put("bmp", "photo");
		MEDIA_TYPE.put("gif", "photo");
	}

	public static boolean isPhoto(String extension) {
		if (UtilString.isNotBlank(extension)) {
			return "photo".equals((String) MEDIA_TYPE.get(extension.toLowerCase()));
		}
		return false;
	}

	public static boolean isVideo(String extension) {
		if (UtilString.isNotBlank(extension)) {
			return "video".equals((String) MEDIA_TYPE.get(extension.toLowerCase()));
		}
		return false;
	}

	public static boolean isAudio(String extension) {
		if (UtilString.isNotBlank(extension)) {
			return "audio".equals((String) MEDIA_TYPE.get(extension.toLowerCase()));
		}
		return false;
	}

}
