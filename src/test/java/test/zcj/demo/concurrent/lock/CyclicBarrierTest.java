package test.zcj.demo.concurrent.lock;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 循环倒计数量锁
 * 		可循环实现N个任务完成之后，就做一些事情。
 * 
 * 		步骤1、定义 CyclicBarrier。
 * 			final CyclicBarrier cb = new CyclicBarrier(3, new MyRunnable());
 * 		步骤2、cb.await() 被运行 3 次后，MyRunnable 运行。
 * 		步骤3、cb.await() 再运行 3 次后，MyRunnable 运行。
 * 
 * @author ZCJ
 * @data 2012-10-31
 */
public class CyclicBarrierTest {
	
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		
		// 声明有三个任务，cb.await()被调用三次之后，就运行MyRunnable（可循环使用：三个任务都结束之后又变成新申请了三个任务）
		final CyclicBarrier cb = new CyclicBarrier(3, new MyRunnable());
		
		for (int i=0; i<3; i++) {
			service.submit(new Runnable() {
				@Override
				public void run() {
					try {
						
						System.out.println("线程"+Thread.currentThread().getName()+"跑出来了");
						Thread.sleep((long)(Math.random()*10000));
						System.out.println("线程"+Thread.currentThread().getName()+"到达终点，当前已有"+(cb.getNumberWaiting()+1)+"人到达终点");
						cb.await();// 说明完成了一个任务（被调用三次后，下面的语句才开始运行）
						
						System.out.println("线程"+Thread.currentThread().getName()+"跑出来了");
						Thread.sleep((long)(Math.random()*10000));
						System.out.println("线程"+Thread.currentThread().getName()+"到达终点，当前已有"+(cb.getNumberWaiting()+1)+"人到达终点");
						cb.await();// 说明完成了一个任务（被调用三次后，下面的语句才开始运行）
						
					} catch (Exception e) {
					}
				}
			});
		}
		service.shutdown();
	}
	
	private static final class MyRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println("所有任务都完成了，我要做一些事情了！由线程"+Thread.currentThread().getName()+"来做！");
		}
	}
}
