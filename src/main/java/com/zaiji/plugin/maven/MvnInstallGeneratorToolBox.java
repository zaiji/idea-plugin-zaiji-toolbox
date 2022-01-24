package com.zaiji.plugin.maven;

import com.zaiji.annotation.PluginComponentInfo;
import com.zaiji.util.ErrorInfoUtil;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * maven安装
 *
 * @author zaiji
 */

@PluginComponentInfo(name = "mvn install")
public class MvnInstallGeneratorToolBox {
    private JPanel panel1;
    private JButton buttonGeneratorPom;
    private JTextArea infoPomTextArea;
    private JTextArea pomResultTextArea;
    private JButton buttonGeneratorInfo;
    private JTextArea infoResultTextArea;
    private JTextField infoGroudIdTextField;
    private JTextField infoArtifactIdTextField;
    private JTextField infoVersionTextField;
    private JTabbedPane tabbedPane1;

    public MvnInstallGeneratorToolBox() {
        buttonGeneratorPom.addActionListener(e -> pomResultTextArea.setText(generatorCodeByPom()));
        buttonGeneratorInfo.addActionListener(e -> infoResultTextArea.setText(generatorCodeByInfo()));
    }

    private String generatorCodeByPom() {
        try {
            //解析pom文件
            String pomString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dependencies>" + infoPomTextArea.getText() + "</dependencies>";
            Element rootElement = DocumentHelper.parseText(pomString).getRootElement();
            List<String> result = new LinkedList<>();
            final Iterator dependencyIterator = rootElement.elementIterator("dependency");
            while (dependencyIterator.hasNext()) {
                final DefaultElement next = (DefaultElement) dependencyIterator.next();
                final String groupId = Optional.ofNullable(next.element("groupId")).map(Element::getText).orElse("");
                final String artifactId = Optional.ofNullable(next.element("artifactId")).map(Element::getText).orElse("");
                final String version = Optional.ofNullable(next.element("version")).map(Element::getText).orElse("");
                result.add(generatorCode(groupId, artifactId, version));
            }

            return String.join("\r\n", result);
        } catch (Exception e) {
            return ErrorInfoUtil.outPrintStack("解析xml失败，请确认数据格式！", e);
        }
    }

    private String generatorCodeByInfo() {
        String groupId = infoGroudIdTextField.getText();
        String artifactid = infoArtifactIdTextField.getText();
        String version = infoVersionTextField.getText();
        return generatorCode(groupId, artifactid, version);
    }

    private final String generatorCode(String groupId, String artifactid, String version) {
        return "mvn install:install-file -DgroupId=" + groupId + " -DartifactId=" + artifactid + " -Dversion=" + version + " -Dpackaging=jar -Dfile=" + artifactid + "-" + version + ".jar";
    }

    /**
     * 从dom4j的节点内获取某个子节点
     *
     * @param element     节点，可以传入null
     * @param elementName 子节点名称
     * @return 如果子节点不存在则返回null
     */
    private Element getElement(Element element, String elementName) {
        if (null == element) {
            return null;
        }
        return element.element(elementName);
    }

    public JPanel getContent() {
        return panel1;
    }

}
