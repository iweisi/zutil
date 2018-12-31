package test.zcj.demo.designpattern.creates.singleton;

/**
 * 单例模式（饿汉式单例）（优先使用）
 * <p>
 * <p>
 * 不要使用反射，否则将会实例化一个新对象。
 * <p>
 * 不要做断开单例类对象与类中静态引用的危险操作。
 * <p>
 * 多线程使用时，注意线程安全问题。
 * 
 * @author ZCJ
 * @data 2012-12-30
 */
public class MySingleton {

	private static final MySingleton singleton = new MySingleton();

	private MySingleton() {

	}

	public static MySingleton getInstance() {
		return singleton;
	}
}
