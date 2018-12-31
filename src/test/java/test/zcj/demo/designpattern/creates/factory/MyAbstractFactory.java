package test.zcj.demo.designpattern.creates.factory;

/**
 * 2、抽象工厂模式
 * 	用来创建一组相关或者相互依赖的对象。 
 * 	产生新的产品品种的时候改动太大。
 */
public class MyAbstractFactory {

	interface IProductA {
		public void showA();
	}

	interface IProductB {
		public void showB();
	}

	class ProductA1 implements IProductA {
		public void showA() {
			System.out.println("A1");
		}
	}

	class ProductA2 implements IProductA {
		@Override
		public void showA() {
			System.out.println("A2");
		}
	}

	class ProductB1 implements IProductB {
		public void showB() {
			System.out.println("B1");
		}
	}

	class ProductB2 implements IProductB {
		@Override
		public void showB() {
			System.out.println("B2");
		}
	}

	interface IFactory {
		public IProductA createProductA();

		public IProductB createProductB();
	}

	class Factory1 implements IFactory {
		public IProductA createProductA() {
			return new ProductA1();
		}

		public IProductB createProductB() {
			return new ProductB1();
		}
	}

	class Factory2 implements IFactory {
		public IProductA createProductA() {
			return new ProductA2();
		}

		public IProductB createProductB() {
			return new ProductB2();
		}
	}

	public void test() {
		IFactory factory = new Factory1();
		factory.createProductA().showA();
		factory.createProductB().showB();
	}

	public static void main(String[] args) {
		new MyAbstractFactory().test();
	}

}
