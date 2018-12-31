package test.zcj.demo.designpattern.activity.state;

public class TestState2 {

	public interface State {
		public void doAction(Context context);
	}

	public class State1 implements State {
		public void doAction(Context context) {
			System.out.println("State1状态下做什么事情");
			context.setState(this);
		}

		public String toString() {
			return "当前状态：State1";
		}
	}

	public class State2 implements State {
		public void doAction(Context context) {
			System.out.println("State2状态下做什么事情");
			context.setState(this);
		}

		public String toString() {
			return "当前状态：State2";
		}
	}

	public class Context {
		private State state;

		public Context() {
			state = null;
		}

		public void setState(State state) {
			this.state = state;
		}

		public State getState() {
			return state;
		}
	}

	public void test() {
		Context context = new Context();

		State1 start1 = new State1();
		start1.doAction(context);

		System.out.println(context.getState().toString());

		State2 state2 = new State2();
		state2.doAction(context);

		System.out.println(context.getState().toString());
	}

	public static void main(String[] args) {
		new TestState2().test();
	}
}
