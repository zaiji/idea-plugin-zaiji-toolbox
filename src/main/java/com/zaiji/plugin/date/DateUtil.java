package com.zaiji.plugin.date;

import com.nlf.calendar.Lunar;
import com.zaiji.annotation.PluginComponentInfo;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 时间相关工具类
 *
 * @author zaiji
 */
//todo:

@PluginComponentInfo(name = "时间相关工具类", defaultComponent = true)
public class DateUtil {
    private JTextField textField1;
    private JRadioButton 秒RadioButton;
    private JRadioButton 毫秒RadioButton;
    private JTextField textField2;
    private JRadioButton 秒RadioButton1;
    private JRadioButton 毫秒RadioButton1;
    private JButton 转换Button1;
    private JButton 转换Button;
    private JTextField textField3;
    private JTextField textField4;
    private JTabbedPane tabbedPane1;
    private JLabel nowDateTimeTextLabel;
    private JPanel mainPane;
    private JTextPane lunarTextPane;
    private JTextField textField5;
    private JTextField textField6;
    private JButton 新增Button;

    //定时任务
    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    {
        try {
            //textpane文本居中
            StyledDocument doc = lunarTextPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            //时间定时任务
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                String printTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                nowDateTimeTextLabel.setText(printTime);
            }, 0, 1000, TimeUnit.MILLISECONDS);

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                //农历定时任务
                final Lunar lunar = Lunar.fromDate(new Date());
                StringBuilder s = new StringBuilder();
                s.append(lunar);
                s.append("\r\n");
                s.append(lunar.getYearInGanZhi());
                s.append("(");
                s.append(lunar.getYearShengXiao());
                s.append(")年 ");
                s.append(lunar.getMonthInGanZhi());
                s.append("(");
                s.append(lunar.getMonthShengXiao());
                s.append(")月 ");
                s.append(lunar.getDayInGanZhi());
                s.append("(");
                s.append(lunar.getDayShengXiao());
                s.append(")日 ");
                s.append(lunar.getTimeZhi());
                s.append("(");
                s.append(lunar.getTimeShengXiao());
                s.append(")时");
                s.append("\r\n");
                s.append("星期");
                s.append(lunar.getWeekInChinese());


                lunarTextPane.setText(s.toString());
            }, 0, 1000, TimeUnit.MILLISECONDS);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public JPanel getContent() {
        return mainPane;
    }
}
