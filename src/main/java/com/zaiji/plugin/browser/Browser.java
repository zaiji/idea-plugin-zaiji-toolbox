package com.zaiji.plugin.browser;

import com.zaiji.annotation.PluginComponentInfo;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * 浏览器
 *
 * @author zaiji
 */

//@PluginComponentInfo(name = "浏览器")
public class Browser {
    private JPanel mainPane;

    {
        NativeInterface.open();
        new Thread() {
            @Override
            public void run() {

                SwingUtilities.invokeLater(() -> {
                    mainPane.setLayout(new BorderLayout());


                    JPanel webBrowserPanel = new JPanel(new BorderLayout());
                    webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
                    final JWebBrowser webBrowser = new JWebBrowser();
                    webBrowser.navigate("http://www.4399.com");
                    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
                    mainPane.add(webBrowserPanel, BorderLayout.CENTER);


                    mainPane.updateUI();
                });
                NativeInterface.runEventPump();
            }
        }.start();

    }

    public JPanel getContent() {
        return mainPane;
    }
}