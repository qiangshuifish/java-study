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
    private int count;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = context.getMergedJobDataMap();



        String format = MessageFormat.format(" ======== 当前线程 {3} 定时任务 {0},定时任务描述 {1},定时任务参数：{2},当前对象哈希值 {4}  ======== ",
                jobDetail.getKey(), jobDetail.getDescription(),count,Thread.currentThread().getName(),this.hashCode());
        System.out.println(format);
        count++;
        dataMap.putAsString("count",count);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
