package com.zaiji.plugin.browser;

//import com.teamdev.jxbrowser.engine.Engine;
import com.zaiji.annotation.PluginComponentInfo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 浏览器
 *
 * @author zaiji
 */

@PluginComponentInfo(name = "浏览器")
public class Browser {
    private JPanel mainPane;

    {
//        UIUtils.setPreferredLookAndFeel();
//        NativeInterface.open();
//        mainPane.setLayout(new BorderLayout());
//
//
//        JPanel webBrowserPanel = new JPanel(new BorderLayout());
//        final JWebBrowser webBrowser = new JWebBrowser();
//        webBrowser.navigate("http://www.baidu.com");
//        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
//        mainPane.add(webBrowserPanel, BorderLayout.CENTER);
//
//
//        mainPane.updateUI();
//        new Thread(() -> {
//            if (!NativeInterface.isEventPumpRunning()) {
//                NativeInterface.runEventPump();
//            }
//        }).start();
    }

    public JPanel getContent() {
        return mainPane;
    }
}

class Testdsf{
    public static void main(String[] args) {
        // Initialize Chromium.
//        Engine engine = Engine.newInstance(HARDWARE_ACCELERATED);
//
//// Create a Browser instance.
//        com.teamdev.jxbrowser.browser.Browser browser = engine.newBrowser();
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("JxBrowser AWT/Swing");
//            frame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosing(WindowEvent e) {
//                    // Shutdown Chromium and release allocated resources.
//                    engine.close();
//                }
//            });
//            // Create and embed Swing BrowserView component to display web content.
//            frame.add(BrowserView.newInstance(browser));
//            frame.setSize(1280, 800);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//
//            // Load the required web page.
//            browser.navigation().loadUrl("https://html5test.com/");
//        });
    }
}