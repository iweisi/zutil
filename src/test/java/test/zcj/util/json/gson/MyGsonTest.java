package test.zcj.util.json.gson;

import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zcj.util.json.gson.GsonExclusionStrategy;
import com.zcj.util.json.gson.GsonFieldNamingStrategy;
import com.zcj.util.json.gson.GsonInclusionStrategy;

public class MyGsonTest {

	@SuppressWarnings("unused")
	private static final Gson GSON1 = new GsonBuilder()
			.serializeNulls()
			// 显示 null 的数据
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			// 格式化时间格式的数据
			.registerTypeAdapter(Date.class, new MyGsonDateTypeAdapter())
			// 添加时间类型转换器，可对时间进行任意处理
			.excludeFieldsWithoutExposeAnnotation()
			// 排除没有@Expose注解的字段
			.setExclusionStrategies(
					new GsonExclusionStrategy(new String[] { "descr", "mapurl" }, new Class<?>[] { List.class }))
			// 自定义排除指定属性和类型
			.setFieldNamingStrategy(new GsonFieldNamingStrategy(new String[] { "descr" }, new String[] { "theDescr" }))
			// 修改指定字段名称
			.setPrettyPrinting()
			// 返回优美的JSON格式
			.setVersion(1.0)
			// 忽略1.0版本以后的字段
			.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
			// 默认排除static、transient、volatile修饰的字段,如果需要显示，则去掉需要显示的那个
			.create();

	@Test
	public void test1() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.setExclusionStrategies(new GsonInclusionStrategy(new String[] { "name" })).create();
		List<Student> slist = Student.initStudentList();
		String s = gson.toJson(slist);
		System.out.println(s);
	}

	@Test
	public void t2() {
		List<Student> students = Student.initStudentList();
		String studentJson = "{'id':12,'name':'sddddss'}";

		String result = new GsonBuilder().setVersion(1.0).create().toJson(students);
		System.out.println(result);

		List<Student> s = new Gson().fromJson(result, new TypeToken<List<Student>>() {
		}.getType());
		System.out.println(s.get(0).getId());

		Student ss = new Gson().fromJson(studentJson, Student.class);
		System.out.println(ss.getName());

		// test1();
	}

	@Test
	public void test3() {
		String jsonString = "{'s':1,'d':{'id':12,'name':'sddddss'}}";
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonString).getAsJsonObject();
		String subJsonString = jsonObject.get("d").getAsJsonObject().toString();
		System.out.println(subJsonString);
	}
}
