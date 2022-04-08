package com.zaiji.plugin.date;

import com.nlf.calendar.Lunar;
import com.zaiji.annotation.PluginComponentInfo;
import com.zaiji.util.ErrorInfoUtil;
import com.zaiji.util.ScheduleUtil;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 时间相关工具类
 *
 * @author zaiji
 */

@PluginComponentInfo(name = "时间相关工具类", defaultComponent = true)
public class DateUtil {
    private JTextField td_param;
    private JRadioButton td_radio_second;
    private JRadioButton td_radio_milli;
    private JTextField dt_param;
    private JRadioButton dt_radio_second;
    private JRadioButton dt_radio_milli;
    private JButton td_button;
    private JButton dt_button;
    private JTextField td_result;
    private JTextField dt_result;
    private JTabbedPane tabbedPane1;
    private JLabel nowDateTimeTextLabel;
    private JPanel mainPane;
    private JTextPane lunarTextPane;
    private JTextField countdown_title;
    private JTextField countdown_time;
    private JButton countdown_button;
    private JPanel countdown_pane;

    {
        try {
            //textpane文本居中
            StyledDocument doc = lunarTextPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            //时间定时任务
            ScheduleUtil.executeByPeriod(() -> {
                String printTime = new SimpleDateFormat(DATE_FORMAT_PATTERN).format(new Date());
                nowDateTimeTextLabel.setText(printTime);
            }, 500, TimeUnit.MILLISECONDS);

            ScheduleUtil.executeByPeriod(() -> {
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
            }, 1, TimeUnit.MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public DateUtil() {
        td_button.addActionListener(e -> {
            try {
                Long timeStamp = 0L;
                if (td_radio_second.isSelected()) {
                    timeStamp = Long.parseLong(td_param.getText() + "000");
                } else if (td_radio_milli.isSelected()) {
                    timeStamp = Long.parseLong(td_param.getText());
                }
                td_result.setText(new SimpleDateFormat(DATE_FORMAT_PATTERN).format(new Date(timeStamp)));
            } catch (Exception exception) {
                final String s = ErrorInfoUtil.outPrintStack(exception);
                td_result.setText(s);
            }
        });
        dt_button.addActionListener(e -> {
            try {
                Long timeStamp = 0L;
                final Date parse = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(dt_param.getText());
                if (dt_radio_second.isSelected()) {
                    timeStamp = parse.getTime() / 1000;
                } else if (dt_radio_milli.isSelected()) {
                    timeStamp = parse.getTime();
                }
                dt_result.setText(String.valueOf(timeStamp));
            } catch (Exception exception) {
                final String s = ErrorInfoUtil.outPrintStack(exception);
                dt_result.setText(s);
            }
        });


        countdown_button.addActionListener(e -> {
            try {
                final String title = countdown_title.getText();
                final Date deaLine = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(countdown_time.getText());
                if (deaLine.getTime() <= new Date().getTime()) {
                    countdown_time.setText("结束时间不得早于当前时间");
                    return;
                }
                final JPanel jPanel = new JPanel();
                final JLabel jLabel = new JLabel();
                final JButton removeButton = new JButton("移除");
                jPanel.setLayout(new BorderLayout());
                jPanel.add(jLabel, BorderLayout.CENTER);
                jPanel.add(removeButton, BorderLayout.EAST);
                final GridLayout gridLayout = new GridLayout(0, 1);

                countdown_pane.setLayout(gridLayout);
                countdown_pane.add(jPanel, 0, 0);
                countdown_pane.updateUI();

                //定时任务
                final ScheduledFuture<?> scheduledFuture = ScheduleUtil.executeByPeriodWithEndTime(() -> {
                    final Date tempDeadline = deaLine;
                    jLabel.setText("【" + title + "】剩余时间：" + getCountdownString(tempDeadline));
                }, 500, TimeUnit.MILLISECONDS, deaLine);

                //移除按钮的点击事件
                removeButton.addActionListener(eee -> {
                    if (!scheduledFuture.isCancelled()) {
                        scheduledFuture.cancel(true);
                    }
                    countdown_pane.remove(jPanel);
                });
            } catch (Exception exception) {
                exception.printStackTrace();
                countdown_time.setText(ErrorInfoUtil.outPrintStack(exception));
            }
        });
    }

    private static String getCountdownString(Date deadline) {
        Long leftTime = deadline.getTime() - new Date().getTime();

        long day = leftTime / (24 * 60 * 60 * 1000);
        leftTime -= day * 24 * 60 * 60 * 1000;

        long hours = leftTime / (60 * 60 * 1000);
        leftTime -= hours * 60 * 60 * 1000;

        long minutes = leftTime / (60 * 1000);
        leftTime -= minutes * 60 * 1000;

        long seconds = leftTime / 1000;

        return day + " 天 " + hours + " 小时 " + minutes + " 分 " + seconds + " 秒 ";
    }

    public JPanel getContent() {
        return mainPane;
    }

}
