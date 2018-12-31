package test.zcj.demo.designpattern.creates.singleton;

/**
 * 单例模式（懒汉式单例）
 * 
 * @author ZCJ
 * @data 2012-12-30
 */
public class MySingleton2 {
	
	private static MySingleton2 singleton;

	private MySingleton2() {
		
	}

	public static synchronized MySingleton2 getInstance() {
		if (singleton == null) {
			singleton = new MySingleton2();
		}
		return singleton;
	}
}
