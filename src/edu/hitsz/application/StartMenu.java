package edu.hitsz.application;

import edu.hitsz.dao.RankDao;

import javax.swing.*;
import java.awt.*;

public class StartMenu {
    private JPanel mainPanel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private JCheckBox soundCheckBox;
    private JLabel musicLabel;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JFrame frame;
    public StartMenu(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        easyButton.addActionListener(e -> startGame("EASY"));
        normalButton.addActionListener(e -> startGame("NORMAL"));
        hardButton.addActionListener(e -> startGame("HARD"));
    }

    private void startGame(String difficulty) {
        //Main类获取特定难度的DAO
        RankDao currentRankDao = Main.rankDaos.get(difficulty);
        if (currentRankDao == null) {
            System.err.println("未找到难度 " + difficulty + " 对应的RankDao");
            return;
        }

        // 模板模式：根据难度实例化对应的Game子类
        GameTemplate game;
        switch (difficulty) {
            case "NORMAL":
                game = new NormalGame(difficulty, soundCheckBox.isSelected(), cardLayout, cardPanel, currentRankDao);
                break;
            case "HARD":
                game = new HardGame(difficulty, soundCheckBox.isSelected(), cardLayout, cardPanel, currentRankDao);
                break;
            case "EASY":
            default:
                game = new EasyGame(difficulty, soundCheckBox.isSelected(), cardLayout, cardPanel, currentRankDao);
                break;
        }

        cardPanel.add(game, "GAME");
        cardLayout.show(cardPanel, "GAME");
        game.action();
        game.requestFocusInWindow();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {
        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        easyButton = new JButton();
        easyButton.setText("简单模式");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(easyButton, gbc);
        normalButton = new JButton();
        normalButton.setText("普通模式");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(normalButton, gbc);
        hardButton = new JButton();
        hardButton.setText("困难模式");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(hardButton, gbc);
        soundCheckBox = new JCheckBox();
        soundCheckBox.setSelected(true);
        soundCheckBox.setText("开启音效");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(soundCheckBox, gbc);
    }

    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}