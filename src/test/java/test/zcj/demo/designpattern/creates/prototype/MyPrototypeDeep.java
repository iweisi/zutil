package test.zcj.demo.designpattern.creates.prototype;

public class MyPrototypeDeep implements Cloneable {

	private String id;
	private MyPrototype prototype;

	// 深复制，所有属性都额外复制一份
	public MyPrototypeDeep clone() throws CloneNotSupportedException {
		MyPrototypeDeep ret = (MyPrototypeDeep) super.clone();
		ret.prototype = this.prototype.clone();
		return ret;
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
