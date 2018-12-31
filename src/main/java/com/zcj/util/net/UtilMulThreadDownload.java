package com.zcj.util.net;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程下载文件
 * 
 * @author ZCJ
 * @data 2013-9-16
 */
public class UtilMulThreadDownload {

	private static final Logger logger = LoggerFactory.getLogger(UtilMulThreadDownload.class);

	public void download(String path, int threadsize) throws Exception {
		URL downpath = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			int length = conn.getContentLength();// 获取网络文件的长度
			File file = new File(getFileName(path));
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");// 生成本地文件
			accessFile.setLength(length);
			accessFile.close();
			// 计算每条线程负责下载的数据量
			int block = length % threadsize == 0 ? length / threadsize : length / threadsize + 1;
			for (int threadid = 0; threadid < threadsize; threadid++) {
				new DownloadThread(threadid, downpath, block, file).start();
			}
		}
	}

	private final class DownloadThread extends Thread {
		private int threadid;
		private URL downpath;
		private int block;
		private File file;

		public DownloadThread(int threadid, URL downpath, int block, File file) {
			this.threadid = threadid;
			this.downpath = downpath;
			this.block = block;
			this.file = file;
		}

		public void run() {
			int startposition = threadid * block;// 从网络文件的什么位置开始下载数据
			int endposition = (threadid + 1) * block - 1;// 下载到网络文件的什么位置结束
			// 指示该线程要从网络文件的startposition位置开始下载，下载到endposition位置结束
			// Range:bytes=startposition-endposition
			try {
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.seek(startposition);// 移动指针到文件的某个位置
				HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Range", "bytes=" + startposition + "-" + endposition);
				if (conn.getResponseCode() == 206) {
					InputStream inStream = conn.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						accessFile.write(buffer, 0, len);
					}
					accessFile.close();
					inStream.close();
					logger.debug("第" + (threadid + 1) + "线程下载完成");
				} else {
					logger.debug("第" + (threadid + 1) + "线程下载失败");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 获取文件名称
	 * 
	 * @param path
	 *            下载路径
	 * @return
	 */
	private static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}
}
