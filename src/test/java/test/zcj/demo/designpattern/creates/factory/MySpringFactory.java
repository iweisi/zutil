package test.zcj.demo.designpattern.creates.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 3、Spring 的工厂模式（抽象工厂模式）
 * @author ZCJ
 * @data 2012-12-31
 */
public class MySpringFactory {

//	 applicationContext.xml
//	 <beans>
//	 <bean id="v" class="com.bjsxt.spring.factory.Train"></bean>
//	 </beans>

	public interface Moveable {
		void run();
	}
	public class Car implements Moveable {
		public void run() {
			System.out.println("car.......");
		}
	}
	public class Train implements Moveable {
		public void run() {
			System.out.println("train......");
		}
	}

	public interface BeanFactory {
		Object getBean(String id);
	}
	public class ClassPathXmlApplicationContext implements BeanFactory {
		private Map<String, Object> container = new HashMap<String, Object>();
		public ClassPathXmlApplicationContext(String fileName) throws Exception {
			// ...
			container.put("v", Class.forName("com.bjsxt.spring.factory.Train").newInstance());
		}
		@Override
		public Object getBean(String id) {
			return container.get(id);
		}
	}
	
	public class Test {
		public void myTest() throws Exception {
			BeanFactory f = new ClassPathXmlApplicationContext("com/bjsxt/spring/factory/applicationContext.xml");
			Object o = f.getBean("v");
			Moveable m = (Moveable)o;
			m.run();
		}
	}

}
