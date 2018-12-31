package test.zcj.demo.designpattern.forms.decorator;

/**
 * 装饰者设计模式
 * 
 * 意图： 动态地给一个对象添加一些额外的职责（可重复添加）。就增加功能来说，Decorator模式相比生成子类更为灵活。
 * 
 * 适用环境： 需要扩展一个类的功能。 动态的为一个对象增加功能，而且还能动态撤销。
 * 
 * 缺点： 缺点：产生过多相似的对象，不易排错。
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月8日
 */
public class TestDecorator {

	// 【已存在】抽象构件
	public interface Food {
		void run();
	}

	// 【已存在】基础具体构件
	public class Noodle implements Food {
		@Override
		public void run() {
			System.out.println("一碗面");
		}
	}

	// 抽象装饰者，必须和被装饰者 implements 同一接口（都 implements Food）
	public abstract class Ext implements Food {
		private Food food;

		public Ext(Food food) {
			this.food = food;
		}

		public void run() {
			food.run();
			ext();
		}

		/** 装饰方法 */
		abstract void ext();
	}

	// 具体装饰者
	public class ExtEgg extends Ext {
		public ExtEgg(Food food) {
			super(food);
		}

		@Override
		void ext() {
			System.out.println("加一份鸡蛋");
		}
	}

	// 具体装饰者
	public class ExtBeef extends Ext {
		public ExtBeef(Food food) {
			super(food);
		}

		@Override
		void ext() {
			System.out.println("加一份牛肉");
		}
	}

	public void test() {

		// 基础的东西（一碗面）
		Food base = new Noodle();

		// 装饰以后的东西（加两个鸡蛋一份牛肉）
		Food decorator = new ExtBeef(new ExtEgg(new ExtEgg(base)));

		decorator.run();
	}

	public static void main(String[] args) {
		new TestDecorator().test();
	}

}
