package test.zcj.demo.designpattern.creates.builder;

/**
 * 生成器模式(建造者模式)</br>
 * 
 * 	意图：
 * 		将一个复杂对象的构建(ConcreteBuilder的方法)与它的表示(Director的方法)分离，使得同样的构建过程可以创建不同的表示。
 * 	适用环境：
 * 		当我们要创建的对象很复杂的时候（通常是由很多其他的对象组合而成）。
 * 
 */
public class MyBuilder {

	/** 产品类 */
	class Product {
		private String name;
		private String type;
		private Integer age;

		public void showProduct() {
			System.out.print("名称：" + name + ";");
			System.out.print("型号：" + type + ";");
			System.out.print("年限：" + age + ";");
			System.out.println();
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}

	/** 抽象建造者 */
	abstract class Builder {
		public abstract void setMustPart(String arg1, String arg2);

		public abstract void setNoMustPart(Integer arg1);

		public abstract Product getProduct();
	}

	/** 建造者(提供产品各部件的构建方法；返回组建好的产品。) */
	class ConcreteBuilder extends Builder {
		private Product product = new Product();

		public void setMustPart(String arg1, String arg2) {
			product.setName(arg1);
			product.setType(arg2);
		}

		public void setNoMustPart(Integer arg1) {
			product.setAge(arg1);
		}

		public Product getProduct() {
			return product;
		}
	}

	/** 导演类(负责调用适当的建造者来组建产品，导演类一般不与产品类发生依赖关系，而是与建造者类直接交互。) */
	public class Director {

		public Product getAProduct() {
			Builder builder = new ConcreteBuilder();
			builder.setMustPart("宝马汽车", "X7");
			builder.setNoMustPart(11);
			return builder.getProduct();
		}

		public Product getBProduct() {
			Builder builder = new ConcreteBuilder();
			builder.setMustPart("奥迪汽车", "Q5");
			return builder.getProduct();
		}
	}

	public void test1() {
		Director director = new Director();

		Product product1 = director.getAProduct();
		product1.showProduct();

		Product product2 = director.getBProduct();
		product2.showProduct();
	}

	public static void main(String[] args) {
		new MyBuilder().test1();
	}

}
