package test.zcj.demo.designpattern.activity.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者设计模式
 * 
 * 	增加新的访问者无须修改原有系统
 * 
 * 	例子1：
 * 		顾客在超市中将选择的商品，如苹果、图书等放在购物车中，然后到收银员处付款。
 * 		在购物过程中，顾客需要对这些商品进行访问，以便确认这些商品的质量。
 * 		之后收银员计算价格时也需要访问购物车内顾客所选择的商品。
 * 		此时，购物车作为一个ObjectStructure（对象结构）用于存储各种类型的商品，而顾客和收银员作为访问这些商品的访问者，他们需要对商品进行检查和计价。
 * 	
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class TestVisitor {

	// 抽象被访问者
	public interface ElementAble {
		public void accept(VisitorAble visitor);
	}

	// 具体被访问者（苹果）
	public class ElementA implements ElementAble {
		@Override
		public void accept(final VisitorAble visitor) {// 苹果 被 访问者 访问
			visitor.visit(this);
		}
	}

	// 具体被访问者（图书）
	public class ElementB implements ElementAble {
		@Override
		public void accept(final VisitorAble visitor) {// 图书 被 访问者 访问
			visitor.visit(this);
		}
	}
	
	// 抽象访问者
	public interface VisitorAble {
		public void visit(ElementA elementa);

		public void visit(ElementB elementb);
	}

	// 具体访问者（顾客）
	public class VisitorA implements VisitorAble {
		@Override
		public void visit(final ElementA elementa) {
			System.out.println("顾客 访问 苹果");
		}

		@Override
		public void visit(final ElementB elementb) {
			System.out.println("顾客 访问 图书");
		}
	}

	// 具体访问者（收银员）
	public class VisitorB implements VisitorAble {
		@Override
		public void visit(ElementA elementa) {
			System.out.println("收银员 访问 苹果");
		}

		@Override
		public void visit(ElementB elementb) {
			System.out.println("收银员 访问 图书");
		}
	}

	// （购物车）
	public class ObjectStructure {
		private final List<ElementAble> elements = new ArrayList<ElementAble>();

		public void addElement(final ElementAble e) {
			elements.add(e);
		}

		public void removeElement(final ElementAble e) {
			elements.remove(e);
		}

		// 所有元素（苹果和图书）被访问
		public void accept(final VisitorAble visitor) {
			for (final ElementAble e : elements) {
				e.accept(visitor);
			}
		}
	}

	public void test() {
		
		// 创建 购物车 并添加 苹果 和 图书
		final ObjectStructure os = new ObjectStructure();
		os.addElement(new ElementA());
		os.addElement(new ElementB());

		final VisitorAble gVisitor = new VisitorA();
		os.accept(gVisitor);// 购物车 被 顾客 检查

		final VisitorAble chVisitor = new VisitorB();
		os.accept(chVisitor);// 购物车 被 收银员 检查
	}

	public static void main(final String[] args) {
		new TestVisitor().test();
	}

}
