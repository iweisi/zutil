package com.zcj.util.vod.mediainfo;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class MediainfoBean {

	private String format;// 视频格式：mpeg-4、avi
	private long duration;// 持续时间，毫秒：8000
	private String fileSize;// 文件大小：9.43 mib、1.35 gib、569 kib
	
	private String videoFormat;// 视频格式：mpeg-4 visual、avc、techsmith、ms video
	private String videoFormatProfile;//  视频格式Profile：simple@l1、high@l2.1、 baseline@l2.1
	private int width;
	private int height;
	private String aspectRatio;// 显示比例：4:3、16:9、5:4
	private int bitRate;
	private int frameRate;
	private String rotation;// 旋转角度

	private String audioFormat;// 音频格式：aac、pcm
	private String audioFormatProfile;// 音频格式Profile：lc、he-aac / lc
	private int accBitRate;
	
	private String remark;

	private boolean isGeneralStart;
	private boolean isVideoStart;
	private boolean isAudioStart;

	public MediainfoBean() {
	}

	private String group(String line, String prefix) {
		if (StringUtils.isBlank(line))
			return null;
		if (StringUtils.isBlank(prefix))
			return line;
		line = line.trim();
		String[] tmps = line.split(prefix);
		if (tmps.length == 1)
			return tmps[0];
		String value = tmps[1].trim();
		int index = value.indexOf(":");
		if (-1 == index) {
			return value;
		} else {
			return value.substring(index + 1);
		}
	}

	public void parse(String line) {
		if (null == line)
			return;
		line = line.trim();
		remark = remark + line + "\n";
		if (line.equals("General")) {
			isGeneralStart = true;
			return;
		}
		if (line.equals("Video")) {
			isVideoStart = true;
			isGeneralStart = false;
			return;
		}
		if (line.equals("Audio")) {
			isAudioStart = true;
			isVideoStart = false;
			return;
		}
		if (isGeneralStart) {
			String[] tmps = line.split(":");
			if (tmps[0].trim().equals("Format")) {
				format = tmps[1].trim().toLowerCase();
				return;
			}
			if (tmps[0].trim().equals("Duration")) {
				duration = parseTime(tmps[1].trim().toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("File size")) {
				fileSize = tmps[1].trim().toLowerCase();
				return;
			}
		}
		if (isVideoStart) {
			String[] tmps = line.split(":");
			if (tmps[0].trim().equals("Format")) {
				videoFormat = tmps[1].trim().toLowerCase();
				return;
			}

			if (tmps[0].trim().equals("Width")) {
				width = parseInt(tmps[1].toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("Height")) {
				height = parseInt(tmps[1].toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("Display aspect ratio")) {
				if (tmps.length == 2) {
					aspectRatio = tmps[1].trim().toLowerCase();
				} else {
					aspectRatio = tmps[1].trim().toLowerCase() + ":" + tmps[2].trim().toLowerCase();
				}
				return;
			}
			if (tmps[0].trim().equals("Bit rate")) {
				bitRate = (int) parseFloat(tmps[1].toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("Frame rate")) {
				frameRate = (int) parseFloat(tmps[1].toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("Format profile")) {
				videoFormatProfile = group(line, "Format profile").toLowerCase();
				return;
			}
			if (tmps[0].trim().equals("Rotation")) {
				rotation = tmps[1].toLowerCase();
				return;
			}
		}
		if (isAudioStart) {
			String[] tmps = line.split(":");
			if (tmps[0].trim().equals("Format")) {
				audioFormat = tmps[1].trim().toLowerCase();
				return;
			}
			if (tmps[0].trim().equals("Bit rate")) {
				accBitRate = (int) parseFloat(tmps[1].toLowerCase());
				return;
			}
			if (tmps[0].trim().equals("Format profile")) {
				audioFormatProfile = tmps[1].toLowerCase();
				return;
			}
		}
	}

	@SuppressWarnings("resource")
	private static float parseFloat(String line) {
		Scanner scaner = new Scanner(line);
		// scaner.useDelimiter(" ");
		StringBuilder builder = new StringBuilder();
		while (scaner.hasNextFloat()) {
			builder.append(scaner.next());
		}
		if (builder.length() == 0) {
			return 0;
		} else {
			return Float.parseFloat(builder.toString());
		}
	}

	@SuppressWarnings("resource")
	private static int parseInt(String line) {
		Scanner scaner = new Scanner(line);
		// scaner.useDelimiter(" ");
		StringBuilder builder = new StringBuilder();
		while (scaner.hasNextInt()) {
			builder.append(scaner.next());
		}
		if (builder.length() == 0) {
			return 0;
		} else {
			return Integer.parseInt(builder.toString());
		}
	}

	@SuppressWarnings("resource")
	private static long parseTime(final String time) {
		Scanner scanner = new Scanner(time);
		int hour = 0, minute = 0, second = 0, ms = 0;
		while (scanner.hasNext()) {
			String temp = scanner.next();
			int index = temp.indexOf("h");
			if (index > 0) {
				hour = Integer.parseInt(temp.substring(0, index));
				continue;
			}
			index = temp.indexOf("mn");
			if (index > 0) {
				minute = Integer.parseInt(temp.substring(0, index));
				continue;
			}
			index = temp.indexOf("ms");
			if (index > 0) {
				ms = Integer.parseInt(temp.substring(0, index));
				continue;
			}
			index = temp.indexOf("s");
			if (index > 0) {
				second = Integer.parseInt(temp.substring(0, index));
				continue;
			}

		}
		return (hour * 3600 + minute * 60 + second) * 1000L + ms;
	}

	public boolean isMp4() {
		if (null == format) {
			return false;
		} else {
			return format.indexOf("mpeg-4") >= 0;
		}
	}

	public boolean isH264() {
		if (null == videoFormat) {
			return false;
		} else {
			return videoFormat.indexOf("avc") >= 0;
		}
	}

	public boolean isAac() {
		if (null == audioFormat) {
			return false;
		} else {
			return audioFormat.indexOf("aac") >= 0;
		}
	}

	public String getVideoFormat() {
		return videoFormat;
	}

	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String toString() {
		return String.format("是否是mp4格式:%s,视频信息是否是H.264格式：%s,音频信息是否是Acc格式:%s", isMp4() ? "是" : "否", isH264() ? "是" : "否", isAac() ? "是"
				: "否");
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public int getBitRate() {
		return bitRate;
	}

	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	public int getAccBitRate() {
		return accBitRate;
	}

	public void setAccBitRate(int accBitRate) {
		this.accBitRate = accBitRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVideoFormatProfile() {
		return videoFormatProfile;
	}

	public void setVideoFormatProfile(String videoFormatProfile) {
		this.videoFormatProfile = videoFormatProfile;
	}

	public String getAudioFormatProfile() {
		return audioFormatProfile;
	}

	public void setAudioFormatProfile(String audioFormatProfile) {
		this.audioFormatProfile = audioFormatProfile;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

}