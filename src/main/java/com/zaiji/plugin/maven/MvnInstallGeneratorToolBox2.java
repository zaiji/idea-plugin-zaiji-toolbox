package com.zaiji.plugin.maven;

import com.zaiji.plugin.BaseComponentClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MvnInstallGeneratorToolBox2 extends BaseComponentClass {
    private JRadioButton pom方式RadioButton;
    private JRadioButton info信息RadioButton;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton 生成Button;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton 生成Button1;
    private JTextArea textArea3;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    @Override
    protected JPanel getContent() {
        return panel1;
    }

}
