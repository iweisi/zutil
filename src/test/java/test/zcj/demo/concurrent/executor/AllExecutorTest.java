package test.zcj.demo.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *	线程池(java.util.concurrent)
 *		1、ExecutorService extends Executor
 *		2、添加了一些生命周期管理的方法
 *			2.1、运行--创建时处于运行状态。
 *			2.2、关闭--调用ExecutorService.shutdown()后，未完成的任务还会继续完成，但是不能再添加任务。
 *			2.3、终止--所有已添加的任务执行完毕后，为终止状态，isTerminated()返回true。
 */
public class AllExecutorTest {
	
	// 创建固定数目线程的线程池。
	// 在某个线程被 shutdown 之前,池中的线程将一直存在
	public static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
				
	// 创建一个同时只有一个线程数的执行者
	// 线程死掉后自动创建
	public static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		
	// 创建一个缓存型池子。
	// 通常用于执行一些生存期很短的异步型任务
	// 调用 execute(无返回值) 或 submit(有返回值 Future) ：
	// 先查看池中有没有以前建立的可用线程，如果有，就重用.如果没有，就建一个新的线程加入池中
	// 自动终止并从缓存中移除那些已有 60 秒钟未被使用的线程。因此，长时间保持空闲的线程池不会使用任何资源。
	public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		
	// 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
	public static final ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		
	// 创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
	// 都是在轮询任务的状态、只有当任务的执行时间到了才真正启动一个线程。
	public static final ScheduledExecutorService ScheduledThreadPool = Executors.newScheduledThreadPool(10);
	
}
