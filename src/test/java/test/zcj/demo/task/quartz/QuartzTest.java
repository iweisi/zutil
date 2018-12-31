package test.zcj.demo.task.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {

	public static void main(String[] args) {
		try {
			
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

			// 创建一个JobDetail(负责定义任务)
			JobDetail jobDetail = new JobDetail("myJob", "myJobGroup", JobForMy.class);
			// jobDetail.getJobDataMap().put("myDescription", "GOOD");
			// jobDetail.addJobListener("MyJobListener");

			// 创建一个Trigger(负责设置调度策略)立即执行且仅执行一次
			SimpleTrigger trigger = new SimpleTrigger("myTrigger", "myTriggerGroup", new Date(), null, 0, 0L);

			// 1、全局JobListener
			// sched.addGlobalJobListener(new MyJobListener("MyJobListener"));
			
			// 2、非全局JobListener(必须定义在sched.scheduleJob()之前)
			// JobListener jobListener = new MyJobListener("MyJobListener");
			// sched.addJobListener(jobListener);
			// jobDetail.addJobListener(jobListener.getName());
			
			// 3、全局TriggerListener
			// sched.addGlobalTriggerListener(new MyTriggerListener("MyTriggerListener"));
			
			// 4、非全局TriggerListener(必须定义在sched.scheduleJob()之前)
			// TriggerListener triggerListener = new MyTriggerListener("MyTriggerListener");
			// sched.addTriggerListener(triggerListener);
			// trigger.addTriggerListener(triggerListener.getName());
			
			// 5、全局SchedulerListener
			// sched.addSchedulerListener(new MySchedulerListener());

			// 用scheduler将JobDetail与Trigger关联在一起，开始调度任务
			sched.scheduleJob(jobDetail, trigger);
			
			sched.start();

//			sched.standby();// 暂时停止查找 Job 去执行。
//			sched.start();// 重新启动 Job 去执行。
			
//			sched.shutdown();// 马上停止 Job。
//			sched.shutdown(true);// 当前 Job 完成后停止。
			
//			sched.deleteJob("myJob", "myJobGroup");// 移除 Job。
			
//			sched.addJob(jobDetail, true);// 用将要传入的 JobDetail 替换已部署的 JobDetail

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Calendar initCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.set(Calendar.YEAR, year); 
		calendar.set(Calendar.MONTH, month); 
		calendar.set(Calendar.DAY_OF_MONTH, day); 
		calendar.set(Calendar.HOUR, hour); 
		calendar.set(Calendar.MINUTE, minute); 
		calendar.set(Calendar.SECOND, second); 
		calendar.set(Calendar.MILLISECOND, millisecond); 
		return calendar;
	}
	
	public static Trigger getOtherTriger() {
		// 半分钟后开始执行，且每隔一分钟重复执行一次
		SimpleTrigger trigger = new SimpleTrigger("myTrigger", "myTriggerGroup", new Date(System.currentTimeMillis()+30*1000), null, 0, 60*1000);
		
		// 2011-06-01 14:30:00开始，一小时执行一次，一共执行一百次，一天之后截止（实际执行24次）
		Calendar calendar = initCalendar(2011, Calendar.JUNE, 1, 14, 30, 0, 0);
		Date startTime = calendar.getTime(); 
		Date endTime = new Date (calendar.getTimeInMillis() +24*60*60*1000); 
		trigger = new SimpleTrigger("myTrigger", "myTriggerGroup", startTime, endTime, 100, 60*60*1000); 
		
		// 基于日历的调度安排
		CronTrigger trigger3 = new CronTrigger("myTrigger", "myGroup");
		try {
			/** 
			 *  '/'表示开始时刻与间隔时段
			 *  '?'仅适用于 Day-of-Month 和 Day-of-Week,其中之一必填?
			 *  'L'仅适用于 Day-of-Month 和 Day-of-Week,Day-of-Month=L该月最后一天,Day-of-Week=L表示周六,Day-of-Week=5L表示该月最后一个星期四。
				Seconds Minutes Hours 	Day-of-Month Month Day-of-Week Year
				0		0		0/3	  	*			   *	 ?					// 每三小时执行 每小时的整点开始执行
				0		3/10	*	  	*			   *	 ?					// 每十分钟执行 每小时的第三分钟开始执行
				0		0/30	20-23 	?			   *	 MON-WED,SAT		// 每周一，周二，周三，周六 20:00 到 23:00，每半小时执行一次
				0		30		11-14/1 ?			   *	 5L					// 每月最后一个周四，中午 11:30-14:30，每小时执行一次
			*/
			trigger3.setCronExpression("0 0/30 20-23 ? * MON-WED,SAT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return trigger;
	}
}