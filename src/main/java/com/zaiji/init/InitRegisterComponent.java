package com.zaiji.init;

import com.zaiji.entity.ComponentInfo;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 注册所有组件类
 *
 * @author zaiji
 */

public class InitRegisterComponent {

    /**
     * 组件列表
     */
    private static final LinkedList<JPanel> PANEL_LIST = new LinkedList<>();

    public static void registerAllComponent(String basePackage, Box funBtnPanel, JPanel contentPanel) {
        final List<ComponentInfo> componentInfos = InitPackageScan.scanAllComponentInfo(basePackage);
        componentInfos.forEach(componentInfo -> {
            try {
                final JButton jButton = new JButton(componentInfo.getComponentName());
                final Class<?> componentClass = Class.forName(componentInfo.getClassName());

                final Method getContent = componentClass.getMethod("getContent");

                //组件对象
                final Object component = componentClass.getDeclaredConstructor().newInstance();

                final JPanel content = (JPanel) getContent.invoke(component);

                //判断是否默认页
                if (componentInfo.isDefaultComponent()) {
                    content.setVisible(true);
                    contentPanel.add(content, BorderLayout.CENTER);
                    contentPanel.updateUI();
                } else {
                    content.setVisible(false);
                }

                final Box horizontalBox = Box.createHorizontalBox();
                horizontalBox.add(jButton);
                PANEL_LIST.add(content);
                jButton.addActionListener(aaa -> {
                    PANEL_LIST.forEach(e -> e.setVisible(false));
                    contentPanel.add(content, BorderLayout.CENTER);
                    content.setVisible(true);
                    contentPanel.updateUI();
                });

                funBtnPanel.add(horizontalBox);
            } catch (Exception e) {
                System.out.println("组件：【" + componentInfo.getComponentName() + "】初始化失败！" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
