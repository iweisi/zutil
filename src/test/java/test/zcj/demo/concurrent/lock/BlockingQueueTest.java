package test.zcj.demo.concurrent.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 固定长度的可阻塞队列(可用线程池替代)
 * 
 * @author ZCJ
 * @data 2012-11-1
 */
public class BlockingQueueTest {

	final static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(16);// PS:参数可以是任意数值，运行效果一样
	
	public static void main(String[] args) throws InterruptedException {
		
		for (int i=0; i<4; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							String log = queue.take();// 获取元素时等待队列有内容。
							myPrintln(log);
						} catch (InterruptedException e) {
						}
					}
				}
			}).start();
		}
		
		for (int i=0; i<16; i++) {// PS:也可以改装成多线程储数据
			final String log = (i+1)+"";
			queue.put(log);// 储元素时等待空间变得可用。
		}
		
	}
	
	public static void myPrintln(String log) throws InterruptedException {
		Thread.sleep(1000);
		System.out.println(log+":"+(System.currentTimeMillis()/1000));
	}
}
