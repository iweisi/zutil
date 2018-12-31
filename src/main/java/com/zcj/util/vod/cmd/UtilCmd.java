package com.zcj.util.vod.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilCmd {

	private static final Logger logger = LoggerFactory.getLogger(UtilCmd.class);

	private static void printCmd(List<String> cmds) {
		for (String cmd : cmds) {
			logger.info(cmd);
		}
	}

	public static void exec(List<String> cmd, String code) {
		BufferedReader stdout = null;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(cmd);
			builder.redirectErrorStream(true);
			printCmd(cmd);
			Process proc = builder.start();
			stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = stdout.readLine()) != null) {
				if (code != null) {
					code = line;
				}
			}
			proc.waitFor();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != stdout) {
				try {
					stdout.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}
}
