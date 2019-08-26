package com.putin.quartz;


import java.util.Properties;

public class QuartzProperties {

    private static final String ORG_QUARTZ_DATASOURCE_MYDS_MAXCONNECTIONS = "2";

    private static final String ORG_QUARTZ_JOBSTORE_CLASS = "org.quartz.impl.jdbcjobstore.JobStoreTX";

    private static final String ORG_QUARTZ_JOBSTORE_CLUSTERCHECKININTERVAL = "20000";

    private static final String ORG_QUARTZ_JOBSTORE_DATASOURCE = "myDS";

    private static final String ORG_QUARTZ_JOBSTORE_DRIVERDELEGATECLASS = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";

    private static final String ORG_QUARTZ_JOBSTORE_ISCLUSTERED = "true";

    private static final String ORG_QUARTZ_JOBSTORE_MISFIRETHRESHOLD = "60000";

    private static final String ORG_QUARTZ_JOBSTORE_TABLEPREFIX = "QRTZ_";

    private static final String ORG_QUARTZ_JOBSTORE_USEPROPERTIES = "false";

    private static final String ORG_QUARTZ_SCHEDULER_INSTANCEID = "hello";

    private static final String ORG_QUARTZ_SCHEDULER_INSTANCENAME = "DefaultQuartzScheduler";

    private static final String ORG_QUARTZ_SCHEDULER_WRAPJOBEXECUTIONINUSERTRANSACTION = "false";

    private static final String ORG_QUARTZ_THREADPOOL_CLASS = "org.quartz.simpl.SimpleThreadPool";

    private static final String ORG_QUARTZ_THREADPOOL_THREADCOUNT = "50";

    private static final String ORG_QUARTZ_THREADPOOL_THREADPRIORITY = "5";

    private static final String ORG_QUARTZ_THREADPOOL_THREADSINHERITCONTEXTCLASSLOADEROFINITIALIZINGTHREAD = "true";

    private static final String ORG_QUARTZ_JOBSTORE_SELECTWITHLOCKSQL = "SELECT * FROM {0}LOCKS WHERE SCHED_NAME = {1} AND LOCK_NAME = ? FOR UPDATE";

    private static final String ORG_QUARTZ_JOBSTORE_MAXMISFIRESTOHANDLEATATIME ="20";

    private static final String ORG_QUARTZ_JOBSTORE_TXISOLATIONLEVELSERIALIZABLE = "true";

    public static Properties getQuartzProperties(String groupName) {

        Properties prop = new Properties();
        prop.setProperty("groupName", groupName);

        prop.setProperty("org.quartz.dataSource.myDS" + groupName + ".URL", "jdbc:mysql://47.104.128.114:3306/quartz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8");
        prop.setProperty("org.quartz.dataSource.myDS" + groupName + ".driver", "com.mysql.jdbc.Driver");
        prop.setProperty("org.quartz.dataSource.myDS" + groupName + ".user", "test");
        prop.setProperty("org.quartz.dataSource.myDS" + groupName + ".password", "1234");
        prop.setProperty("org.quartz.dataSource.myDS" + groupName + ".maxConnections", ORG_QUARTZ_DATASOURCE_MYDS_MAXCONNECTIONS);

        prop.setProperty("org.quartz.jobStore.dataSource", ORG_QUARTZ_JOBSTORE_DATASOURCE + groupName);

        prop.setProperty("org.quartz.scheduler.instanceName", groupName);
        prop.setProperty("org.quartz.scheduler.batchTriggerAcquisitionMaxCount", "100");
        prop.setProperty("org.quartz.jobStore.misfireThreshold", "5000");
        prop.setProperty("overwriteExistingJobs", "true");
        prop.setProperty("org.quartz.jobStore.class", ORG_QUARTZ_JOBSTORE_CLASS);
        prop.setProperty("org.quartz.jobStore.clusterCheckinInterval", ORG_QUARTZ_JOBSTORE_CLUSTERCHECKININTERVAL);
        prop.setProperty("org.quartz.jobStore.driverDelegateClass", ORG_QUARTZ_JOBSTORE_DRIVERDELEGATECLASS);
        prop.setProperty("org.quartz.jobStore.isClustered", ORG_QUARTZ_JOBSTORE_ISCLUSTERED);
        prop.setProperty("org.quartz.jobStore.misfireThreshold", ORG_QUARTZ_JOBSTORE_MISFIRETHRESHOLD);
        prop.setProperty("org.quartz.jobStore.tablePrefix", ORG_QUARTZ_JOBSTORE_TABLEPREFIX);
        prop.setProperty("org.quartz.jobStore.useProperties", ORG_QUARTZ_JOBSTORE_USEPROPERTIES);
        prop.setProperty("org.quartz.jobStore.selectWithLockSQL",ORG_QUARTZ_JOBSTORE_SELECTWITHLOCKSQL);
        prop.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime",ORG_QUARTZ_JOBSTORE_MAXMISFIRESTOHANDLEATATIME);
        prop.setProperty("org.quartz.jobStore.txIsolationLevelSerializable",ORG_QUARTZ_JOBSTORE_TXISOLATIONLEVELSERIALIZABLE);
        prop.setProperty("org.quartz.scheduler.instanceId", ORG_QUARTZ_SCHEDULER_INSTANCEID);
        prop.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", ORG_QUARTZ_SCHEDULER_WRAPJOBEXECUTIONINUSERTRANSACTION);
        prop.setProperty("org.quartz.threadPool.class", ORG_QUARTZ_THREADPOOL_CLASS);
        prop.setProperty("org.quartz.threadPool.threadCount", ORG_QUARTZ_THREADPOOL_THREADCOUNT);
        prop.setProperty("org.quartz.threadPool.threadPriority", ORG_QUARTZ_THREADPOOL_THREADPRIORITY);
        prop.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", ORG_QUARTZ_THREADPOOL_THREADSINHERITCONTEXTCLASSLOADEROFINITIALIZINGTHREAD);
        return prop;
    }

}
