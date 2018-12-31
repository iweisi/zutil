package test.zcj.demo.designpattern.other.ioc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestIoc {

	// 1、定义一个Factory接口，提供getBean方法
	public interface BeanFactory {
		public Object getBean(String name);
	}

	// 2、编写一个Factory类，实现getBean方法，同时提供构造方法用于读取配置文件里的所有bean
	public class PropertiesBeanFactory implements BeanFactory {
		Map<String, Object> beans = new HashMap<String, Object>();

		public PropertiesBeanFactory(String configurationFile) {
			try {
				Properties props = new Properties();
				props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configurationFile));
				Set<?> set = props.entrySet();
				for (Iterator<?> iterator = set.iterator(); iterator.hasNext();) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey(); // DAO的名称
					String className = (String) entry.getValue(); // 全路径类名
					Class<?> clz = Class.forName(className);
					Object bean = clz.newInstance(); // 预先创建好的DAO对象
					beans.put(key, bean); // 缓存DAO对象
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object getBean(String name) {
			return beans.get(name);
		}
	}

	// 3、在web.xml里配置InitBeanFactoryServlet，构造PropertiesBeanFactory对象放入ServletContext中
	public class InitBeanFactoryServlet extends HttpServlet {

		private static final long serialVersionUID = 576490175963333716L;

		public static final String INIT_FACTORY_NAME = "_my_bean_factory";

		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
			String configLocation = config.getInitParameter("configLocation");
			BeanFactory factory = new PropertiesBeanFactory(configLocation);
			getServletContext().setAttribute(INIT_FACTORY_NAME, factory);
		}
	}

	// 5、编写BaseServlet，在ServletContext中拿出需要用的bean，利用反射，将依赖对象articleDao注入到ArticleServlet中
	public class BaseServlet extends HttpServlet {

		private static final long serialVersionUID = 3350407615905611229L;

		@Override
		protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			BeanFactory factory = (BeanFactory) getServletContext().getAttribute(InitBeanFactoryServlet.INIT_FACTORY_NAME);

			// 利用反射，调用this对象中的相关的setters方法！
			Method[] methods = this.getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					String propertyName = m.getName().substring(3);// TestService
					StringBuffer sb = new StringBuffer(propertyName);
					sb.replace(0, 1, (propertyName.charAt(0) + "").toLowerCase());
					propertyName = sb.toString();// testService
					Object bean = factory.getBean(propertyName);
					try {
						m.invoke(this, bean);// 将依赖对象注入客户端==this.m(bean)
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public class ArticleServlet extends BaseServlet {

		private static final long serialVersionUID = -1242712377030741530L;

		private TestService testService;

		public int doing() {
			return testService.add(1, 2);
		}

		public void setTestService(TestService testService) {
			this.testService = testService;
		}
	}

}
