package com.putin.quartz;

import com.putin.quartz.jobs.HelloJob;
import com.putin.quartz.jobs.TestJobData;
import org.quartz.*;
import org.quartz.impl.jdbcjobstore.StdRowLockSemaphore;

import java.text.ParseException;
import java.util.Properties;
import java.util.Random;

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
    public static void main(String[] args) throws SchedulerException, ParseException, InterruptedException {


        System.out.println(StdRowLockSemaphore.SELECT_FOR_LOCK);
        Scheduler scheduler = getScheduler();


//        testHelloJob(scheduler);
        for (int i = 0; i < 100; i++) {
            testJobData(scheduler,i);
            Thread.sleep(100);
        }
        //4. 启动
        scheduler.start();
    }

    private static void testJobData(Scheduler scheduler,int i) throws SchedulerException {
        JobDataMap newJobDataMap = new JobDataMap();

        newJobDataMap.putAsString("count",0);



        JobDetail jobData = JobBuilder.newJob(TestJobData.class)
                .withIdentity("testJobData"+i, "hello")
                .withDescription("TestJobData")
                .usingJobData("count",1)
                .build();
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger"+i, "hello")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(i+1*1000L)
                        )
                .build();

        scheduler.scheduleJob(jobData, trigger1);
    }

    private static void testHelloJob(Scheduler scheduler) throws SchedulerException, ParseException {
        //1. 定义一个 JobDetail
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJob", "hello")
                .withDescription("HelloJob")
                .build();


        //2. 定义一个 Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "hello")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
                .build();
        //3. 配置触发器和作业
        //   trigger 和 job 一一对应
        scheduler.scheduleJob(job, trigger);
    }

    private static Scheduler getScheduler() throws SchedulerException {
        Properties quartzProperties = QuartzProperties.getQuartzProperties("hello");
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(quartzProperties);
        return schedFact.getScheduler();
    }
}
