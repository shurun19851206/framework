package com.rainy.mytest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class MyQuartz {
	
	public static void main(String[] args) throws SchedulerException {
//		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//		Scheduler scheduler = schedulerFactory.getScheduler();
//		JobDetail jobDetail = new JobDetail("jobDetail-s1", "jobDetailGroup-s1", SimpleQuartzJob.class);
//		SimpleTrigger simpleTrigger =new SimpleTrigger("simpleTrigger", "triggerGroup-s1");
//		long ctime = System.currentTimeMillis();
//		simpleTrigger.setStartTime(new Date(ctime));
//		simpleTrigger.setRepeatInterval(5);
//		simpleTrigger.setRepeatCount(100);
//		scheduler.scheduleJob(jobDetail, simpleTrigger);
//		scheduler.start();
//		QuartzTest.main(null);
//		for (int i=0; i<100; i++) {
//			Thread t = new MyThread();
//			t.start();
//		}
		
		
	}

}

class SimpleQuartzJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("==========");
	}
	
} 



class QuartzTest {
	public static void main(String[] args) {
		QuartzTest tqz = new QuartzTest();
		try {
			tqz.startShedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void startShedule() {
		try {
			/* 调度器 */
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			/* 具体执行类 */
			JobDetail jobDetail = new JobDetail("firstJOB", Scheduler.DEFAULT_GROUP, MQuartz.class);
			/* 触发器定义每三秒一次 */
			SimpleTrigger trigger = new SimpleTrigger("trigger1", scheduler.DEFAULT_GROUP, 10, 3000);
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}

class MQuartz implements Job {
	/**
	 * 事件类，处理具体的业务
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Hello quzrtz  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
	}
}

class MyThread extends Thread {
	@Override
	public void run() {
		System.out.println("==========");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}