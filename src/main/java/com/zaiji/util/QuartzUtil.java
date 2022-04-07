package com.zaiji.util;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务工具类
 *
 * @author zaiji
 */

public final class QuartzUtil {

    private static Scheduler SCHEDULER;

    private QuartzUtil() {
    }

    static {
        try {
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Properties prop = new Properties();
            //***************开始填写配置**********************
            //调度器实例名称
            prop.put("org.quartz.scheduler.instanceName", "zaiji-scheduler");
            prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
            prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
            prop.put("org.quartz.threadPool.threadCount", "10");
            prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
            prop.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
            //***************结束填写配置**********************
            schedulerFactory.initialize(prop);
            SCHEDULER = schedulerFactory.getScheduler();
            SCHEDULER.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 周期性执行任务，从方法调用开始周期执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     */
    public static void executeByPeriod(Class<? extends Job> job, Integer period, TimeUnit periodUnit) {
        executeByPeriodWithEndTime(job, period, periodUnit, null);
    }

    /**
     * 周期性执行任务，等待waitTime时间后开始按周期执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param waitTime   等待时长
     * @param waitUnit   等待时长时间单位
     */
    public static void executeByPeriod(Class<? extends Job> job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit) {
        executeByPeriodWithEndTime(job, period, periodUnit, waitTime, waitUnit, null);
    }

    /**
     * 周期性执行任务，从方法调用开始周期执行，等到当前时间>endTime之后停止执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param endTime    结束时间
     */
    public static void executeByPeriodWithEndTime(Class<? extends Job> job, Integer period, TimeUnit periodUnit, Date endTime) {

        //定义一个触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + UUID.randomUUID(), "period_trigger_group")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(changTimeToMilliseconds(period, periodUnit))
                        .repeatForever())
                .endAt(endTime)
                .build();

        //定义一个任务
        JobDetail tjob = JobBuilder.newJob(job)
                .withIdentity("job" + UUID.randomUUID(), "period_job_group")
                .build();


        try {
            SCHEDULER.scheduleJob(tjob, trigger);
            SCHEDULER.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 周期性执行任务，等待waitTime时间后开始按周期执行，等到当前时间>endTime之后停止执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param waitTime   等待时长
     * @param waitUnit   等待时长时间单位
     * @param endTime    结束时间
     */
    public static void executeByPeriodWithEndTime(Class<? extends Job> job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit, Date endTime) {
        //定义一个触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + UUID.randomUUID(), "period_trigger_group")
                .startAt(new Date(new Date().getTime() + changTimeToMilliseconds(waitTime, waitUnit)))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(changTimeToMilliseconds(period, periodUnit))
                        .repeatForever())
                .endAt(endTime)
                .build();

        //定义一个任务
        JobDetail tjob = JobBuilder.newJob(job)
                .withIdentity("job" + UUID.randomUUID(), "period_job_group")
                .build();

        try {
            SCHEDULER.scheduleJob(tjob, trigger);
            SCHEDULER.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据cron表达式执行任务
     *
     * @param job  定时任务类
     * @param cron cron表达式
     */
    public static void executeByCron(Class<? extends Job> job, String cron) {
        executeByCronWithEndTime(job, cron, null);
    }

    /**
     * 根据cron表达式执行任务，等到当前时间>endTime之后停止执行
     *
     * @param job     定时任务
     * @param cron    cron表达式
     * @param endTime 结束时间
     */
    public static void executeByCronWithEndTime(Class<? extends Job> job, String cron, Date endTime) {
        //定义一个触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + UUID.randomUUID(), "cron_trigger_group")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .endAt(endTime)
                .build();

        //定义一个任务
        JobDetail tjob = JobBuilder.newJob(job)
                .withIdentity("job" + UUID.randomUUID(), "cron_job_group")
                .build();


        try {
            SCHEDULER.scheduleJob(tjob, trigger);
            SCHEDULER.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将时间转换为秒为单位的数字
     *
     * @param time     时间
     * @param timeUnit 时间单位
     * @return 秒数
     */
    private static int changTimeToMilliseconds(Integer time, TimeUnit timeUnit) {
        time = Objects.nonNull(time) ? time : 0;
        switch (timeUnit) {
            case DAYS: {
                time *= 24 * 60 * 60 * 1000;
                break;
            }
            case HOURS: {
                time *= 60 * 60 * 1000;
                break;
            }
            case MINUTES: {
                time *= 60 * 1000;
                break;
            }
            case SECONDS: {
                time *= 1000;
                break;
            }
            case MILLISECONDS: {
                time *= 1;
                break;
            }
            case MICROSECONDS: {
                time /= 1000;
                break;
            }
            case NANOSECONDS: {
                time /= 1000 * 1000;
                break;
            }
            default:
        }
        return time;
    }
}
