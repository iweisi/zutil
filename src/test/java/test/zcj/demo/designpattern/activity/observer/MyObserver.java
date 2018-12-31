package test.zcj.demo.designpattern.activity.observer;

import java.util.Vector;

/**
 * 观察者设计模式
 * 
 * 	意图：
 * 		定义对象间一种一对多的依赖关系，使得当每一个对象改变状态，则所有依赖于它的对象都会得到通知并自动更新。
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class MyObserver {

	/** 被观察者 */
	abstract class Subject {
		private Vector<Observer> obs = new Vector<Observer>();

		public void addObserver(Observer obs) {
			this.obs.add(obs);
		}

		public void delObserver(Observer obs) {
			this.obs.remove(obs);
		}

		protected void notifyObserver() {
			for (Observer o : obs) {
				o.update();
			}
		}

		public abstract void doSomething();
	}

	/** 具体的被观察者 */
	class ConcreteSubject extends Subject {
		public void doSomething() {
			System.out.println("被观察者事件发生");
			this.notifyObserver();
		}
	}

	/** 观察者 */
	interface Observer {
		public void update();
	}

	/** 具体的观察者 */
	class ConcreteObserver1 implements Observer {
		public void update() {
			System.out.println("观察者1收到信息，并进行处理。");
		}
	}

	/** 具体的观察者 */
	class ConcreteObserver2 implements Observer {
		public void update() {
			System.out.println("观察者2收到信息，并进行处理。");
		}
	}

	public void test() {
		Subject sub = new ConcreteSubject();
		sub.addObserver(new ConcreteObserver1()); // 添加观察者1
		sub.addObserver(new ConcreteObserver2()); // 添加观察者2
		sub.doSomething();
	}
	
	public static void main(String[] args) {
		new MyObserver().test();
	}

}
