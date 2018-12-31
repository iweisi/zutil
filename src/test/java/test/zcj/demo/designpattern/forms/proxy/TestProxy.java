package test.zcj.demo.designpattern.forms.proxy;

/**
 * 代理设计模式
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月28日
 */
public class TestProxy {

	public interface Image {
		void display();
	}

	// 真实对象
	public class RealImage implements Image {
		private String fileName;

		public RealImage(String fileName) {
			this.fileName = fileName;
			loadFromDisk(fileName);
		}

		@Override
		public void display() {
			System.out.println("Displaying " + fileName);
		}

		private void loadFromDisk(String fileName) {
			System.out.println("Loading " + fileName);
		}
	}

	// 代理对象，需要和真实对象实现同一个接口
	public class ProxyImage implements Image {
		private String fileName;
		private RealImage realImage;

		public ProxyImage(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public void display() {
			if (realImage == null) {
				realImage = new RealImage(fileName);
			}
			realImage.display();
		}
	}

	public void test() {
		Image image = new ProxyImage("test_10mb.jpg");
		image.display();
	}

	public static void main(String[] args) {
		new TestProxy().test();
	}

}
