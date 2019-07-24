package com.putin.quartz;

import com.putin.quartz.jobs.HelloJob;
import org.quartz.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class TestTrigger {



    public static void main(String[] args) throws SchedulerException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJob", "group1")
                .withDescription("HelloJob")
                .build();

        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0/3 * * * * ?");
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(schedBuilder)
                .forJob("helloJob", "group1")
                .build();

        scheduler.scheduleJob(job,trigger);



        scheduler.start();

        CronExpression cronExpression = new CronExpression("0/3 * * * * ?");
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
       /* Date nextFireTime = trigger.getNextFireTime();
        Date previousFireTime = trigger.getPreviousFireTime();
        System.out.println(sdf.format(nextFireTime));
        System.out.println(sdf.format(previousFireTime));*/
    }
}
