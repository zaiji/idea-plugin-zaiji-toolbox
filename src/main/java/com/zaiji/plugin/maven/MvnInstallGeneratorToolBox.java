package com.zaiji.plugin.maven;

import com.zaiji.plugin.BaseComponentClass;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mvn install命令生成
 *
 * @author zaiji
 */

public class MvnInstallGeneratorToolBox extends BaseComponentClass {

    private JPanel mvnInstallGeneratorToolJPanel;

    private static ConcurrentHashMap<String, JComponent> keyToComponents = new ConcurrentHashMap<>();

    public static enum AllComponents {
        PANEL_TOP_BAR("PANEL_TOP_BAR", new JPanel()),
        LABEL_TYPE("LABEL_TYPE", new JLabel("选择生成类型")),
        RADIO_TYPE_POM("RADIO_TYPE_POM", new JRadioButton("根据pom文件生成")),
        RADIO_TYPE_INFO("RADIO_TYPE_INFO", new JRadioButton("根据信息生成")),
        BUTTON_GENERATOR("BUTTON_GENERATOR", new JButton("生成")),
        PANEL_CONTENT("PANEL_CONTENT", new JPanel()),
        PANEL_CONTENT_POM("PANEL_CONTENT_POM", new JPanel()),
        TEXTAREA_POM("TEXTAREA_POM", new JTextArea(10, 70)),
        PANEL_CONTENT_INFO("PANEL_CONTENT_INFO", Box.createVerticalBox()),
        LABEL_GROUPID("LABEL_GROUPID", new JLabel("groupId")),
        INPUT_GROUPID("INPUT_GROUPID", new JTextField(20)),
        LABEL_ARTIFACTID("LABEL_ARTIFACTID", new JLabel("artifactId")),
        INPUT_ARTIFACTID("INPUT_ARTIFACTID", new JTextField(20)),
        LABEL_VERSION("LABEL_VERSION", new JLabel("version")),
        INPUT_VERSION("INPUT_VERSION", new JTextField(20)),
        PANEL_RESULT("PANEL_RESULT", new JPanel()),
        TEXTAREA_RESULT("TEXTAREA_RESULT", new JTextArea(10, 100));

        private String key;
        private JComponent component;

        AllComponents(String key, JComponent component) {
            this.key = key;
            this.component = component;
            keyToComponents.put(key, component);
        }

        public JComponent getComponent() {
            return component;
        }

        public <T> T getComponent(Class<T> clazz) {
            return (T) component;
        }
    }

    public MvnInstallGeneratorToolBox() {
        mvnInstallGeneratorToolJPanel = new JPanel();
        mvnInstallGeneratorToolJPanel.setLayout(new BorderLayout());
        mvnInstallGeneratorToolJPanel.add(AllComponents.PANEL_TOP_BAR.component, BorderLayout.NORTH);
        mvnInstallGeneratorToolJPanel.add(AllComponents.PANEL_CONTENT.component, BorderLayout.WEST);
        mvnInstallGeneratorToolJPanel.add(AllComponents.PANEL_RESULT.component, BorderLayout.CENTER);
        init();
    }

    private void init() {
        setLayout();
        setListener();
    }

    private final ButtonGroup buttonGroup = new ButtonGroup();

