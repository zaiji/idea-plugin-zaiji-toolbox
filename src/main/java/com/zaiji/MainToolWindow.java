package com.zaiji;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zaiji.init.InitRegisterComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * 工具箱主页面
 *
 * @author zaiji
 * 2021-12-07
 */

public class MainToolWindow implements ToolWindowFactory {

    /**
     * 主工具箱布局
     */
    private static class MainToolWindowLayOut {

        private final JPanel mainJpanel;
        private final Box leftMenuJpanel;
        private final JPanel rightContentJpanel;

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
            InitRegisterComponent.registerAllComponent("com.zaiji", leftMenuJpanel, rightContentJpanel);
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
