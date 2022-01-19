package com.zaiji.plugin.log4j2.entity;

/**
 * 配置对象
 *
 * @author zaiji
 */
public class ConfigEntity {

    public ConfigEntity() {
    }

    /**
     * 文件格式
     */
    private LogFileType fileType;

    /**
     * 是否需要控制台输出
     */
    private boolean needConsolePrint;

    /**
     * 日志等级
     */
    private LogLevel logLevel;

    /**
     * 日志文件名
     */
    private String logFileName;

    /**
     * 日志滚动大小
     */
    private String fileRollingSize;

    /**
     * 日志滚动大小单位
     */
    private String fileRollingSizeUnit;

    /**
     * 日志文件最大保留数量
     */
    private String fileNum;

    /**
     *log4j2监测配置文件修改时间间隔，单位为秒
     * */
    private String detectionChangeInterval;

    /**
     * log4j2本身日志级别
     */
    private LogLevel log4j2LogLevel;

    public LogFileType getFileType() {
        return fileType;
    }

    public ConfigEntity setFileType(LogFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public boolean isNeedConsolePrint() {
        return needConsolePrint;
    }

    public ConfigEntity setNeedConsolePrint(boolean needConsolePrint) {
        this.needConsolePrint = needConsolePrint;
        return this;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public ConfigEntity setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public ConfigEntity setLogFileName(String logFileName) {
        this.logFileName = logFileName;
        return this;
    }

    public String getFileRollingSize() {
        return fileRollingSize;
    }

    public ConfigEntity setFileRollingSize(String fileRollingSize) {
        this.fileRollingSize = fileRollingSize;
        return this;
    }

    public String getFileRollingSizeUnit() {
        return fileRollingSizeUnit;
    }

    public ConfigEntity setFileRollingSizeUnit(String fileRollingSizeUnit) {
        this.fileRollingSizeUnit = fileRollingSizeUnit;
        return this;
    }

    public String getFileNum() {
        return fileNum;
    }

    public ConfigEntity setFileNum(String fileNum) {
        this.fileNum = fileNum;
        return this;
    }

    public LogLevel getLog4j2LogLevel() {
        return log4j2LogLevel;
    }

    public ConfigEntity setLog4j2LogLevel(LogLevel log4j2LogLevel) {
        this.log4j2LogLevel = log4j2LogLevel;
        return this;
    }

    public String getDetectionChangeInterval() {
        return detectionChangeInterval;
    }

    public ConfigEntity setDetectionChangeInterval(String detectionChangeInterval) {
        this.detectionChangeInterval = detectionChangeInterval;
        return this;
    }
}
