package edu.hitsz.dao;

import java.io.*;
import java.text.SimpleDateFormat; //时间格式化类
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;  //Date类
import java.util.List;

public class RankDaoImpl implements RankDao {
    private final List<Rank> rankList;
    private final String dataFilePath;
    private final Comparator<Rank> rankComparator = (r1, r2) -> {
        if (r1.getScore() != r2.getScore()) {
            return Integer.compare(r2.getScore(), r1.getScore());
            //分数降序
        } else {
            return Long.compare(r1.getTime(), r2.getTime());
            //相同分数时间升序
        }
    };

    // 构造函数接收一个文件名
    public RankDaoImpl(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        rankList = loadFromFile();
    }

    private List<Rank> loadFromFile() {
        List<Rank> list = new ArrayList<>();
        File file = new File(dataFilePath);

        //System.out.println("加载排行榜数据: " + dataFilePath);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 4个部分: name, score, formattedTime, timestamp
                String[] parts = line.split(",", 4);

                if (parts.length < 3) {
                    System.err.println("无效的记录格式：" + line + " (文件: " + dataFilePath + ")");
                    continue;
                }

                String name = parts[0].trim();
                int score;
                long time;

                try {
                    score = Integer.parseInt(parts[1].trim());

                    if (parts.length == 3) {
                        time = Long.parseLong(parts[2].trim());
                    } else {
                        time = Long.parseLong(parts[3].trim());
                    }

                    list.add(new Rank(name, score, time));

                } catch (NumberFormatException e) {
                    System.err.println("解析记录失败 (非数字)：" + line + "，错误：" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("加载排名记录失败：" + e.getMessage());
        }

        Collections.sort(list, rankComparator);
        return list;
    }

    private void saveToFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //System.out.println("保存排行榜数据: " + dataFilePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (Rank rank : rankList) {
                // 文件存储格式为-玩家名,分数,格式化时间,时间戳(long)
                String formattedTime = sdf.format(new Date(rank.getTime()));

                String line = String.format("%s,%d,%s,%d",
                        rank.getName().replace(",", ""), // 避免名称中的逗号破坏格式
                        rank.getScore(),
                        formattedTime,
                        rank.getTime()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存排名记录失败：" + e.getMessage());
        }
    }

    @Override
    public void add(Rank rank) {
        rankList.add(rank);
        Collections.sort(rankList, rankComparator);
        saveToFile();
    }

    @Override
    public List<Rank> select() {
        List<Rank> sortedList = new ArrayList<>(rankList);
        Collections.sort(sortedList, rankComparator);
        return sortedList;
    }

    @Override
    public void delete(int index) {
        if (index >= 0 && index < rankList.size()) {
            rankList.remove(index);
            saveToFile();
        } else {
            System.err.println("无效的删除索引：" + index);
        }
    }
}