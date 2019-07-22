package com.putin.quartz;

import com.putin.quartz.jobs.HelloJob;
import com.putin.quartz.jobs.TestJobData;
import org.quartz.*;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author wenpengyuan
 * @version 1.0
 * @since 1.0
 */
public class SimpleDemo {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler scheduler = schedFact.getScheduler();
        //1. 定义一个 JobDetail
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJob", "group1")
                .withDescription("HelloJob")
                .build();
        JobDetail jobData = JobBuilder.newJob(TestJobData.class)
                .withIdentity("testJobData", "group1")
                .withDescription("TestJobData")
                .build();

        //2. 定义一个 Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(1000L)
                        .repeatForever())
                .build();

        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(1000L)
                        .repeatForever())
                .build();

        //3. 配置触发器和作业
        //   trigger he job
        scheduler.scheduleJob(job, trigger);
        scheduler.scheduleJob(jobData, trigger1);

        //4. 启动
        scheduler.start();
    }
}
