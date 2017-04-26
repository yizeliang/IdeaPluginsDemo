package cn.yzl.translate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class SelectUI extends JDialog {
    private JPanel contentPane;
    private JList resultList;
    private String mText;
    private SelectCallBack callBack;

    private int selIndex;

    public SelectUI() {
        setContentPane(contentPane);
        {
            //设置大小
            setMinimumSize(new Dimension(800, 200));
            //居中
            Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
            Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
            int screenWidth = screenSize.width / 2; // 获取屏幕的宽
            int screenHeight = screenSize.height / 2; // 获取屏幕的高
            int height = this.getHeight();
            int width = this.getWidth();
            setLocation(screenWidth - width / 2, screenHeight - height / 2);
        }
        setModal(true);
    }

    public SelectUI(String mText, SelectCallBack callBack) {
        this();
        this.mText = mText;
        this.callBack = callBack;
        initList();
    }

    private void initList() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Vector<String> data = generateCode(mText);

        resultList.setListData(data);

        resultList.setSelectedIndex(0);

        /**
         * 增加键盘处理事件
         */
        resultList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        onOK();
                        dispose();
                    } catch (Exception e1) {

                    }
                }
            }
        });


        resultList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    onOK();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void onOK() {
        callBack.selected(resultList.getSelectedValue().toString());
        dispose();

    }

    private Vector<String> generateCode(String mText) {
        mText = mText.replace(" ", "");

        Vector<String> vector = new Vector<>();

        if (mText.startsWith("the")) {
            vector.addAll(generateCode(mText.replace("the", "")));
        } else if (mText.startsWith("The")) {
            vector.addAll(generateCode(mText.replace("The", "")));
        }


        vector.add("m" + mText);

        String temp = new String();
        temp += mText.substring(0, 1).toLowerCase();
        if (mText.length() > 1) {
            temp += mText.substring(1, mText.length());
        }
        vector.add(temp);
        temp = new String();
        temp += mText.substring(0, 1).toUpperCase();
        if (mText.length() > 1) {
            temp += mText.substring(1, mText.length());
        }
        vector.add(temp);

        return vector;
    }

    public interface SelectCallBack {
        void selected(String text);
    }

}
