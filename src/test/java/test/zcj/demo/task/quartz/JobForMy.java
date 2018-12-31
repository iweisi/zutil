package test.zcj.demo.task.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *			Job 类 									Job 用法 
 * org.quartz.jobs.FileScanJob 			检查某个指定文件是否变化，并在文件被改变时通知到相应监听器的 Job 
 * org.quartz.jobs.FileScanListener 	在文件被修改后通知 FileScanJob 的监听器  
 * org.quartz.jobs.NativeJob 			用来执行本地程序(如 windows 下 .exe 文件) 的 Job 
 * org.quartz.jobs.NoOpJob 				什么也不做，但用来测试监听器不是很有用的。一些用户甚至仅仅用它来导致一个监听器的运行 
 * org.quartz.jobs.ee.mail.SendMailJob 	使用 JavaMail API 发送 e-mail 的 Job 
 * org.quartz.jobs.ee.jmx.JMXInvokerJob 调用 JMX bean 上的方法的 Job 
 * org.quartz.jobs.ee.ejb.EJBInvokerJob 用来调用 EJB 上方法的 Job 
 */
public class JobForMy implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
//		System.out.println(context.getJobDetail().getFullName());// myJobGroup.myJob
//		System.out.println(context.getJobDetail().getJobDataMap().getString("myDescription"));// GOOD
		// 具体任务
	}

}
