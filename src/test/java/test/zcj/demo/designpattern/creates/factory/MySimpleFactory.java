package test.zcj.demo.designpattern.creates.factory;

/**
1、简单工厂(静态工厂模式)：
	将产品的实例化封装起来,调用者不用关心实例化过程，只需依赖工厂。
	定义一个用于创建对象的接口，让子类决定实例化哪一个类。Factory Method 使一个类的实例化延迟到其子类。

interface MoveAble {run();}
	class Car implements MoveAble {run(){...}}
	class Plane implements MoveAble {run(){...}}
	class Broom implements MoveAble {run(){...}}
	
abstract class IFactory {MoveAble create();}
	|_class CarFactory {MoveAble create(){return new Car();}}
	|_class PlaneFactory {MoveAble create(){return new Plane();}}
	|_class BroomFactory {MoveAble create(){return new Broom();}}

Test.java
	IFactory factory = new BroomFactory();
	MoveAble m = factory.create();
	m.run();

 */
public class MySimpleFactory {
	
}