package test.zcj.demo.designpattern.forms.adapter;

/**
 * 适配器设计模式
 * 
 * 	意图：
 * 		将一个类的接口转换成客户希望的另外一个接口。Adapter 模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * 	适用环境：
 * 		你想使用一个已经存在的类，而它的接口不符合你的需求。 
 * 		你想创建一个可以复用的类，该类可以与其他不相关的类或不可预见的类（即那些接口可能不一定兼容的类）协同工作。
 * 		你想使用一些已经存在的子类，但是不可能对每一个都进行子类化以匹配它们的接口。对象适配器可以适配它的父类接口。（仅适用于对象Adapter）
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月8日
 */
public class TestAdapter {

	// 【已存在】
	public interface MyService {
		void run();
	}

	// 【已存在】
	public class MyServiceImpl implements MyService {
		@Override
		public void run() {
			System.out.println("标准的实现");
		}
	}

	// 【已存在】 但是使用方法和MyServiceImpl不一样的类
	public class OtherServiceImpl {
		public void otherRun() {
			System.out.println("特殊的实现");
		}
	}

	// 适配器实现方法一：类适配器，当希望将一个类转换成满足另一个新接口的类时
	public class OtherAdapter extends OtherServiceImpl implements MyService {
		@Override
		public void run() {
			super.otherRun();
		}
	}

	// 适配器实现方法二：对象适配器，当希望将一个对象转换成满足另一个新接口的对象时
	public class OtherAdapter2 implements MyService {
		private OtherServiceImpl otherService;

		public OtherAdapter2(OtherServiceImpl otherService) {
			this.otherService = otherService;
		}

		@Override
		public void run() {
			otherService.otherRun();
		}
	}

	public void test() {
		MyService service = new MyServiceImpl();
		service.run();

		MyService service2 = new OtherAdapter();
		service2.run();

		MyService service3 = new OtherAdapter2(new OtherServiceImpl());
		service3.run();
	}
	
	public static void main(String[] args) {
		new TestAdapter().test();
	}
}
