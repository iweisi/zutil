package test.zcj.demo.net.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

	private static final ExecutorService executorService = Executors.newCachedThreadPool();// 线程池

	// Socket的发送与接收是需要同步进行的，即客户端发送一条信息，服务器必需先接收这条信息，而后才可以向客户端发送信息，否则将会有运行时出错。
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(8888);

		while (true) {

			System.out.println("服务端：重新侦听并等待接受客户端的连接");
			final Socket socket = ss.accept();
			System.out.println("服务端：服务器接收到客户端的数据后，创建与此客户端对话的Socket");

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 用于向客户端发送数据的输出流
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						// 用于接收客户端发来的数据的输入流
						DataInputStream dis = new DataInputStream(socket.getInputStream());

						System.out.println("服务器接收到客户端的连接请求：" + dis.readUTF());
						// 服务器向客户端发送连接成功确认信息
						dos.writeUTF("接受连接请求，连接成功!");

						// 不需要继续使用此连接时，关闭连接
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 关闭服务
		// ss.close();
	}
}