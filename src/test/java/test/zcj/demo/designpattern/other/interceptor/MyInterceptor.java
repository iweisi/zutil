package test.zcj.demo.designpattern.other.interceptor;

import java.util.ArrayList;
import java.util.List;

public class MyInterceptor {

	public class Action {
		public void execute() {
			System.out.println("execute!");
		}
	}
	
	public interface Interceptor {
		public void intercept(ActionInvocation invocation) ;
	}
	
	public class FirstInterceptor implements Interceptor {
		@Override
		public void intercept(ActionInvocation invocation) {
			System.out.println(1);
			invocation.invoke();
			System.out.println(-1);
		}
	}
	
	public class SecondInterceptor implements Interceptor {
		@Override
		public void intercept(ActionInvocation invocation) {
			System.out.println(2);
			invocation.invoke();
			System.out.println(-2);
		}
	}
	
	public class ActionInvocation {
		List<Interceptor> interceptors = new ArrayList<Interceptor>();
		int index = -1;
		Action a = new Action();
		
		public ActionInvocation() {
			this.interceptors.add(new FirstInterceptor());
			this.interceptors.add(new SecondInterceptor());
		}
		
		public void invoke() {
			index ++;
			if(index >= this.interceptors.size()) {
				a.execute();
			}else {
				this.interceptors.get(index).intercept(this);
			}
		}
	}
	
	public void test() {
		new ActionInvocation().invoke();
	}
	
	public static void main(String[] args) {
		new MyInterceptor().test();
	}
}
