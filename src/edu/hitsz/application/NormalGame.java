package edu.hitsz.application;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.dao.RankDao;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class NormalGame extends GameTemplate {

    public NormalGame(String difficulty, boolean soundOn, CardLayout cardLayout, JPanel cardPanel, RankDao rankDao) {
        super(difficulty, soundOn, cardLayout, cardPanel, rankDao);
    }

    @Override
    protected void setGameParameters() {
        this.enemyMaxNumber = 5;
        this.baseEnemyMaxNumber = 5;
        this.cycleDuration = 600;
        this.baseCycleDuration = 600;
        this.bossThreshold = 1000;
        this.bossHp = 700;
        this.increaseBossHp = false;
        this.increaseDifficultyOverTime = true;
    }

    @Override
    protected void setBackgroundImage() {
        this.backgroundImage = ImageManager.BACKGROUND_NORMAL;
    }

    @Override
    protected void spawnEnemies() {
        // 生成普通和精英敌机
        // 计时判断在GameTemplate的action()方法
        if (enemyAircrafts.size() < enemyMaxNumber) {
            double randomValue = Math.random();
            EnemyAircraft newEnemy;
            if (randomValue < 0.30) { // 30%概率精英
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
        // 普通模式每11秒生成一次超级精英敌机
        if (currentTime - lastSuperEliteSpawnTime > (SUPER_ELITE_SPAWN_INTERVAL - 4000) &&
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
            EnemyAircraft boss = createBoss(); // 创建Boss，HP固定
            bombPublisher.addObserver(boss);
            enemyAircrafts.add(boss);

            bossExists = true;
            bossThreshold += 950; // 设置下一个Boss出现的分数阈值

            // 播放Boss BGM
            if (soundOn) {
                if (bgmThread != null) bgmThread.stopMusic();
                bossBgmThread = new MusicThread("src/videos/bgm_boss.wav", true);
                bossBgmThread.start();
            }
        }
    }
}