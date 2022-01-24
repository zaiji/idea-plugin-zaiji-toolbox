package com.zaiji.plugin.browser;

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
//        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        new Thread() {
            @Override
            public void run() {

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
                mainPane.setLayout(new BorderLayout());


                JPanel webBrowserPanel = new JPanel(new BorderLayout());
                webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
                final JWebBrowser webBrowser = new JWebBrowser();
                webBrowser.navigate("http://www.google.com");
                webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
                mainPane.add(webBrowserPanel, BorderLayout.CENTER);


//                mainPane.add(new SimpleWebBrowserExample(), BorderLayout.CENTER);
                mainPane.updateUI();
//            }
//        });
                NativeInterface.runEventPump();
            }
        }.start();

    }

    public JPanel getContent() {
        return mainPane;
    }
}


class SimpleWebBrowserExample extends JPanel {

    public SimpleWebBrowserExample() {
//        super(new BorderLayout());
//        JPanel webBrowserPanel = new JPanel(new BorderLayout());
//        webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
//        final JWebBrowser webBrowser = new JWebBrowser();
//        webBrowser.navigate("http://www.google.com");
//        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
//        add(webBrowserPanel, BorderLayout.CENTER);
        // Create an additional bar allowing to show/hide the menu bar of the web browser.
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
//        JCheckBox menuBarCheckBox = new JCheckBox("Menu Bar", webBrowser.isMenuBarVisible());
//        menuBarCheckBox.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                webBrowser.setMenuBarVisible(e.getStateChange() == ItemEvent.SELECTED);
//            }
//        });
//        buttonPanel.add(menuBarCheckBox);
//        add(buttonPanel, BorderLayout.SOUTH);
    }


}