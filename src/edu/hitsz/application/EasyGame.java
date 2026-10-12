package edu.hitsz.application;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.dao.RankDao;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class EasyGame extends GameTemplate {

    public EasyGame(String difficulty, boolean soundOn, CardLayout cardLayout, JPanel cardPanel, RankDao rankDao) {
        super(difficulty, soundOn, cardLayout, cardPanel, rankDao);
    }

    @Override
    protected void setGameParameters() {
        this.enemyMaxNumber = 4;
        this.baseEnemyMaxNumber = 4;
        this.cycleDuration = 650;
        this.baseCycleDuration = 650;
        this.increaseDifficultyOverTime = false;
    }

    @Override
    protected void setBackgroundImage() {
        this.backgroundImage = ImageManager.BACKGROUND_EASY;
    }

    @Override
    protected void spawnEnemies() {
        // 简单模式只生成普通和精英敌机
        // 计时判断在GameTemplate的action()方法中
        if (enemyAircrafts.size() < enemyMaxNumber) {
            double randomValue = Math.random();
            EnemyAircraft newEnemy;
            if (randomValue < 0.25) { // 25%概率精英
                newEnemy = eliteFactory.createEnemyAircraft();
            } else { // 70%概率普通
                newEnemy = mobFactory.createEnemyAircraft();
            }
            bombPublisher.addObserver(newEnemy);
            enemyAircrafts.add(newEnemy);
        }
    }

    // spawnSuperElite() 用GameTemplate中的默认实现 15s生成一次超级精英敌机

    @Override
    protected void spawnBoss() {
        // 简单模式没有Boss
    }

    @Override
    protected void updateDifficulty() {
        // 简单模式难度不随时间增加
    }
}