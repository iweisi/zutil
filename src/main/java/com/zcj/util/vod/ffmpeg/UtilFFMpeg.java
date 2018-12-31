package com.zcj.util.vod.ffmpeg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.zcj.util.vod.cmd.UtilCmd;
import com.zcj.util.vod.mediainfo.MediainfoBean;

public class UtilFFMpeg {

	private static final String ASPECT_RATIO_16_9 = "16:9";
	private static final String ASPECT_RATIO_4_3 = "4:3";
	private static final Pattern pattern = Pattern.compile("\\d+[\\.]?\\d*");

	public static boolean mobileSupport(String profileLevel) {
		if (StringUtils.isBlank(profileLevel))
			return true;
		if (profileLevel.indexOf("@") == -1)
			return true;
		String[] tmps = profileLevel.split("@");
		String profile = tmps[0];
		String level = tmps[1];
		if (profile.indexOf("high") > -1) {
			if (profile.indexOf("4:4:4") > -1) {
				return false;
			} else {
				Matcher matcher = pattern.matcher(level);
				if (matcher.find()) {
					if (Double.parseDouble(matcher.group()) > 4.1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static void setOptions169(List<String> cmd, MediainfoBean info) {
		int y = info.getHeight();
		int width, height, avcKbps, avcFps;
		if (y < 480) {
			width = info.getWidth();
			height = info.getHeight();
			avcKbps = info.getBitRate();
			avcFps = info.getFrameRate();
			// accKbps = info.getAccBitRate();
		} else if (y < 720) {
			width = 640;
			height = 360;
			avcKbps = 500;
			avcFps = info.getFrameRate();
		} else if (y < 1080) {
			width = 854;
			height = 480;
			avcKbps = 700;
			avcFps = info.getFrameRate();
		} else {
			width = 1104;
			height = 622;
			avcKbps = 1000;
			avcFps = info.getFrameRate();
		}
		setProfile(cmd, info);
		cmd.add("-s");
		cmd.add(width + "x" + height);
		if (avcKbps > 0) {
			cmd.add("-b:v");
			cmd.add(avcKbps + "k");
		}
		if (avcFps > 0) {
			cmd.add("-r");
			cmd.add(avcFps + "");
		}
	}

	private static boolean checkCopyOptions169(List<String> cmd, MediainfoBean info) {
		int y = info.getHeight();
		int x = info.getWidth();
		int ak = info.getBitRate();
		if (y == 360 && x == 640 && ak == 500) {
			return true;
		} else if (y == 480 && x == 854 && ak == 700) {
			return true;
		} else if (y == 622 && x == 1104 && ak == 1000) {
			return true;
		} else {
			setOptions169(cmd, info);
			return false;
		}
	}

	private static boolean checkCopyOptions43(List<String> cmd, MediainfoBean info) {
		int y = info.getHeight();
		int x = info.getWidth();
		int ak = info.getBitRate();
		if (y == 480 && x == 640 && ak == 500) {
			return true;
		} else if (y == 600 && x == 800 && ak == 700) {
			return true;
		} else if (y == 768 && x == 1024 && ak == 1000) {
			return true;
		} else {
			setOptions43(cmd, info);
			return false;
		}
	}

	private static void setOptions43(List<String> cmd, MediainfoBean info) {
		int y = info.getHeight();
		int width, height, avcKbps, avcFps;
		if (y < 480) {
			width = info.getWidth();
			height = info.getHeight();
			avcKbps = info.getBitRate();
			avcFps = info.getFrameRate();
			// accKbps = info.getAccBitRate();
		} else if (y < 768) {
			width = 640;
			height = 480;
			avcKbps = 500;
			// avcKbps = 1000;
			// avcFps = info.getFrameRate()>15?15:info.getFrameRate();
			avcFps = info.getFrameRate();
			// accKbps = info.getAccBitRate()>32?32:info.getAccBitRate();
		} else if (y < 1200) {
			width = 800;
			height = 600;
			avcKbps = 700;
			avcFps = info.getFrameRate();
			// accKbps = info.getAccBitRate()>48?48:info.getAccBitRate();
		} else {
			width = 1024;
			height = 768;
			avcKbps = 1000;
			avcFps = info.getFrameRate();
			// accKbps = info.getAccBitRate()>96?96:info.getAccBitRate();
		}
		setProfile(cmd, info);
		cmd.add("-s");
		cmd.add(width + "x" + height);
		if (avcKbps > 0) {
			cmd.add("-b:v");
			cmd.add(avcKbps + "k");
		}
		if (avcFps > 0) {
			cmd.add("-r");
			cmd.add(avcFps + "");
		}
	}

	private static void setProfile(List<String> cmd, MediainfoBean info) {
		if (!mobileSupport(info.getVideoFormatProfile())) {
			cmd.add("-profile:v");
			cmd.add("high");
			cmd.add("-pix_fmt");
			cmd.add("yuv420p");
		}
	}

	private static void setOptions(List<String> cmd, MediainfoBean info) {
		// cmd.add("-aspect");
		// cmd.add("16:9");
		setProfile(cmd, info);
	}

	public static boolean transformToH264(String ffmpegExePath, int cpus, String videoStoredPath, String outputStoredPath, MediainfoBean info) {
		List<String> cmd = new ArrayList<String>();
		cmd.add(ffmpegExePath);
		cmd.add("-i");
		cmd.add(videoStoredPath);

		if (null != info.getRotation()) {
			if (info.getRotation().trim().startsWith("90")) {
				cmd.add("-metadata:s:v:0");
				cmd.add("rotate=0");
				cmd.add("-vf");
				cmd.add("\"transpose=1\"");
			}
		}

		cmd.add("-y");// 覆盖输出
		boolean isCopy = false;
		if (!info.isH264()) {
			cmd.add("-vcodec");
			cmd.add("libx264");

			if (ASPECT_RATIO_16_9.equals(info.getAspectRatio())) {
				setOptions169(cmd, info);
			} else if (ASPECT_RATIO_4_3.equals(info.getAspectRatio())) {
				setOptions43(cmd, info);
			} else {
				setOptions(cmd, info);
			}
		} else {
			if (ASPECT_RATIO_16_9.equals(info.getAspectRatio())) {
				isCopy = checkCopyOptions169(cmd, info);
			} else if (ASPECT_RATIO_4_3.equals(info.getAspectRatio())) {
				isCopy = checkCopyOptions43(cmd, info);
			} else {
				isCopy = true;
			}
			setProfile(cmd, info);
		}

		if (!info.isAac()) {
			// cmd.add("-acodec");
			// cmd.add("aac");
		}
		/*
		 * cmd.add("-vframes"); cmd.add("1");
		 */
		cmd.add("-coder");
		cmd.add("0");
		cmd.add("-refs");
		cmd.add("1");
		// cmd.add("-crf");
		// cmd.add("1");

		if (0 < cpus) {
			cmd.add("-threads");
			cmd.add(String.valueOf(cpus));
		}
		if (isCopy) {
			boolean mobileSuport = mobileSupport(info.getVideoFormatProfile());
			if (mobileSuport) {
				if (info.isH264()) {
					cmd.add("-vcodec");
					cmd.add("copy");
				}
				if (info.isAac()) {
					cmd.add("-acodec");
					cmd.add("copy");
				}
			}
		}
		cmd.add(outputStoredPath);
		UtilCmd.exec(cmd, null);
		File checkFile = new File(outputStoredPath);
		if (checkFile.exists() && checkFile.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取截图
	 * 
	 * @param ffmpegExePath
	 * @param cpus
	 * @param inPath
	 * @param imgPath
	 *            如果为空，则默认为inPath同文件夹下文件名为photo.jpg
	 * @param screenSize
	 * @param duration
	 * @return
	 */
	public static boolean screenshot(String ffmpegExePath, int cpus, String inPath, String imgPath, String screenSize, long duration) {

		if (StringUtils.isBlank(imgPath)) {
			return false;
		}

		List<String> cmd = new ArrayList<String>();
		cmd.add(ffmpegExePath);
		cmd.add("-i");
		cmd.add(inPath);
		cmd.add("-y");
		cmd.add("-f");
		cmd.add("image2");
		cmd.add("-ss");
		long times = 8;
		if (duration > 0 && duration <= 8 * 1000L) {
			Random random = new Random();
			times = random.nextInt((int) (duration / 1000.0) - 1) + 1;
		}
		cmd.add(String.valueOf(times));
		cmd.add("-t");
		cmd.add("0.001");
		if (null != screenSize) {
			cmd.add("-s");
			cmd.add(screenSize);
		}

		cmd.add(imgPath);
		UtilCmd.exec(cmd, null);
		File checkFile = new File(imgPath);
		if (checkFile.exists() && checkFile.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

}