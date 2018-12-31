package test.zcj.demo.designpattern.activity.state;

/**
 * 状态设计模式
 * 
 * 	实例：
 * 		开关、投票
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class TestState {

	public abstract class State {
		public abstract void handlepush(Context c);

		public abstract void handlepull(Context c);

		public abstract void p();
	}

	public class BlueState extends State {

		public void handlepush(Context c) {
			c.setState(new GreenState());
		}

		public void handlepull(Context c) {
			c.setState(new RedState());
		}

		public void p() {
			System.out.println("blue");
		}
	}

	public class GreenState extends State {

		public void handlepush(Context c) {
			c.setState(new RedState());
		}

		public void handlepull(Context c) {
			c.setState(new BlueState());
		}

		public void p() {
			System.out.println("green");
		}
	}

	public class RedState extends State {

		public void handlepush(Context c) {
			c.setState(new BlueState());
		}

		public void handlepull(Context c) {
			c.setState(new GreenState());
		}

		public void p() {
			System.out.println("red");
		}
	}

	public class Context {

		private State state = null;

		public void push() {
			state.handlepush(this);
		}

		public void pull() {
			state.handlepull(this);
		}
		
		public void setState(State state) {
			this.state = state;
		}

		public State getState() {
			return state;
		}
	}

	public void test() {
		Context c = new Context();
		c.setState(new RedState());
		c.pull();
		c.getState().p();
		c.pull();
		c.getState().p();
		c.pull();
		c.getState().p();
		c.pull();
		c.getState().p();
	}

	public static void main(String[] args) {
		new TestState().test();
	}
}
