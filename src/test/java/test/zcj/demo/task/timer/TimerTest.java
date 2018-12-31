package test.zcj.demo.task.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer 的设计核心是一个 TaskList 和一个 TaskThread 。
 * TimerThread 在创建 Timer 时会启动成为一个守护线程。
 * TimerThread 找到最近要执行的任务-->休眠-->该执行的时候唤醒-->执行-->找到最近要执行的任务-->休眠--...
 * 所有任务同一线程、串行。
 * @deprecated 用 ScheduledThreadPool 替代
 */
public class TimerTest {
	
	public static void main(String[] args) {
		Timer myTimer = new Timer();
		myTimer.schedule(new MyTimeTask(), 1000, 1000);// 一秒后每秒执行一次
		myTimer.schedule(new MyTimeTask2(), 2000);// 两秒后执行
	}
	
	private static final class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("go...");
		}
	}
	
	private static final class MyTimeTask2 extends TimerTask {
		@Override
		public void run() {
			System.out.println("gogogo...");
		}
	}
}