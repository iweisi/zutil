package test.zcj.demo.designpattern.creates.builder;

/**
 * 构造器链式写法（先通过构造器定义参数，最后才实例化需要的类）
 * 
 * @author zouchongjin@sina.com
 * @data 2017年6月16日
 */
public class MyBuilder2 {

	private final String name;
	private final Integer age;

	private MyBuilder2(Builder builder) {
		name = builder.name;
		age = builder.age;
	}

	public static class Builder {
		private String name = null;
		private Integer age = null;

		public Builder(String name) {
			this.name = name;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder age(Integer age) {
			this.age = age;
			return this;
		}

		// 最后才构建需要的对象
		public MyBuilder2 build() {
			return new MyBuilder2(this);
		}
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public static void main(String[] args) {
		MyBuilder2 b = new MyBuilder2.Builder("abc").age(18).build();
		System.out.println(b.getName());
	}
}
