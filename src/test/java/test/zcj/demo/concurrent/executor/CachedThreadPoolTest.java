package test.zcj.demo.concurrent.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("unused")
public class CachedThreadPoolTest {
	
	public static final ExecutorService execService = Executors.newCachedThreadPool();
	
	public static void doing() throws Exception {
		
		// 将我们自定义的任务提交给线程池去执行并返回一个 Future 对象
		Future<String> future = execService.submit(new UpdateState(service, id));
		
		// Future代表一个异步执行的操作，如果任务未完成，则get会阻塞直到任务完成。get方法之后的语句也阻塞。
		System.out.println(future.get());
		
		// 当所有的线程执行完毕后关闭线程池
		execService.shutdown();
	}
	
	private static final class UpdateState implements Callable<String> {
		
		private final MyService service;
		private final String id;

		public UpdateState(final MyService service, final String id) {
			this.service = service;
			this.id = id;
		}

		@Override
		public String call() throws Exception {
			System.out.println("开始运行");
			Thread.sleep(3000);
			System.out.println("运行完毕");
			return "返回值";
		}
	}
	
	// ----------------------------------------------------------------------------
	interface MyService {}
	private static MyService service;
	private static String id = "AAA";
	public static void main(String[] args) throws Exception {
		doing();
	}
	// ----------------------------------------------------------------------------
}
