package com.zaiji.annotation;

import java.lang.annotation.*;

/**
 * 组件信息
 *
 * @author zaiji
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PluginComponentInfo {

    /**
     * 组件名
     */
    String name();

    /**
     * 是否为默认组件
     */
    boolean defaultComponent() default false;
}
