package test.zcj.util.vod;

import com.zcj.util.vod.mediainfo.UtilMediainfo;
import com.zcj.util.vod.mediainfo.MediainfoBean;

public class TestMediainfo {

	public static void main(String[] args) {

		String run = "E:\\ffmpeg\\mediainfo\\MediaInfo.exe";
		String path = "E:\\ffmpeg\\aa.mp4";

		MediainfoBean vminfo = UtilMediainfo.parser(run, path, true);
		System.out.println(vminfo.getAspectRatio());
	}

}
