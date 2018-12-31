package test.zcj.demo.task.quartz;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;

public class MySchedulerListener implements SchedulerListener {

	@Override
	public void jobScheduled(Trigger trigger) {
		// 有新的 JobDetail 部署时调用
	}

	@Override
	public void jobUnscheduled(String triggerName, String triggerGroup) {
		// 有新的 JobDetail 卸载时调用
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		// trigger 到了再也不会触发的时候调用
	}

	@Override
	public void triggersPaused(String triggerName, String triggerGroup) {
		// triggerName 或 triggerGroup 被暂停时触发（设置另一个参数为 null）
	}

	@Override
	public void triggersResumed(String triggerName, String triggerGroup) {
		// triggerName 或 triggerGroup 从暂停中恢复
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {

	}

	@Override
	public void jobDeleted(String jobName, String groupName) {

	}

	@Override
	public void jobsPaused(String jobName, String jobGroup) {

	}

	@Override
	public void jobsResumed(String jobName, String jobGroup) {

	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		// 在 Scheduler 的正常运行期间产生一个严重错误时调用这个方法。
	}

	@Override
	public void schedulerInStandbyMode() {

	}

	@Override
	public void schedulerStarted() {

	}

	@Override
	public void schedulerShutdown() {
		// 关闭之前触发
	}

	@Override
	public void schedulerShuttingdown() {

	}

}
