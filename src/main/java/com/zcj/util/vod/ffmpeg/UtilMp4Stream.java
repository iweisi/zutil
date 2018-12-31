package com.zcj.util.vod.ffmpeg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zcj.util.vod.cmd.UtilCmd;

public class UtilMp4Stream {

	private static final String INJECTED_KEY_TEXT = "last atom in file was not a moov atom";

	/**
	 * 
	 * @param faststartExePath
	 *            E:\\ffmpeg\\qt-faststart\\qt-faststart.exe
	 * @param inPath
	 * @param outPath
	 * @return
	 */
	public static boolean injectKey(String faststartExePath, String inPath, String outPath) {
		if (StringUtils.isBlank(outPath)) {
			return false;
		}
		List<String> cmd = new ArrayList<String>();
		cmd.add(faststartExePath);
		cmd.add(inPath);
		cmd.add(outPath);
		String lastLine = "";
		UtilCmd.exec(cmd, lastLine);
		if (null != lastLine && INJECTED_KEY_TEXT.equals(lastLine)) {
			outPath = inPath;
		}
		File checkFile = new File(outPath);
		if (checkFile.exists() && checkFile.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
