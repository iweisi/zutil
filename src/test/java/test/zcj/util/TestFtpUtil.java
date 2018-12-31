package test.zcj.util;

import java.io.File;

import com.zcj.util.FtpUtil;

public class TestFtpUtil {

	public static void main(String[] args) throws Exception {
		
		FtpUtil t = new FtpUtil();

		System.out.println("连接 ：" + t.connect("192.168.1.119", "test", "test"));

		t.upload(new File("F:\\2013db_log.LDF"), "F:\\test\\");

		// 下载
		// t.download("E:\\ftptest\\mulu", "test.txt", "D:\\db");
	}

}
