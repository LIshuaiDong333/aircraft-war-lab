package edu.hitsz.application;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.dao.RankDao;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class HardGame extends GameTemplate {

    public HardGame(String difficulty, boolean soundOn, CardLayout cardLayout, JPanel cardPanel, RankDao rankDao) {
        super(difficulty, soundOn, cardLayout, cardPanel, rankDao);
    }

    @Override
    protected void setGameParameters() {
        this.enemyMaxNumber = 6;
        this.baseEnemyMaxNumber = 6;
        this.cycleDuration = 580;
        this.baseCycleDuration = 580;
        this.bossThreshold = 900;
        this.bossHp = 800;
        this.increaseBossHp = true;
        this.increaseDifficultyOverTime = true;
    }

    @Override
    protected void setBackgroundImage() {
        this.backgroundImage = ImageManager.BACKGROUND_HARD;
    }

    @Override
    protected void spawnEnemies() {
        // 生成普通和精英敌机
        // 计时判断已移至 GameTemplate 的 action() 方法中
        if (enemyAircrafts.size() < enemyMaxNumber) {
            double randomValue = Math.random();
            EnemyAircraft newEnemy;
            if (randomValue < 0.32) { // 32% 概率精英
                newEnemy = eliteFactory.createEnemyAircraft();
            } else {
                newEnemy = mobFactory.createEnemyAircraft();
            }
            bombPublisher.addObserver(newEnemy);
            enemyAircrafts.add(newEnemy);
        }
    }

    @Override
    protected void spawnSuperElite(long currentTime) {
        // 困难模式每8秒生成一次超级精英敌机
        if (currentTime - lastSuperEliteSpawnTime > (SUPER_ELITE_SPAWN_INTERVAL - 7000) &&
                enemyAircrafts.size() < enemyMaxNumber) {
            EnemyAircraft superElite = superEliteFactory.createEnemyAircraft();
            bombPublisher.addObserver(superElite);
            enemyAircrafts.add(superElite);
            lastSuperEliteSpawnTime = currentTime;
        }
    }


    @Override
    protected void spawnBoss() {
        if (score >= bossThreshold && !bossExists) {
            EnemyAircraft boss = createBoss(); // 创建Boss，HP会随次数增加
            bombPublisher.addObserver(boss);
            enemyAircrafts.add(boss);

            bossExists = true;
            bossThreshold += 900; // Boss阈值增长更快

            // 播放Boss BGM
            if (soundOn) {
                if (bgmThread != null) bgmThread.stopMusic();
                bossBgmThread = new MusicThread("src/videos/bgm_boss.wav", true);
                bossBgmThread.start();
            }
        }
    }

    @Override
    protected void updateDifficulty() {
        if (time % 10000 == 0 && time > 0) {  // 每10秒提升一次难度
            difficultyFactor += 0.03;
            enemyMaxNumber = (int) (baseEnemyMaxNumber * difficultyFactor);
            cycleDuration = (int) (baseCycleDuration / difficultyFactor);
            cycleDuration = Math.max(cycleDuration, 520); // 周期下限更低
            System.out.printf("高难-难度提升: 难度系数:%.2f, 敌机周期:%d, 敌机上限:%d\n",
                    difficultyFactor, cycleDuration, enemyMaxNumber);
        }
    }
}