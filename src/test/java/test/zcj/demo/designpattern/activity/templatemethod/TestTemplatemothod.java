package test.zcj.demo.designpattern.activity.templatemethod;

/**
 * 模板设计模式
 * 
 * 	意图：
 * 		定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class TestTemplatemothod {

	abstract class AbstractSort {

		protected abstract void sort(int[] array);

		public void showSortResult(int[] array) {
			this.sort(array);
			System.out.print("排序结果：");
			for (int i = 0; i < array.length; i++) {
				System.out.printf("%3s", array[i]);
			}
		}
	}

	public class ConcreteSort extends AbstractSort {
		@Override
		protected void sort(int[] array) {
			// ...
		}
	}

	public void test() {
		int[] a = { 10, 32, 1, 9, 5, 7, 12, 0, 4, 3 }; // 预设数据数组
		AbstractSort s = new ConcreteSort();
		s.showSortResult(a);
	}

	public static void main(String[] args) {
		new TestTemplatemothod().test();
	}
}
