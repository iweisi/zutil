package test.zcj.demo.designpattern.creates.prototype;

public class MyPrototype implements Cloneable {

	private String name;

	// clone方法只能复制基础数据类型（int等）和不可变的引用类型（String等）
	// 自定义对象类型只是复制了引用，修改对象的属性会同时修改复制出来的那一份
	@Override
	public MyPrototype clone() throws CloneNotSupportedException {
		return (MyPrototype) super.clone();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}