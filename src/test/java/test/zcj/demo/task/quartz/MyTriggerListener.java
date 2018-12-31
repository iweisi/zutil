package test.zcj.demo.task.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class MyTriggerListener implements TriggerListener {

	private String name;

	public MyTriggerListener(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// trigger 被触发，Job 执行前
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// 假如返回 true，这个 Job 将不会为此次 Trigger 触发而得到执行。
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// Trigger 错过触发时
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
		// 当前一次触发的 Job 完成后
	}
}
