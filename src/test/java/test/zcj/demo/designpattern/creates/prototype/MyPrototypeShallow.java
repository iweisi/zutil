package test.zcj.demo.designpattern.creates.prototype;

public class MyPrototypeShallow implements Cloneable {

	private String id;
	private MyPrototype prototype;

	// 浅复制，id属性独立一份，prototype对象复制了引用
	public MyPrototypeShallow clone() throws CloneNotSupportedException {
		return (MyPrototypeShallow) super.clone();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MyPrototype getPrototype() {
		return prototype;
	}

	public void setPrototype(MyPrototype prototype) {
		this.prototype = prototype;
	}
}