package test.zcj.demo.net.rfid;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zcj.util.UtilCollection;
import com.zcj.util.UtilString;

public class RfidServerDemo {

	private static final ExecutorService executorService = Executors.newCachedThreadPool();// 线程池

	private static Set<Integer> TX = UtilCollection.initHashSet(741, 742, 815, 816);

	public static void main(String[] args) throws IOException {
		server();
	}

	private static void server() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(32500);

		while (true) {

			System.out.println("服务端：重新侦听并等待接受客户端的连接");
			final Socket socket = ss.accept();
			System.out.println("服务端：服务器接收到客户端的数据后，创建与此客户端对话的Socket");

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 用于接收客户端发来的数据的输入流
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						String kc = "";
						int length = 999;
						while (true) {
							int k = dis.readUnsignedByte();
							if ("85-170-".equals(kc)) {
								length = k;
							}
							kc += k + "-";
							length--;
							if (length == 0) {
								printRoomInOut(kc);
								printCardLocation(kc);
								kc = "";
								length = 999;
							}
						}
						// 不需要继续使用此连接时，关闭连接
						// socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 关闭服务
		// ss.close();
	}

	// KEY:[卡号] VALUE:[天线位置]
	private static Map<Integer, Integer> CardLocationMap = new ConcurrentHashMap<Integer, Integer>();

	private static void printCardLocation(String kc) {
		int[] kcConver = conver(kc);
		if (kcConver != null && kcConver.length == 5) {
			int dcard = kcConver[2];
			int dtx = kcConver[3];
			if (TX.contains(dtx)) {
				Integer oldLocation = CardLocationMap.get(dcard);
				if (oldLocation == null || oldLocation.intValue() != dtx) {
					System.out.println("卡" + dcard + "出现在天线" + dtx + "的位置");
					CardLocationMap.put(dcard, dtx);
				}
			}
		}
	}

	// KEY:[卡号-房间号]:110007-741 VALUE:最后一次感应的天线，里=true；外=false
	private static Map<String, Boolean> CardRoomStateMap = new ConcurrentHashMap<String, Boolean>();

	private static void printRoomInOut(String kc) {
		int[] kcConver = conver(kc);
		if (kcConver != null && kcConver.length == 5) {
			int dcard = kcConver[2];
			int dtx = kcConver[3];
			int droom = dtx % 2 == 0 ? dtx - 1 : dtx;
			if (TX.contains(dtx)) {
				Boolean inRoom = CardRoomStateMap.get(dcard + "-" + droom);
				if ((inRoom == null || inRoom.booleanValue()) && droom == dtx) {
					System.out.println("卡" + dcard + "离开房间" + droom + "[感应天线：" + dtx + "]");
					CardRoomStateMap.put(dcard + "-" + droom, false);
				} else if ((inRoom == null || !inRoom.booleanValue()) && droom != dtx) {
					System.out.println("卡" + dcard + "进入房间" + droom + "[感应天线：" + dtx + "]");
					CardRoomStateMap.put(dcard + "-" + droom, true);
				}
			}
		}
	}

	private static int[] conver(String kv) {
		// System.out.println(kv);
		int[] result = null;
		if (UtilString.isNotBlank(kv)) {
			String[] a = kv.split("-");
			if (a.length == 19 && "1".equals(a[6])) {
				result = new int[5];
				result[0] = Integer.valueOf(a[5]) * 256 + Integer.valueOf(a[4]);// 设备ID
				result[1] = Integer.valueOf(a[10]) * 256 + Integer.valueOf(a[9]);// 信号强度
				result[2] = Integer.valueOf(a[13]) * 256 * 256 + Integer.valueOf(a[12]) * 256 + Integer.valueOf(a[11]);// 标签ID
				result[3] = Integer.valueOf(a[15]) * 256 + Integer.valueOf(a[16]);// 唤醒天线ID号
				result[4] = Integer.valueOf(a[17]);// 唤醒天线场强值
				// System.out.println("---卡" + result[2] + ";天线" + result[3] +
				// ";设备" + result[0] + ";信号强度" + result[1]);
			}
		}
		return result;
	}
}