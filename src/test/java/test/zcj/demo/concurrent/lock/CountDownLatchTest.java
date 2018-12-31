package test.zcj.demo.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计数量锁。
 * 		可以实现当一个或多个任务完成之后，其他一个或多个任务才同时开始进行。
 * 
 * 
 * 		步骤1、定义一道拦截门。
 * 			final CountDownLatch cdOrder = new CountDownLatch(1);
 * 		步骤2、多个运动员等待放开拦截。
 * 			cdOrder.await();
 * 			cdOrder.await();
 * 			cdOrder.await();
 * 		步骤3、放开拦截。
 * 			cdOrder.countDown()
 * 		步骤4、所有 cdOrder.await() 后面的代码开始执行。
 * 
 * 
 * 		步骤1、定义三道拦截门
 * 			final CountDownLatch cdAnswer = new CountDownLatch(3);
 * 		步骤2、一个运动员等待放开3个拦截。
 * 			cdAnswer.await();
 * 		步骤3、放开三个拦截。
 * 			cdAnswer.countDown();
 * 			cdAnswer.countDown();
 * 			cdAnswer.countDown();
 * 		步骤4、这个 cdAnswer.await() 后面的代码开始执行。
 * 			
 * @author ZCJ
 * @data 2012-10-31
 */
public class CountDownLatchTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final CountDownLatch cdOrder = new CountDownLatch(1);// 枪声
		final CountDownLatch cdAnswer = new CountDownLatch(3);// 运动员
		
		for (int i=0; i<3; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						//System.out.println("线程"+Thread.currentThread().getName()+"在起跑线");
						//cdOrder.await();// 等待开枪
						System.out.println("线程"+Thread.currentThread().getName()+"开始跑...");
						Thread.sleep((long)(Math.random()*10000));
						System.out.println("线程"+Thread.currentThread().getName()+"到达终点");
						cdAnswer.countDown();// 跑完了
					} catch (InterruptedException e) {
					}
				}
			});
		}
		
		try {
			//Thread.sleep((long)(Math.random()*10000));
			//System.out.println("线程"+Thread.currentThread().getName()+"准备开枪");
			//Thread.sleep((long)(Math.random()*10000));
			//cdOrder.countDown();// 开枪
			//System.out.println("线程"+Thread.currentThread().getName()+"已经开枪了");
			cdAnswer.await();// 等待三个运动员跑完
			System.out.println("线程"+Thread.currentThread().getName()+"看到三个运动员都跑完了");
			service.shutdown();
		} catch (InterruptedException e) {
		}
	}
}
