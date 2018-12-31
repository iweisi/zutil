package test.zcj.demo.designpattern.activity.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 使用JDK自带的观察者设计模式
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月28日
 */
public class TestJdkObserver {

	/** 被观察者 */
	public class Monitore1 extends Observable {
		public void sendMessage(String str) {
			this.setChanged();
			this.notifyObservers(str);// 最后加入的观察者最先被调用update()方法
		}
	}

	/** 观察者1 */
	public class Watcher1 implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			System.out.println("Watcher1 receive:" + (String) arg);
		}
	}

	/** 观察者2 */
	public class Watcher2 implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			System.out.println("Watcher2 receive:" + (String) arg);
		}
	}

	public void test() {
		Monitore1 monitored = new Monitore1();
		Watcher1 watcher1 = new Watcher1();
		Watcher2 watcher2 = new Watcher2();
		monitored.addObserver(watcher1);
		monitored.addObserver(watcher2);
		monitored.sendMessage("这是一条消息");
	}

	public static void main(String[] args) {
		new TestJdkObserver().test();
	}
}
