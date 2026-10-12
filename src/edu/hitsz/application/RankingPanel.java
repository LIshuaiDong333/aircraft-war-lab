package edu.hitsz.application;

import edu.hitsz.dao.Rank;
import edu.hitsz.dao.RankDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RankingPanel {
    private JPanel mainPanel;
    private JTable rankTable;
    private JButton deleteButton;
    private JScrollPane tableScrollPane;
    private JLabel headerLabel;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private RankDao rankDao;
    private DefaultTableModel model;

    public RankingPanel(String difficulty, CardLayout cardLayout, JPanel cardPanel, RankDao rankDao) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.rankDao = rankDao;

        headerLabel.setText("难度:" + difficulty + "             排行榜");

        // 初始化表格
        setupTable();
        // 加载数据
        loadRankData();

        // 添加按钮监听器
        deleteButton.addActionListener(e -> deleteSelectedRank());
        // 移除 backButton 监听器
    }

    private void setupTable() {
        // 设置表头
        String[] columnNames = {"名次", "玩家名称", "得分", "记录时间"};
        // 创建表格模型，并设置为不可编辑
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        rankTable.setModel(model);
        tableScrollPane.setViewportView(rankTable);
    }

    private void loadRankData() {
        // 清空旧数据
        model.setRowCount(0);
        List<Rank> ranks = rankDao.select();

        // "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < ranks.size(); i++) {
            Rank rank = ranks.get(i);
            model.addRow(new Object[]{
                    i + 1,
                    rank.getName(),
                    rank.getScore(),
                    sdf.format(new Date(rank.getTime()))
            });
        }
    }

    private void deleteSelectedRank() {
        int selectedRow = rankTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "请先选择要删除的记录！");
            return;
        }

        // 弹出确认对话框
        int result = JOptionPane.showConfirmDialog(mainPanel,
                "您确定要删除这条记录吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            // 从DAO删除
            rankDao.delete(selectedRow);
            // 刷新排名（删除后名次会变）
            loadRankData();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {

        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        headerLabel = new JLabel();
        headerLabel.setText("难度：EASY 排行榜");
        panel1.add(headerLabel);
        mainPanel.add(panel1, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        deleteButton = new JButton();
        deleteButton.setText("删除所选记录");
        panel2.add(deleteButton);

        // 移除 backButton 的组件初始化

        mainPanel.add(panel2, BorderLayout.SOUTH);
        tableScrollPane = new JScrollPane();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        rankTable = new JTable();
        tableScrollPane.setViewportView(rankTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}