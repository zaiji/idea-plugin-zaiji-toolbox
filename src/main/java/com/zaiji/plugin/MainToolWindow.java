package com.zaiji.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zaiji.plugin.maven.MvnInstallGeneratorToolBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 工具箱主页面
 *
 * @author zaiji
 * 2021-12-07
 */

public class MainToolWindow implements ToolWindowFactory {

    private final static Map<String, Class<? extends BaseComponentClass>> COMPONENTS = new HashMap<>(20) {{
        put("mvn install", MvnInstallGeneratorToolBox.class);
    }};

    /**
     * 主工具箱布局
     */
    private class MainToolWindowLayOut {

        private JPanel mainJPanel;
        private Box leftMenuJPanel;
        private JPanel rightContentJPanel;
        private LinkedList<JPanel> panelList = new LinkedList();

        public MainToolWindowLayOut() {

            mainJPanel = new JPanel();
            mainJPanel.setLayout(new BorderLayout());
            leftMenuJPanel = Box.createVerticalBox();
            rightContentJPanel = new JPanel();
            mainJPanel.add(leftMenuJPanel, BorderLayout.WEST);
            mainJPanel.add(rightContentJPanel, BorderLayout.CENTER);
            init();
        }

        private void init() {
            COMPONENTS.forEach((key, value) -> {
                try {
                    final JButton jButton = new JButton(key);
                    final BaseComponentClass baseComponentClass = value.getDeclaredConstructor().newInstance();
                    final JPanel content = baseComponentClass.getContent();
                    content.setVisible(false);
                    final Box horizontalBox = Box.createHorizontalBox();
                    horizontalBox.add(jButton);
                    panelList.add(content);
                    jButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent aaa) {
                            panelList.forEach(e -> e.setVisible(false));
                            content.setVisible(true);
                        }
                    });

                    leftMenuJPanel.add(horizontalBox);
                    rightContentJPanel.add(content);
                } catch (Exception e) {
                    System.out.println("组件：【" + key + "】初始化失败！" + e.getMessage());
                }
            });
            leftMenuJPanel.updateUI();
            rightContentJPanel.updateUI();
        }

        public JPanel getContent() {
            return mainJPanel;
        }
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MainToolWindowLayOut mainToolWindowLayOut = new MainToolWindowLayOut();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mainToolWindowLayOut.getContent(), "仔鸡的工具箱", true);
        toolWindow.getContentManager().addContent(content);
    }
}
