package com.zaiji.util;


import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务工具类
 *
 * @author zaiji
 */

public final class QuartzUtil {

    /**
     * 任务
     */
    @FunctionalInterface
    public static interface ZJJob {
        void task();
    }

    /**
     * 周期性执行任务，从方法调用开始周期执行
     *
     * @param job        定时任务
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     */
    public Object executeByPeriod(ZJJob job, Integer period, TimeUnit periodUnit) {
        return executeByPeriod(job, period, periodUnit, 0, TimeUnit.SECONDS);
    }

    /**
     * 周期性执行任务，等待waitTime时间后开始按周期执行
     *
     * @param job        定时任务
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param waitTime   等待时长
     * @param waitUnit   等待时长时间单位
     */
    public Object executeByPeriod(ZJJob job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit) {
        return executeByPeriodWithEndTime(job, period, periodUnit, waitTime, waitUnit, null);
    }

    /**
     * 周期性执行任务，从方法调用开始周期执行，等到当前时间>endTime之后停止执行
     *
     * @param job        定时任务
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param endTime    结束时间
     */
    public Object executeByPeriodWithEndTime(ZJJob job, Integer period, TimeUnit periodUnit, Date endTime) {
        return executeByPeriodWithEndTime(job, period, periodUnit, 0, TimeUnit.SECONDS, endTime);
    }

    /**
     * TODO:周期性执行任务，等待waitTime时间后开始按周期执行，等到当前时间>endTime之后停止执行
     *
     * @param job        定时任务
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param waitTime   等待时长
     * @param waitUnit   等待时长时间单位
     * @param endTime    结束时间
     */
    public Object executeByPeriodWithEndTime(ZJJob job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit, Date endTime) {
        return null;
    }

    /**
     * 根据cron表达式执行任务
     *
     * @param job  定时任务
     * @param cron cron表达式
     */
    public Object executeByCron(ZJJob job, String cron) {
        return executeByCronWithEndTime(job, cron, null);
    }

    /**
     * TODO:根据cron表达式执行任务，等到当前时间>endTime之后停止执行
     *
     * @param job     定时任务
     * @param cron    cron表达式
     * @param endTime 结束时间
     */
    public Object executeByCronWithEndTime(ZJJob job, String cron, Date endTime) {
        return null;
    }

}
