package test.zcj.demo.designpattern.forms.bridge;

/**
 * 桥接设计模式
 * 
 * 	意图：
 * 		将抽象部分与它的实现部分分离,使它们都可以独立地变化。
 * 
 * 	适用环境：
 * 		如果子类有多个维度排列组合，需要解决耦合问题，就用桥接模式（多对多）。
 * 
 * @author ZCJ
 * @data 2012-11-8
 */
public class MyBridge {

	// 抽象的给礼物方式
	interface GiftImpl {
		String t1();
	}

	// 具体的给礼物方式
	public class GiftImplA implements GiftImpl {
		@Override
		public String t1() {
			return "快递";
		}
	}

	// 具体的给礼物方式
	public class GiftImplB implements GiftImpl {
		@Override
		public String t1() {
			return "亲手送";
		}
	}

	/** 定义抽象的礼物 */
	abstract class Gift {
		protected GiftImpl impl;

		abstract String t2();
	}

	/** 定义礼物C */
	public class GiftC extends Gift {
		public GiftC(GiftImpl impl) {
			this.impl = impl;
		}

		@Override
		String t2() {
			return impl.t1() + "鲜花";
		}
	}

	/** 定义礼物D */
	public class GiftD extends Gift {
		public GiftD(GiftImpl impl) {
			this.impl = impl;
		}

		@Override
		String t2() {
			return impl.t1() + "巧克力";
		}
	}

	public void test() {
		Gift a = new GiftD(new GiftImplB());
		System.out.println(a.t2());

		Gift b = new GiftC(new GiftImplA());
		System.out.println(b.t2());
	}

	public static void main(String[] args) {
		new MyBridge().test();
	}

}
