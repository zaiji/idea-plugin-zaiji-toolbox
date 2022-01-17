package com.zaiji.plugin.log4j2.entity;

/**
 * 日志级别类型
 *
 * @author zaiji
 */
public enum LogLevel {
    /**
     * info级别
     */
    info,

    /**
     * warn级别
     */
    warn,

    /**
     * error级别
     */
    error;

    public static LogLevel getValueOf(String name){
        try {
            return valueOf(name);
        }catch (Exception e){
            return null;
        }
    }
}
