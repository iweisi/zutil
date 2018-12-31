package test.zcj.demo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

public class NetDemo {

	public static void main(String[] args) throws Exception {

		testINetAddress();
		testUrl();
		testReadUrl();

	}

	public static void testINetAddress() throws UnknownHostException {
		InetAddress add = InetAddress.getLocalHost();
		System.out.println("当前主机名：" + add.getHostName());
		System.out.println("当前主机地址：" + add.getHostAddress());
		System.out.println("当前主机地址的字节数组：" + Arrays.toString(add.getAddress()));
	}

	public static void testUrl() throws MalformedURLException {
		URL u = new URL("http://www.imooc.com/index.html?username=zou#test");
		System.out.println(u.getProtocol());
		System.out.println(u.getHost());
		System.out.println(u.getPort());
		System.out.println(u.getPath());// 文件路径
		System.out.println(u.getFile());// 文件名
		System.out.println(u.getRef());// 相对路径
		System.out.println(u.getQuery());// 查询字符串
	}

	// 读取URL内的HTML
	public static void testReadUrl() throws IOException {
		URL u = new URL("http://www.baidu.com/");
		InputStream is = u.openStream();// 获取字节输入流
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");// 转为字符输入流
		BufferedReader br = new BufferedReader(isr);// 为字符输入流添加缓冲
		String data = br.readLine();// 读取一条数据
		while (data != null) {
			System.out.println(data);
			data = br.readLine();
		}
	}

}
