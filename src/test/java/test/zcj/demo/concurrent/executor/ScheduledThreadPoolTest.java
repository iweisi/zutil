package test.zcj.demo.concurrent.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class ScheduledThreadPoolTest {
	
	public static final ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
	
	public static void doing() {
		
		// 5秒后执行1次
		service.schedule(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("1go...");
			}
			
		}, 5, TimeUnit.SECONDS);
		
		
		// 3秒后每2秒执行1次(任务结束时向后推)
		service.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run() {
				System.out.println("2go...");
			}
			
		}, 3, 2, TimeUnit.SECONDS);
		
		
		// 2秒后每秒执行1次(任务开始时向后推)
		
		final ScheduledFuture<?> myTask = service.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				System.out.println("3go...");
			}
			
		}, 2, 1, TimeUnit.SECONDS);
		
		// myTask.cancel(true);
	}

	
	// ----------------------------------------------------------------------------
	public static void main(String[] args) {
		doing();
	}
	// ----------------------------------------------------------------------------
	
}