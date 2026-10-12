package edu.hitsz.application;

import edu.hitsz.dao.RankDao;
import edu.hitsz.dao.RankDaoImpl;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static CardLayout cardLayout = new CardLayout(0, 0);
    public static JPanel cardPanel = new JPanel(cardLayout);

    // 为每种难度创建一个独立的DAO实例，对应不同的文件
    public static Map<String, RankDao> rankDaos = new HashMap<>();
    static {
        rankDaos.put("EASY", new RankDaoImpl("ranks_easy.txt"));
        rankDaos.put("NORMAL", new RankDaoImpl("ranks_normal.txt"));
        rankDaos.put("HARD", new RankDaoImpl("ranks_hard.txt"));
    }

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartMenu startMenu = new StartMenu(frame, cardLayout, cardPanel);
        cardPanel.add(startMenu.getMainPanel(), "START");

        frame.add(cardPanel);
        frame.setVisible(true);

        cardLayout.show(cardPanel, "START");
    }
}
