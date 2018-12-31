package test.zcj.demo.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁-线程同步
 * 
 * @author ZCJ
 * @data 2012-10-25
 */
public class LockTest {

	public static void main(String[] args) {
		final Going going = new Going();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					going.togoing("zouchongjin");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					going.togoing("1111111111111");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	/** togoing == togoing2 */
	static class Going {
		
		Lock lock = new ReentrantLock();
		public void togoing(String name) throws InterruptedException {
			int a = name.length();
			lock.lock();
			try {
				for (int i=0; i<a; i++) {
					System.out.print(name.charAt(i));
					Thread.sleep(1000);
				}
				System.out.println();
			} finally {
				lock.unlock();
			}
		}
		
		public void togoing2(String name) throws InterruptedException {
			int a = name.length();
			synchronized (this) {
				for (int i=0; i<a; i++) {
					System.out.print(name.charAt(i));
					Thread.sleep(1000);
				}
				System.out.println();
			}
		}
		
	}
}
