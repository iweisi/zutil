package test.zcj.util.vod;

import com.google.gson.Gson;
import com.zcj.util.vod.UtilVod;
import com.zcj.util.vod.VodResult;

public class TestVod {

	public static void main(String[] args) {
		String source = "E:\\ffmpeg\\bb.3gp";
		
		String ffmpegPath = "E:\\ffmpeg\\ffmpeg-20140919-64-static\\bin\\ffmpeg.exe";
		String mediainfoPath = "E:\\ffmpeg\\mediainfo\\MediaInfo.exe";
		String faststartPath = "E:\\ffmpeg\\qt-faststart\\qt-faststart.exe";
		
		VodResult result = UtilVod.transcoding(mediainfoPath, ffmpegPath, faststartPath, 1, source);
		System.out.println(new Gson().toJson(result));
	}
}
