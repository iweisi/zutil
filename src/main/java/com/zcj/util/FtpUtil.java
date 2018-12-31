package com.zcj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//<dependency>
//	<groupId>commons-net</groupId>
//	<artifactId>commons-net</artifactId>
//	<version>3.3</version>
//</dependency>
public class FtpUtil {

	private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	private FTPClient ftp;

	/**
	 * 连接到FTP服务器
	 * 
	 * @param addr
	 *            服务器地址
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String addr, String username, String password) throws Exception {
		return connect(addr, 21, username, password);
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param addr
	 *            服务器地址
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String addr, int port, String username, String password) throws Exception {
		ftp = new FTPClient();
		ftp.connect(addr, port);
		ftp.login(username, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return false;
		}
		return true;
	}

	/**
	 * 上传本地文件或文件夹到服务器指定目录
	 * 
	 * @param file
	 * @param path
	 * @throws Exception
	 */
	public void upload(File file, String path) throws Exception {
		if (file.isDirectory()) {
			ftp.makeDirectory(file.getName());
			ftp.changeWorkingDirectory(file.getName());
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				File file1 = new File(file.getPath() + "\\" + files[i]);
				if (file1.isDirectory()) {
					upload(file1, path);
					ftp.changeToParentDirectory();
				} else {
					File file2 = new File(file.getPath() + "\\" + files[i]);
					FileInputStream input = new FileInputStream(file2);
					ftp.storeFile(file2.getName(), input);
					logger.debug("文件" + file2.getPath() + "上传成功！");
					input.close();
				}
			}
		} else {
			File file2 = new File(file.getPath());
			InputStream input = new FileInputStream(file2);
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(file2.getName(), input);
			logger.debug("文件" + file2.getPath() + "上传成功！");
			input.close(); // 关闭输入流
			ftp.logout(); // 退出连接
		}
	}

	public void download(String reomvepath, String fileName, String localPath) throws Exception {
		ftp.changeWorkingDirectory(reomvepath);

		// 列出该目录下所有文件
		FTPFile[] fs = ftp.listFiles();
		// 遍历所有文件，找到指定的文件
		for (FTPFile ff : fs) {
			if (ff.getName().equals(fileName)) {
				// 根据绝对路径初始化文件
				File localFile = new File(localPath + "/" + ff.getName());
				// 输出流
				OutputStream is = new FileOutputStream(localFile);
				// 下载文件
				ftp.retrieveFile(ff.getName(), is);
				logger.debug("下载成功!");
				is.close();
			}
		}

		ftp.logout(); // 退出连接
	}
}
