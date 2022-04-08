package com.zaiji.util;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务工具类
 *
 * @author zaiji
 */

public final class ScheduleUtil {

    private ScheduleUtil() {
    }

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    /**
     * 周期性执行任务，从方法调用开始周期执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @return 定时任务对象
     */
    public static ScheduledFuture<?> executeByPeriod(Runnable job, Integer period, TimeUnit periodUnit) {
        return executeByPeriodWithEndTime(job, period, periodUnit, null);
    }

    /**
     * 周期性执行任务，等待waitTime时间后开始按周期执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param waitTime   等待时长
     * @param waitUnit   等待时长时间单位
     * @return 定时任务对象
     */
    public static ScheduledFuture<?> executeByPeriod(Runnable job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit) {
        return executeByPeriodWithEndTime(job, period, periodUnit, waitTime, waitUnit, null);
    }

    /**
     * 周期性执行任务，从方法调用开始周期执行，等到当前时间>endTime之后停止执行
     *
     * @param job        定时任务类
     * @param period     定时周期
     * @param periodUnit 定时周期时间单位
     * @param endTime    结束时间
     * @return 定时任务对象
     */
    public static ScheduledFuture<?> executeByPeriodWithEndTime(Runnable job, Integer period, TimeUnit periodUnit, Date endTime) {

        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(job, 0, period, periodUnit);

        if (Objects.nonNull(endTime)) {
            //定义一个关闭定时任务的定时任务
            scheduledExecutorService.schedule(() -> scheduledFuture.cancel(true), (endTime.getTime() - new Date().getTime()), TimeUnit.MILLISECONDS);
        }
        return scheduledFuture;
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
     * @return 定时任务对象
     */
    public static ScheduledFuture<?> executeByPeriodWithEndTime(Runnable job, Integer period, TimeUnit periodUnit, Integer waitTime, TimeUnit waitUnit, Date endTime) {

        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(job, changTimeToMilliseconds(waitTime, waitUnit, periodUnit), period, periodUnit);

        if (Objects.nonNull(endTime)) {
            //定义一个关闭定时任务的定时任务
            scheduledExecutorService.schedule(() -> scheduledFuture.cancel(true), (endTime.getTime() - new Date().getTime()), TimeUnit.MILLISECONDS);
        }

        return scheduledFuture;
    }

    /**
     * 将一个单位的时间转换成另一个单位的时间
     *
     * @param time           时间
     * @param timeUnit       时间单位
     * @param targetTimeUnit 目标时间单位
     * @return 另一单位下的时间
     */
    private static long changTimeToMilliseconds(long time, TimeUnit timeUnit, TimeUnit targetTimeUnit) {
        return targetTimeUnit.convert(time, timeUnit);
    }
}
