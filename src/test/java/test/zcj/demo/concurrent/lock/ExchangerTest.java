package test.zcj.demo.concurrent.lock;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 两个线程交换数据
 * 
 * 		步骤1、定义。
 * 			final Exchanger<String> exchanger = new Exchanger<String>();
 * 		步骤2、交换数据。（第一个放的时候等待，第二个放的时候马上交换，得到各自的数据后各自继续运行下去。）
 * 			线程1：String data2 = (String) exchanger.exchange(data1);
 * 			线程2：String data1 = (String) exchanger.exchange(data2);
 * 
 * @author ZCJ
 * @data 2012-10-31
 */
public class ExchangerTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Exchanger<String> exchanger = new Exchanger<String>();
		
		service.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String data1 = "111";
					System.out.println("线程"+Thread.currentThread().getName()+"打算提供数据："+data1);
					Thread.sleep((long)(Math.random()*10000));
					String data2 = (String) exchanger.exchange(data1);
					System.out.println("线程"+Thread.currentThread().getName()+"换来了数据："+data2);
				} catch (InterruptedException e) {
				}
			}
		});
		
		service.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String data2 = "222";
					System.out.println("线程"+Thread.currentThread().getName()+"打算提供数据："+data2);
					Thread.sleep((long)(Math.random()*10000));
					String data1 = (String) exchanger.exchange(data2);
					System.out.println("线程"+Thread.currentThread().getName()+"换来了数据："+data1);
				} catch (InterruptedException e) {
				}
			}
		});
		
		service.shutdown();
		
	}
}
