package com.zaiji.annotation;

import java.lang.annotation.*;

/**
 * 标记注解，用于标记是否为默认组件
 *
 * @author zaiji
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultComponent {

}
