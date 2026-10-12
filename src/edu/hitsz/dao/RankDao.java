package edu.hitsz.dao;
import edu.hitsz.dao.Rank;
import java.util.List;

public interface RankDao {
    //添加游戏记录
    void add(Rank rank);
    //查询游戏记录
    List<Rank> select();
    //删除游戏记录
    void delete(int index);
}