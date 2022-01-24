package com.zaiji.plugin.date;

import com.nlf.calendar.Lunar;
import com.zaiji.annotation.PluginComponentInfo;

import javax.swing.*;
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
    private JLabel lunarCalendarTextLabel;

    //定时任务
    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    {
        try {

            //时间定时任务
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                String printTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                nowDateTimeTextLabel.setText(printTime);
            }, 0, 1000, TimeUnit.MILLISECONDS);

            //农历定时任务
            final Lunar lunar = Lunar.fromDate(new Date());
            lunarCalendarTextLabel.setText(lunar.toFullString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public JPanel getContent() {
        return mainPane;
    }
}