    private void setLayout() {
        //顶部选择栏
        AllComponents.PANEL_TOP_BAR.component.add(AllComponents.LABEL_TYPE.component);
        AllComponents.PANEL_TOP_BAR.component.add(AllComponents.RADIO_TYPE_POM.component);
        AllComponents.PANEL_TOP_BAR.component.add(AllComponents.RADIO_TYPE_INFO.component);
        AllComponents.PANEL_TOP_BAR.component.add(AllComponents.BUTTON_GENERATOR.component);
        buttonGroup.add(AllComponents.RADIO_TYPE_POM.getComponent(JRadioButton.class));
        buttonGroup.add(AllComponents.RADIO_TYPE_INFO.getComponent(JRadioButton.class));

        //中间信息栏-pom
        AllComponents.PANEL_CONTENT_POM.component.add(new JScrollPane(AllComponents.TEXTAREA_POM.component));

        //中间信息栏-info
        final JPanel jPanel1 = new JPanel();
        jPanel1.add(AllComponents.LABEL_GROUPID.component);
        jPanel1.add(AllComponents.INPUT_GROUPID.component);
        final JPanel jPanel2 = new JPanel();
        jPanel2.add(AllComponents.LABEL_ARTIFACTID.component);
        jPanel2.add(AllComponents.INPUT_ARTIFACTID.component);
        final JPanel jPanel3 = new JPanel();
        jPanel3.add(AllComponents.LABEL_VERSION.component);
        jPanel3.add(AllComponents.INPUT_VERSION.component);
        AllComponents.PANEL_CONTENT_INFO.component.add(jPanel1);
        AllComponents.PANEL_CONTENT_INFO.component.add(jPanel2);
        AllComponents.PANEL_CONTENT_INFO.component.add(jPanel3);

        hideAllContent();
        AllComponents.PANEL_CONTENT.component.add(AllComponents.PANEL_CONTENT_POM.component);
        AllComponents.PANEL_CONTENT.component.add(AllComponents.PANEL_CONTENT_INFO.component);

        //底部结果栏
        AllComponents.PANEL_RESULT.component.add(new JScrollPane(AllComponents.TEXTAREA_RESULT.component));
    }

    private void hideAllContent() {
        AllComponents.PANEL_CONTENT_INFO.component.setVisible(false);
        AllComponents.PANEL_CONTENT_POM.component.setVisible(false);
    }

    private void setListener() {
        //选择pom方式
        AllComponents.RADIO_TYPE_POM.getComponent(JRadioButton.class).addActionListener(e -> {
            hideAllContent();
            AllComponents.PANEL_CONTENT_POM.component.setVisible(true);
        });

        //选择自己填充的方式
        AllComponents.RADIO_TYPE_INFO.getComponent(JRadioButton.class).addActionListener(e -> {
            hideAllContent();
            AllComponents.PANEL_CONTENT_INFO.component.setVisible(true);
        });

        //生成按钮
        AllComponents.BUTTON_GENERATOR.getComponent(JButton.class).addActionListener(e -> {
            final ButtonModel selection = buttonGroup.getSelection();
            System.out.println(selection);
            if (Objects.nonNull(selection)) {
                if (AllComponents.RADIO_TYPE_POM.getComponent(JRadioButton.class).isSelected()) {
                    AllComponents.TEXTAREA_RESULT.getComponent(JTextArea.class).setText(generatorCodeByPom());
                } else if (AllComponents.RADIO_TYPE_INFO.getComponent(JRadioButton.class).isSelected()) {
                    AllComponents.TEXTAREA_RESULT.getComponent(JTextArea.class).setText(generatorCodeByInfo());
                } else {
                    AllComponents.TEXTAREA_RESULT.getComponent(JTextArea.class).setText("不支持的生成方式！");
                }
            } else {
                AllComponents.TEXTAREA_RESULT.getComponent(JTextArea.class).setText("请先选择一种生成方式！");
            }

        });
    }

    private String generatorCodeByPom() {
        try {
            //解析pom文件
            String pomString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dependencies>" + AllComponents.TEXTAREA_POM.getComponent(JTextArea.class).getText() + "</dependencies>";
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
            return "解析xml失败，请确认数据格式！";
        }
    }

    private String generatorCodeByInfo() {
        String groupId = AllComponents.INPUT_GROUPID.getComponent(JTextField.class).getText();
        String artifactid = AllComponents.INPUT_ARTIFACTID.getComponent(JTextField.class).getText();
        String version = AllComponents.INPUT_VERSION.getComponent(JTextField.class).getText();
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

    @Override
    public JPanel getContent() {
        return mvnInstallGeneratorToolJPanel;
    }
}
