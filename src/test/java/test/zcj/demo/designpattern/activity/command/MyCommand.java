package test.zcj.demo.designpattern.activity.command;

import java.util.ArrayList;
import java.util.List;

/**
 * "命令"设计模式（"公共"设计模式）
 * 	
 * 		注册一个个命令,选择需要的命令添加到命令集合里,一起执行。
 * 
 * @author ZCJ
 * @data 2012-11-8
 */
public class MyCommand {

	/** 定义一个抽象的命令 */
	public abstract class Command {
		public abstract void execute();
	}

	/** 定义一个具体的命令A */
	public class ACommand extends Command {
		public void execute() {
			// doingA...
		}
	}

	/** 定义一个具体的命令B */
	public class BCommand extends Command {
		public void execute() {
			// doingB...
		}
	}

	/** 定义：命令的集合、命令的添加方法、命令的执行方法 */
	public class Boy {
		private List<Command> commands = new ArrayList<Command>();

		public void addCommand(Command A) {
			this.commands.add(A);
		}

		public void executeCommands() {
			for (Command c : commands) {
				c.execute();
			}
		}
	}

	/** 调用：生成命令、命令的添加、命令的执行 */
	public class MM {
		public void order(Boy b) {
			b.addCommand(new ACommand());
			b.addCommand(new BCommand());
			b.executeCommands();
		}
	}

}
