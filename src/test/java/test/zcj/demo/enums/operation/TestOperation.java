package test.zcj.demo.enums.operation;

import java.util.Arrays;
import java.util.Collection;

public class TestOperation {

	// 可用于在枚举中定义一系列操作，然后一次调用自动让所有操作都走一遍的功能
	public static void main(String args[]) {
		double x = 1.5;
		double y = 2;
		test(BasicOperation.class, x, y);
		System.out.println("----");
		test(Arrays.asList(BasicOperation.values()), x, y);
		test(Arrays.asList(ExtendedOperation.values()), x, y);
		System.out.println("----");
		System.out.println(BasicOperation.MINUS.apply(x, y));
	}

	private static <T extends Enum<T> & Operation> void test(Class<T> opSet, double x, double y) {
		for (Operation op : opSet.getEnumConstants()) {
			System.out.println(x + " " + op + " " + y + " = " + op.apply(x, y));
		}
	}

	private static void test(Collection<? extends Operation> opSet, double x, double y) {
		for (Operation op : opSet) {
			System.out.println(x + " " + op + " " + y + " = " + op.apply(x, y));
		}
	}

}