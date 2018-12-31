package test.zcj.demo.designpattern.other.filter;

import java.util.ArrayList;
import java.util.List;

public class MyWebFilter {
	
	public class Agr {
		public String agra;
		public String agrb;
	}

	public interface Filter {
		void doFilter(Object agr, FilterChain chain);
	}

	public class AFilter implements Filter {
		@Override
		public void doFilter(Object agr, FilterChain chain) {
			((Agr)agr).agra += "---HTMLFilter()";
			chain.doFilter(agr, chain);
			((Agr)agr).agrb += "---HTMLFilter()";
		}
	}

	public class BFilter implements Filter {
		@Override
		public void doFilter(Object agr, FilterChain chain) {
			((Agr)agr).agra += "---SesitiveFilter()";
			chain.doFilter(agr, chain);
			((Agr)agr).agrb += "---SesitiveFilter()";
		}
	}

	public class FilterChain implements Filter {
		List<Filter> filters = new ArrayList<Filter>();
		int index = 0;

		public FilterChain addFilter(Filter f) {
			this.filters.add(f);
			return this;
		}

		@Override
		public void doFilter(Object agr, FilterChain chain) {
			if (index == filters.size())
				return;

			Filter f = filters.get(index);
			index++;
			f.doFilter(agr, chain);
		}
	}
	
	public void test() {
		Agr agr = new Agr();
		agr.agra = "111";
		agr.agrb = "222";
		
		FilterChain fc = new FilterChain();
		fc.addFilter(new AFilter()).addFilter(new BFilter());
		fc.doFilter(agr, fc);
		
		System.out.println(agr.agra);
		System.out.println(agr.agrb);
	}
	
	public static void main(String[] args) {
		new MyWebFilter().test();
	}

}
