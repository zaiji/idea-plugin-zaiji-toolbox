package com.zaiji.plugin.log4j2;

import com.zaiji.plugin.BaseComponentClass;
import com.zaiji.plugin.log4j2.entity.ConfigEntity;
import com.zaiji.plugin.log4j2.entity.LogFileType;
import com.zaiji.plugin.log4j2.entity.LogLevel;
import com.zaiji.plugin.util.ErrorInfoUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Log4j2ConfigFileGenerator extends BaseComponentClass {
    private JTextArea resultTextArea;
    private JRadioButton withConsoleRadioBtn;
    private JRadioButton withoutConsoleRadioBtn;
    private JTextField fileNameTextField;
    private JTextField fileSizeTextField;
    private JComboBox fileSizeUnitComboBox;
    private JTextField fileNumTextField;
    private JRadioButton logLevelInfoBtn;
    private JRadioButton logLevelWarnBtn;
    private JRadioButton logLevelErrorBtn;
    private JPanel mainJPane;
    private JRadioButton log4j2LogLevelInfoBtn;
    private JRadioButton log4j2LogLevelWarnBtn;
    private JRadioButton log4j2LogLevelErrorBtn;
    private JRadioButton xmlTypeRadioBtn;
    private JRadioButton ymlTypeRadioBtn;
    private JButton 生成Button;
    private ButtonGroup log4j2LogLevelBtnGroup;
    private ButtonGroup logLevelBtnGroup;

    private final static String LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss}|%level|[%thread]|%logger{0}|%method|%line-%msg%n";

    public Log4j2ConfigFileGenerator() {
        生成Button.addActionListener(e -> {
            final ConfigEntity configInfo = getConfigInfo();
            switch (configInfo.getFileType()) {
                case xml: {
                    resultTextArea.setText(generatorXmlConfig(configInfo));
                    break;
                }
                case yml: {
                    resultTextArea.setText(generatorYmlConfig(configInfo));
                    break;
                }
                default: {
                    final String errorInfo = ErrorInfoUtil.outPrintStack(new RuntimeException("不支持的配置文件格式"));
                    resultTextArea.setText(errorInfo);
                }
            }
        });
    }

    private String generatorXmlConfig(ConfigEntity configInfo) {
        //创建document对象
        Document document = DocumentHelper.createDocument();
        //创建根节点Configuration
        Element configuration = document.addElement("Configuration");
        configuration.addAttribute("status", configInfo.getLog4j2LogLevel().toString().toUpperCase());

        //生成properties节点，统一配置
        final Element properties = configuration.addElement("Properties");
        //配置-文件名
        final Element propertyLogFileName = properties.addElement("Property");
        propertyLogFileName.addAttribute("name", "LOG_FILE");
        propertyLogFileName.setText("log/" + configInfo.getLogFileName());

        //配置-日志前缀
        final Element propertyLogPattern = properties.addElement("Property");
        propertyLogPattern.addAttribute("name", "LOG_PATTERN");
        propertyLogPattern.setText(LOG_PATTERN);

        //生成appenders节点，定义不同的输出
        final Element appenders = configuration.addElement("Appenders");
        //控制台输出
        final Element appenderConsole = appenders.addElement("Console");
        appenderConsole.addAttribute("name", "Console");
        appenderConsole.addAttribute("target", "SYSTEM_OUT");
        appenderConsole.addAttribute("follow", "true");
        final Element patternLayout1 = appenderConsole.addElement("PatternLayout");
        patternLayout1.addAttribute("pattern", "${LOG_PATTERN}");

        //滚动文件输出
        final Element appenderRollingFile = appenders.addElement("RollingFile");
        appenderRollingFile.addAttribute("name", "RollingFile");
        appenderRollingFile.addAttribute("fileName", "${LOG_FILE}");
        appenderRollingFile.addAttribute("append", "true");
        appenderRollingFile.addAttribute("filePattern", "log/$${date:yyyy-MM}/" + configInfo.getLogFileName() + "-%d{MM-dd-yyyy}-%i.log.gz");
        final Element patternLayout2 = appenderRollingFile.addElement("PatternLayout");
        patternLayout2.addAttribute("pattern", "${LOG_PATTERN}");
        final Element sizeBasedTriggeringPolicy = appenderRollingFile.addElement("SizeBasedTriggeringPolicy");
        sizeBasedTriggeringPolicy.addAttribute("size", configInfo.getFileRollingSize() + configInfo.getFileRollingSizeUnit());

        //生成loggers节点，定义实际输出
        final Element loggers = configuration.addElement("Loggers");
        final Element root = loggers.addElement("Root");
        root.addAttribute("level", configInfo.getLogLevel().toString().toUpperCase());
        final Element appenderRefRollingFile = root.addElement("AppenderRef");
        appenderRefRollingFile.addAttribute("ref", "RollingFile");

        if (configInfo.isNeedConsolePrint()) {
            final Element appenderRefConsole = root.addElement("AppenderRef");
            appenderRefConsole.addAttribute("ref", "Console");
        }

        // 5、设置生成xml的格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置编码格式
        format.setEncoding("UTF-8");
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            XMLWriter writer = new XMLWriter(byteArrayOutputStream, format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return ErrorInfoUtil.outPrintStack(e);
        }
    }

    private String generatorYmlConfig(ConfigEntity configInfo) {
        //创建根节点Configuration
        Map<String, Object> configuration = new HashMap<>(5);
        configuration.put("status", configInfo.getLog4j2LogLevel().toString().toUpperCase());

        //生成appenders节点，定义不同的输出
        Map<String, Object> appenders = new HashMap<>();
        configuration.put("appenders", appenders);

        //控制台输出
        Map<String, Object> console = new HashMap<>();
        appenders.put("Console", console);
        console.put("name", "Console");
        console.put("target", "SYSTEM_OUT");
        Map<String, Object> patternLayout1 = new HashMap<>();
        patternLayout1.put("Pattern", LOG_PATTERN);
        console.put("PatternLayout", patternLayout1);
        //滚动文件输出
        Map<String, Object> rollingFile = new HashMap<>();
        appenders.put("RollingFile", rollingFile);
        rollingFile.put("name", "RollingFile");
        rollingFile.put("fileName", "log/" + configInfo.getLogFileName());
        Map<String, Object> patternLayout2 = new HashMap<>();
        patternLayout2.put("Pattern", LOG_PATTERN.replaceAll(" - "," \\- "));
        rollingFile.put("PatternLayout", patternLayout2);
        rollingFile.put("filePattern", "log/$${date:yyyy-MM}/" + configInfo.getLogFileName() + "-%d{MM-dd-yyyy}-%i.log.gz");
        Map<String, Object> policies = new HashMap<>();
        rollingFile.put("Policies", policies);
        Map<Object, Object> sizeBasedTriggeringPolicy = new HashMap<>();
        policies.put("SizeBasedTriggeringPolicy", sizeBasedTriggeringPolicy);
        sizeBasedTriggeringPolicy.put("size", configInfo.getFileRollingSize() + configInfo.getFileRollingSizeUnit());

        //生成loggers节点，定义实际输出
        Map<String, Object> loggers = new HashMap<>();
        configuration.put("Loggers", loggers);
        Map<String, Object> root = new HashMap<>();
        loggers.put("Root", root);
        root.put("level", configInfo.getLogLevel().toString().toUpperCase());
        List<Map<String, Object>> appenderRef = new LinkedList<>();
        root.put("AppenderRef", appenderRef);
        Map<String, Object> appenderRefRollingFile = new HashMap<>();
        appenderRef.add(appenderRefRollingFile);
        appenderRefRollingFile.put("ref", "RollingFile");

        if (configInfo.isNeedConsolePrint()) {
            Map<String, Object> appenderRefConsole = new HashMap<>();
            appenderRef.add(appenderRefConsole);
            appenderRefConsole.put("ref", "Console");
        }
        Yaml yaml = new Yaml();
        final HashMap<Object, Object> result = new HashMap<>(3);
        result.put("Configuration",configuration);
        return yaml.dump(result);
    }

    private ConfigEntity getConfigInfo() {
        final ConfigEntity configEntity = new ConfigEntity();
        //获取配置文件格式
        if (xmlTypeRadioBtn.isSelected()) {
            configEntity.setFileType(LogFileType.xml);
        } else if (ymlTypeRadioBtn.isSelected()) {
            configEntity.setFileType(LogFileType.yml);
        }

        //是否需要控制台输出
        configEntity.setNeedConsolePrint(withConsoleRadioBtn.isSelected());

        //日志等级
        configEntity.setLogLevel(getLogLevel(logLevelBtnGroup.getSelection(), logLevelBtnGroup));

        //文件名
        configEntity.setLogFileName(fileNameTextField.getText());

        //文件滚动大小
        configEntity.setFileRollingSize(fileSizeTextField.getText());

        //文件滚动大小单位
        configEntity.setFileRollingSizeUnit(String.valueOf(fileSizeUnitComboBox.getSelectedItem()));

        //文件数量
        configEntity.setFileNum(fileNumTextField.getText());

        //log4j2本身日志级别
        configEntity.setLog4j2LogLevel(getLogLevel(log4j2LogLevelBtnGroup.getSelection(), log4j2LogLevelBtnGroup));

        return configEntity;
    }

    private LogLevel getLogLevel(ButtonModel selectBtnModel, ButtonGroup buttonGroup) {
        final Enumeration<AbstractButton> elements = buttonGroup.getElements();
        while (elements.hasMoreElements()) {
            final AbstractButton abstractButton = elements.nextElement();
            if (selectBtnModel.equals(abstractButton.getModel())) {
                return LogLevel.getValueOf(abstractButton.getText());
            }
        }
        return null;
    }

    @Override
    protected JPanel getContent() {
        return mainJPane;
    }
}
