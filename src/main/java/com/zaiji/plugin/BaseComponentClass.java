package com.zaiji.plugin;

import javax.swing.*;

/**
 * 组件基类
 *
 * @author zaiji
 * 2021-12-07
 */
public abstract class BaseComponentClass {

    /**
     * 获得此组件的最终面板
     *
     * @return 最终面板
     */
    protected abstract JPanel getContent();

}
