package test.zcj.demo.designpattern.other.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器
 * @author ZCJ
 * @data 2013-6-9
 */
public class MyFilter {

	public interface Filter {
		Object doFilter(Object str);
	}

	public class AFilter implements Filter {
		@Override
		public Object doFilter(Object str) {
			return str.toString().replace(":)", "<V>");
		}
	}

	public class BFilter implements Filter {
		@Override
		public Object doFilter(Object str) {
			return str.toString().replace('<', '[').replace('>', ']');
		}
	}

	public class FilterChain implements Filter {
		List<Filter> filters = new ArrayList<Filter>();
		public FilterChain addFilter(Filter f) {
			this.filters.add(f);
			return this;
		}
		@Override
		public Object doFilter(Object str) {
			Object r = str;
			for (Filter f : filters) {
				r = f.doFilter(r);
			}
			return r;
		}
	}

	public void test() {
		String msg = "大家好:)，<script>";
		FilterChain fc = new FilterChain();
		fc.addFilter(new AFilter()).addFilter(new BFilter());
		Object result = fc.doFilter(msg);
		System.out.println(result.toString());
	}
	
	public static void main(String[] args) {
		new MyFilter().test();
	}

}
