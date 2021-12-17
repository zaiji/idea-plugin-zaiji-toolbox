package com.zaiji.plugin.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息相关工具类
 *
 * @author zaiji
 */

public class ErrorInfoUtil {

    /**
     * 将异常的堆栈信息转换为字符串
     *
     * @param e 异常对象
     * @return 堆栈信息的string
     */
    public static String outPrintStack(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 将异常的堆栈信息转换为字符串,并上可视化错误信息
     *
     * @param customInfo 自定义信息
     * @param e          异常对象
     * @return 堆栈信息的string
     */
    public static String outPrintStack(String customInfo, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return customInfo + "\r\n" + sw;
    }
}
