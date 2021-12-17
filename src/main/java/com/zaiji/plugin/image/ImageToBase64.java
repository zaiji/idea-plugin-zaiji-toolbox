package com.zaiji.plugin.image;

import com.intellij.ui.components.JBScrollPane;
import com.zaiji.plugin.BaseComponentClass;
import com.zaiji.plugin.util.ErrorInfoUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片转base64工具类
 *
 * @author zaiji
 */

public class ImageToBase64 extends BaseComponentClass {
    private JPanel panel1;
    private JPanel base64ToPicJpane;
    private JPanel picToBase64Jpane;
    private JButton buttonChooseFile;
    private JTextArea base64ResultTextArea;
    private JPanel picParamPane;
    private JButton buttonChangeToPic;
    private JTextArea base64paramTextArea;
    private JPanel picResultPane;

    final static Pattern BASE64_PREFIX_REX = Pattern.compile("^data:image/(.)*;base64,");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG & PNG Images", "jpg", "png", "jpeg");

    public ImageToBase64() {
        buttonChooseFile.addActionListener(e -> {
            final JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.setFileFilter(filter);

            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                //正常选择文件
                final File selectedFile = jFileChooser.getSelectedFile();
                ImageIcon img = new ImageIcon(selectedFile.getAbsolutePath());// 创建图片对象
                picParamPane.removeAll();
                picParamPane.setLayout(new BorderLayout());
                picParamPane.add(new JBScrollPane(new JLabel(img)), BorderLayout.CENTER);
                picParamPane.updateUI();
                try (FileInputStream fileInputStream = new FileInputStream(selectedFile); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
                    final byte[] buffer = new byte[1024];
                    int i = 0;
                    while ((i = fileInputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, i);
                    }
                    String base64 = Base64.getMimeEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                    base64ResultTextArea.setText("data:image/jpeg;base64," + base64);
                } catch (Exception exception) {
                    base64ResultTextArea.setText(ErrorInfoUtil.outPrintStack("图片转换错误", exception));
                }
            } else {
                //未正常选择文件，如选择取消按钮
                System.out.println("未选择文件!");
            }
        });
        buttonChangeToPic.addActionListener(e -> {
            String base64 = base64paramTextArea.getText();

            if (base64.startsWith("data:image")) {
                //去除base64前缀
                Matcher matcher = BASE64_PREFIX_REX.matcher(base64);

                if (matcher.find()) {
                    base64 = matcher.replaceFirst("");
                }
            }
            final byte[] decode = Base64.getMimeDecoder().decode(base64);

            picResultPane.removeAll();
            picResultPane.setLayout(new BorderLayout());
            picResultPane.add(new JBScrollPane(new JLabel(new ImageIcon(decode))), BorderLayout.CENTER);
            picResultPane.updateUI();
        });
    }

    @Override
    protected JPanel getContent() {
        return panel1;
    }
}
