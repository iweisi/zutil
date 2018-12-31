package test.zcj.demo.designpattern.activity.strategy;

/**
 * 策略设计模式
 * 
 * 	意图：
 * 		把一系列算法封装到实现相同接口的各个类里，相互之间可以替换。
 * 		实质上就是面向对象中的继承和多态。
 * 	实例：
 * 		接口Comparable、Comparator、大小比较器（可根据不同的属性比较大小）
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class TestStrategy {

	public abstract class AbstractStrategy {
		public abstract void algorithm();
	}

	public class ConcreteStrategy1 extends AbstractStrategy {
		@Override
		public void algorithm() {
			System.out.println("----------------我是策略一算法----------------");
		}
	}

	public class ConcreteStrategy2 extends AbstractStrategy {
		@Override
		public void algorithm() {
			System.out.println("----------------我是策略二算法----------------");
		}
	}

	// 存在的意义：1、存放不同的策略内的共同部分；
	public class Context {
		private AbstractStrategy strategy;

		public Context(AbstractStrategy strategy) {
			this.strategy = strategy;
		}

		public void algorithm() {
			this.strategy.algorithm();
		}
	}

	public void test() {
		Context c = new Context(new ConcreteStrategy1());
		c.algorithm();
	}

	public static void main(String[] args) {
		new TestStrategy().test();
	}
}
