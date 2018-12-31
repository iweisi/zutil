package test.zcj.util.beanutils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class TestUtilBeans {
	
	public static void main(String[] args) throws Exception {
		TestStudent ts = TestStudent.initStudent();
		TestStudent2 ts2 = new TestStudent2();

		// 赋值和取值
		BeanUtils.setProperty(ts, "name", "zozuou");
		System.out.println(BeanUtils.getProperty(ts, "name"));
		
		// Bean转Map
		@SuppressWarnings("unchecked")
		Map<String, String> map4 = BeanUtils.describe(ts);
		System.out.println(map4);
		
		BeanUtils.copyProperties(ts2, ts);// ts里的属性拷贝到ts2里
		BeanUtils.copyProperty(ts2, "click", 999);// 设置ts2的click属性值为999
		
		// Map转Bean
		BeanUtils.populate(ts2, TestStudent.initStudentMap());
		
		System.out.println(ts2.getApps().get(0));
	}
	
}
