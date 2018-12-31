package test.zcj.util.vod;

import com.zcj.util.filenameutils.FilenameUtils;
import com.zcj.util.vod.ffmpeg.UtilFFMpeg;
import com.zcj.util.vod.mediainfo.MediainfoBean;
import com.zcj.util.vod.mediainfo.UtilMediainfo;

public class TestFFMpeg {

	public static void main(String[] args) {
		
		String source = "E:\\ffmpeg\\bb.3gp";
		String h264FilePath = FilenameUtils.getFullPath(source) + FilenameUtils.getBaseName(source) + "_h264.mp4";
		
		String ffmpegPath = "E:\\ffmpeg\\ffmpeg-20140919-64-static\\bin\\ffmpeg.exe";
		String mediainfoPath = "E:\\ffmpeg\\mediainfo\\MediaInfo.exe";
		
		MediainfoBean sourceFileInfo = UtilMediainfo.parser(mediainfoPath, source);
		boolean a = UtilFFMpeg.transformToH264(ffmpegPath, 1, source, h264FilePath, sourceFileInfo);
		System.out.println(a);
	}
	
}
