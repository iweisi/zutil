package test.zcj.demo.designpattern.activity.chainofresponsibility;

/**
 * 职责链设计模式
 * 
 * 	意图：
 * 		将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。
 * 		使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。
 * 
 * 	实例：
 * 		Tomcat中的Filter
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class TestCor {

	// 抽象处理者角色类
	public abstract class Handler {

		// 下一个处理者
		protected Handler successor = null;

		// 处理方法
		public abstract String handleFeeRequest(double price);

		public Handler getSuccessor() {
			return successor;
		}

		public void setSuccessor(Handler successor) {
			this.successor = successor;
		}
	}

	// 具体处理者
	public class ProjectManager extends Handler {
		@Override
		public String handleFeeRequest(double price) {
			if (price <= 500) {
				return "ProjectManager 处理了这个请求";
			} else {
				if (getSuccessor() != null) {
					return getSuccessor().handleFeeRequest(price);
				} else {
					return "没有一个人能处理此请求";
				}
			}
		}
	}

	// 具体处理者
	public class DeptManager extends Handler {
		@Override
		public String handleFeeRequest(double price) {
			if (price <= 1000) {
				return "DeptManager 处理了这个请求";
			} else {
				if (getSuccessor() != null) {
					return getSuccessor().handleFeeRequest(price);
				} else {
					return "没有一个人能处理此请求";
				}
			}
		}
	}

	public void test() {
		Handler h1 = new ProjectManager();
		Handler h2 = new DeptManager();
		h1.setSuccessor(h2);// 设置h1的下一个处理者是h2
		System.out.println(h1.handleFeeRequest(800));
	}

	public static void main(String[] args) {
		new TestCor().test();
	}

}
