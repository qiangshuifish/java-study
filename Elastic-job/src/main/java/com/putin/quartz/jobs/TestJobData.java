package com.putin.quartz.jobs;

import org.quartz.*;

import java.text.MessageFormat;

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
public class TestJobData implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        String format = MessageFormat.format("定时任务 {0},定时任务描述 {1},定时任务参数：{2} ",
                jobDetail.getKey(), jobDetail.getDescription(),dataMap.toString());
        System.out.println(format);
    }
}
