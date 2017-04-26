package cn.yzl.translate.ui;

import cn.yzl.translate.Modle;

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
    private Modle model;
    private SelectCallBack callBack;

    public SelectUI() {
        setContentPane(contentPane);
        {
            //设置大小
            setMinimumSize(new Dimension(800, 250));
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

    public SelectUI(Modle model, SelectCallBack callBack) {
        this();
        this.model = model;
        this.callBack = callBack;
        initList();
    }

    private void initList() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Vector<String> data = new Vector<>();
        for (int i = 0; i < model.getTranslation().size(); i++) {
            data.addAll(generateCode(model.getTranslation().get(i)));
        }
        if (model.getBasic().getExplains() != null) {
            for (int i = 0; i < model.getBasic().getExplains().size(); i++) {
                data.addAll(generateCode(model.getBasic().getExplains().get(i)));
            }
        }
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
//        String text = mText.replace(" ", "");
        //去掉括号里面的内容
        String text = mText.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
        //去掉非英文字符
        text = text.replaceAll("[^a-zA-Z]+", "");
        Vector<String> vector = new Vector<>();

        if (text.startsWith("the")) {
            vector.addAll(generateCode(text.replace("the", "")));
        } else if (text.startsWith("The")) {
            vector.addAll(generateCode(text.replace("The", "")));
        }

        String temp = new String();
        temp = "m";
        temp += text.substring(0, 1).toUpperCase();
        if (text.length() > 1) {
            temp += text.substring(1, text.length());
        }
        vector.add(temp);

        temp = new String();
        temp += text.substring(0, 1).toLowerCase();
        if (text.length() > 1) {
            temp += text.substring(1, text.length());
        }
        vector.add(temp);


        temp = new String();
        temp += text.substring(0, 1).toUpperCase();

        if (text.length() > 1) {
            temp += text.substring(1, text.length());
        }
        vector.add(temp);
        return vector;
    }

    public interface SelectCallBack {
        void selected(String text);
    }

    public static void main(String[] args) {
        String s = "我是一个人（中国人）aaa[真的]bbbb{确定}";

        s = s.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");

        System.out.println(s);
    }
}
