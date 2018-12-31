package com.zcj.util.vod;

import com.zcj.util.filenameutils.FilenameUtils;
import com.zcj.util.vod.ffmpeg.UtilFFMpeg;
import com.zcj.util.vod.ffmpeg.UtilMp4Stream;
import com.zcj.util.vod.mediainfo.MediainfoBean;
import com.zcj.util.vod.mediainfo.UtilMediainfo;

public class UtilVod {

	public static VodResult transcoding(String mediainfoPath, String ffmpegPath, String faststartPath, int cpus, String inFilePath) {
		
		VodResult result = new VodResult();
		
		// 源文件信息
		MediainfoBean sourceFileInfo = UtilMediainfo.parser(mediainfoPath, inFilePath);
		result.setSourceFilePath(inFilePath);
		result.setSourceFileInfo(sourceFileInfo);
		
		// 转码后的文件信息
		String h264FilePath = FilenameUtils.getFullPath(inFilePath) + FilenameUtils.getBaseName(inFilePath) + "_h264.mp4";
		if(!UtilFFMpeg.transformToH264(ffmpegPath, cpus, inFilePath, h264FilePath, sourceFileInfo)){
			result.setH264(false);
			return result;
		}
		if(!sourceFileInfo.isH264()){// 转码成功 处理 mpeg profile@level 问题
			//先从非avc格式转换为了avc格式
			MediainfoBean h264FileInfo = UtilMediainfo.parser(mediainfoPath, h264FilePath);
			if(!UtilFFMpeg.mobileSupport(h264FileInfo.getVideoFormatProfile())){//如果不支持移动端,则进行在转码
				if(!UtilFFMpeg.transformToH264(ffmpegPath, cpus, inFilePath, h264FilePath, h264FileInfo)){
					result.setH264(false);
					return result;
				}
			}
		}
		result.setH264(true);
		result.setH264FilePath(h264FilePath);
		
		// 流文件信息
		String streamFilePath = FilenameUtils.getFullPath(inFilePath) + FilenameUtils.getBaseName(inFilePath) + "_stream.mp4";
		if (UtilMp4Stream.injectKey(faststartPath, h264FilePath, streamFilePath)) {
			result.setStream(true);
			result.setStreamFilePath(streamFilePath);
		} else {
			return result;
		}

		// 截图信息
		String imageFilePath = FilenameUtils.getFullPath(inFilePath) + FilenameUtils.getBaseName(inFilePath) + "_photo.jpg";
		if (UtilFFMpeg.screenshot(ffmpegPath, cpus, inFilePath, imageFilePath, null, sourceFileInfo.getDuration())) {
			result.setImage(true);
			result.setImageFilePath(imageFilePath);
		} else {
			return result;
		}
		
		return result;
	}
	
}
