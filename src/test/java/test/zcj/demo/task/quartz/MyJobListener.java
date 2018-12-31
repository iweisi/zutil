package test.zcj.demo.task.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;

public class MyJobListener implements JobListener {
	
	private String name;
	
	public MyJobListener(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		// Scheduler 在 JobDetail 被执行之后调用这个方法。
		if(jobException != null){
			try {
				context.getScheduler().shutdown();//停止Scheduler
				System.out.println("Error occurs when executing jobs, shut down the scheduler ");
				// 给管理员发送邮件…
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// Scheduler 在 JobDetail 将要被执行时调用这个方法。
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
	}
}