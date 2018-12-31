package test.zcj.demo.designpattern.activity.mediator;

/**
 * 中介者设计模式
 * 
 * 	意图：
 * 		用一个中介对象来封装一系列的对象交互。中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * 
 * 	适用环境：
 * 		当类关系错综复杂的时候，另建一个调停者类，让所有类只对这个调停者有关系。
 * 
 * 	实例：
 * 		MVC中的C
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月11日
 */
public class MyMediator {
	
	abstract class AbstractColleague {
		protected int number;

		public int getNumber() {
			return number;
		}

		public void setNumber(int number){
			this.number = number;
		}
		//注意这里的参数不再是同事类，而是一个中介者
		public abstract void setNumber(int number, AbstractMediator am);
	}

	class ColleagueA extends AbstractColleague{

		public void setNumber(int number, AbstractMediator am) {
			this.number = number;
			am.AaffectB();
		}
	}

	class ColleagueB extends AbstractColleague{

		@Override
		public void setNumber(int number, AbstractMediator am) {
			this.number = number;
			am.BaffectA();
		}
	}

	abstract class AbstractMediator {
		protected AbstractColleague A;
		protected AbstractColleague B;
		
		public AbstractMediator(AbstractColleague a, AbstractColleague b) {
			A = a;
			B = b;
		}

		public abstract void AaffectB();
		
		public abstract void BaffectA();

	}
	class Mediator extends AbstractMediator {

		public Mediator(AbstractColleague a, AbstractColleague b) {
			super(a, b);
		}

		//处理A对B的影响
		public void AaffectB() {
			int number = A.getNumber();
			B.setNumber(number*100);
		}

		//处理B对A的影响
		public void BaffectA() {
			int number = B.getNumber();
			A.setNumber(number/100);
		}
	}

	public class Client {
		public void test() {
			AbstractColleague collA = new ColleagueA();
			AbstractColleague collB = new ColleagueB();
			
			AbstractMediator am = new Mediator(collA, collB);
			
			System.out.println("==========通过设置A影响B==========");
			collA.setNumber(1000, am);
			System.out.println("collA的number值为："+collA.getNumber());
			System.out.println("collB的number值为A的10倍："+collB.getNumber());

			System.out.println("==========通过设置B影响A==========");
			collB.setNumber(1000, am);
			System.out.println("collB的number值为："+collB.getNumber());
			System.out.println("collA的number值为B的0.1倍："+collA.getNumber());
			
		}
	}

}
