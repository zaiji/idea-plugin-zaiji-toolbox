package com.zaiji.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zaiji.plugin.image.ImageToBase64;
import com.zaiji.plugin.maven.MvnInstallGeneratorToolBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
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
        put("图片Base64转换", ImageToBase64.class);
        put("mvn install", MvnInstallGeneratorToolBox.class);
    }};

    /**
     * 主工具箱布局
     */
    private static class MainToolWindowLayOut {

        private final JPanel mainJpanel;
        private final Box leftMenuJpanel;
        private final JPanel rightContentJpanel;
        private final LinkedList<JPanel> panelList = new LinkedList<>();

        public MainToolWindowLayOut() {

            mainJpanel = new JPanel();
            mainJpanel.setLayout(new BorderLayout());
            leftMenuJpanel = Box.createVerticalBox();
            final JBScrollPane jbScrollPane1 = new JBScrollPane(leftMenuJpanel);
            rightContentJpanel = new JPanel();
            rightContentJpanel.setLayout(new BorderLayout());
            final JBScrollPane jbScrollPane2 = new JBScrollPane(rightContentJpanel);
            mainJpanel.add(jbScrollPane1, BorderLayout.WEST);
            mainJpanel.add(jbScrollPane2, BorderLayout.CENTER);
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
                    jButton.addActionListener(aaa -> {
                        panelList.forEach(e -> e.setVisible(false));
                        rightContentJpanel.add(content, BorderLayout.CENTER);
                        content.setVisible(true);
                        rightContentJpanel.updateUI();
                    });

                    leftMenuJpanel.add(horizontalBox);
                } catch (Exception e) {
                    System.out.println("组件：【" + key + "】初始化失败！" + e.getMessage());
                    e.printStackTrace();
                }
            });
            leftMenuJpanel.updateUI();
            rightContentJpanel.updateUI();
        }

        public JPanel getContent() {
            return mainJpanel;
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
