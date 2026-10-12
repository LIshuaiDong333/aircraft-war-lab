package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.*;
import edu.hitsz.observer.BombPublisher;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombSupply;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import edu.hitsz.dao.Rank;
import edu.hitsz.dao.RankDao;

import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.*;

// 游戏主体逻辑模板方法
public abstract class GameTemplate extends JPanel {

    protected int backGroundTop = 0;

    protected final ScheduledExecutorService executorService;
    protected final int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;

    // 观察者模式炸弹发布者
    protected final BombPublisher bombPublisher = new BombPublisher();

    // 工厂
    protected final EnemyFactory mobFactory;
    protected final EnemyFactory eliteFactory;
    protected final EnemyFactory superEliteFactory;

    // 游戏状态
    protected int score = 0;
    protected int time = 0;
    protected int cycleTime = 0;
    protected boolean gameOverFlag = false;
    protected boolean bossExists = false;
    protected long lastSuperEliteSpawnTime = 0;
    protected static final int SUPER_ELITE_SPAWN_INTERVAL = 15000; // 默认超级精英机生成间隔15s

    // 难度参数，由子类设置
    protected int enemyMaxNumber;
    protected int cycleDuration;
    protected int bossThreshold;
    protected int bossHp;
    protected boolean increaseBossHp;
    protected boolean increaseDifficultyOverTime;
    protected double difficultyFactor = 1.0;
    protected int baseEnemyMaxNumber;
    protected int baseCycleDuration;


    // UI与音效
    protected final String difficulty;
    protected final boolean soundOn;
    protected final CardLayout cardLayout;
    protected final JPanel cardPanel;
    protected final RankDao rankDao;
    protected BufferedImage backgroundImage;
    protected MusicThread bgmThread;
    protected MusicThread bossBgmThread;

    public GameTemplate(String difficulty, boolean soundOn, CardLayout cardLayout, JPanel cardPanel, RankDao rankDao) {
        this.difficulty = difficulty;
        this.soundOn = soundOn;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.rankDao = rankDao;

        this.heroAircraft = HeroAircraft.getInstance();
        // 重置英雄机状态
        this.heroAircraft.increaseHp(1000);
        this.heroAircraft.setShootStrategy(this.heroAircraft.getBaseStrategy());

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new ArrayList<>();

        // 初始化工厂
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        superEliteFactory = new SuperEliteFactory();

        setGameParameters();

        setBackgroundImage();

        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }


    // 设置游戏难度参数
    protected abstract void setGameParameters();

    // 生成普通/精英敌机
    protected abstract void spawnEnemies();

    // 生成Boss
    protected abstract void spawnBoss();

    // 钩子方法：生成超级精英机
    protected void spawnSuperElite(long currentTime) {
        if (currentTime - lastSuperEliteSpawnTime > SUPER_ELITE_SPAWN_INTERVAL &&
                enemyAircrafts.size() < enemyMaxNumber) {
            EnemyAircraft superElite = superEliteFactory.createEnemyAircraft();
            bombPublisher.addObserver(superElite);
            enemyAircrafts.add(superElite);
            lastSuperEliteSpawnTime = currentTime;
        }
    }


    // 钩子方法：设置背景图片
    protected void setBackgroundImage() {
        this.backgroundImage = ImageManager.BACKGROUND_EASY; // 默认
    }

    //钩子方法：随时间增加难度
    protected void updateDifficulty() {
        if (time % 15000 == 0 && time > 0) {   // 每15秒加一点难度
            difficultyFactor += 0.02;
            enemyMaxNumber = (int) (baseEnemyMaxNumber * difficultyFactor);
            cycleDuration = (int) (baseCycleDuration / difficultyFactor);
            cycleDuration = Math.max(cycleDuration, 550);   // 子弹射击周期下限
            System.out.printf("难度提升: 难度系数:%.2f, 敌机周期:%d, 敌机上限:%d\n",
                    difficultyFactor, cycleDuration, enemyMaxNumber);
        }
    }

    // 创建Boss
    protected EnemyAircraft createBoss() {
        EnemyAircraft boss = new BossEnemy(
                Main.WINDOW_WIDTH / 2,
                100,
                2,
                0,
                this.bossHp  //用子类设置的HP
        );

        if (increaseBossHp) {
            this.bossHp += 150;
            System.out.println("Boss血量提升, 下次出现血量: " + this.bossHp);
        }
        return boss;
    }


    //模板方法，游戏启动入口，执行游戏逻辑
    public void action() {

        // 播放BGM
        if (soundOn) {
            bgmThread = new MusicThread("src/videos/bgm.wav", true);
            bgmThread.start();
        }

        Runnable task = () -> {
            if (gameOverFlag) {
                return;
            }

            time += timeInterval;
            long currentTime = System.currentTimeMillis();

            // 周期性事件，timeCountAndNewCycleJudge()是唯一的周期触发器
            if (timeCountAndNewCycleJudge()) {
                spawnEnemies();   // 刷普通/精英敌机
                shootAction();    // 射击
            }

            // 刷超级精英机
            spawnSuperElite(currentTime);

            // 生成Boss
            spawnBoss();

            // 更新难度
            if (increaseDifficultyOverTime) {
                updateDifficulty();
            }

            // 物体移动
            bulletsMoveAction();
            aircraftsMoveAction();
            props.forEach(AbstractFlyingObject::forward);

            // 碰撞检测
            crashCheckAction();

            // 后处理移除无效对象
            postProcessAction();

            // 重绘
            repaint();

            // 游戏结束检测
            if (heroAircraft.getHp() <= 0) {
                executorService.shutdown();
                gameOverFlag = true;

                // 停止所有音乐
                if (soundOn) {
                    if (bgmThread != null) bgmThread.stopMusic();
                    if (bossBgmThread != null) bossBgmThread.stopMusic();
                    // 播放游戏结束音效
                    new MusicThread("src/videos/game_over.wav", false).start();
                }

                // 切换到排行榜
                SwingUtilities.invokeLater(this::showRankingPanel);
            }
        };
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }


    private void showRankingPanel() {
        System.out.println("Game Over!");

        String promptMessage = String.format("游戏结束，你的得分是:%d\n请输入名字记录得分:", this.score); //

        String userName = JOptionPane.showInputDialog(
                GameTemplate.this,
                promptMessage,
                "Record Score",
                JOptionPane.PLAIN_MESSAGE
        );
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Player";
        }

        // 保存游戏记录
        saveGameRecord(userName);

        // 切换到排行榜界面
        RankingPanel rankingPanel = new RankingPanel(difficulty, cardLayout, cardPanel, rankDao);
        cardPanel.add(rankingPanel.getMainPanel(), "RANKING");
        cardLayout.show(cardPanel, "RANKING");
    }


    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration) {
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌人射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            List<BaseBullet> bullets = enemyAircraft.shoot();
            // 注册子弹观察者
            for (BaseBullet bullet : bullets) {
                if (bullet instanceof EnemyBullet) {
                    bombPublisher.addObserver((EnemyBullet) bullet);
                }
            }
            enemyBullets.addAll(bullets);
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void crashCheckAction() {
        // 敌机子弹与英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹与敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (soundOn) {
                        new MusicThread("src/videos/bullet_hit.wav", false).start();
                    }

                    if (enemyAircraft.notValid()) {
                        // 得分
                        if (enemyAircraft instanceof EliteEnemy) {
                            score += 20;
                            props.addAll(((EliteEnemy) enemyAircraft).dropProps());
                        } else if (enemyAircraft instanceof SuperEliteEnemy) {
                            score += 35;
                            props.addAll(((SuperEliteEnemy) enemyAircraft).dropProps());
                        } else if (enemyAircraft instanceof BossEnemy) {
                            score += 100;
                            bossExists = false;
                            props.addAll(((BossEnemy) enemyAircraft).dropProps());
                            // Boss死亡，切回BGM
                            if (soundOn) {
                                if (bossBgmThread != null) bossBgmThread.stopMusic();
                                bgmThread = new MusicThread("src/videos/bgm.wav", true);
                                bgmThread.start();
                            }
                        } else if (enemyAircraft instanceof MobEnemy) {
                            score += 10;
                        }
                    }
                }
            }
        }

        // 敌机与英雄机
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            if (heroAircraft.crash(enemyAircraft)) {
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
                enemyAircraft.vanish();
            }
        }

        // 道具与英雄机
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                if (soundOn) {
                    if (prop instanceof BombSupply) {
                        new MusicThread("src/videos/bomb_explosion.wav", false).start();
                    } else {
                        new MusicThread("src/videos/get_supply.wav", false).start();
                    }
                }

                // 观察者模式，检查是否是炸弹道具
                if (prop instanceof BombSupply) {
                    // 是炸弹的话通知所有观察者
                    int bombScore = bombPublisher.notifyObservers();
                    score += bombScore;
                    System.out.println("Bomb active! Score added: " + bombScore);
                } else {
                    // 不是炸弹，正常激活
                    prop.activate();
                }
                prop.vanish();
            }
        }
    }

    private void postProcessAction() {
        // 移除无效对象，并从发布者中取消注册
        enemyBullets.removeIf(bullet -> {
            if (bullet.notValid()) {
                if (bullet instanceof EnemyBullet) {
                    bombPublisher.removeObserver((EnemyBullet) bullet);
                }
                return true;
            }
            return false;
        });

        heroBullets.removeIf(AbstractFlyingObject::notValid);

        enemyAircrafts.removeIf(aircraft -> {
            if (aircraft.notValid()) {
                bombPublisher.removeObserver((EnemyAircraft) aircraft);
                return true;
            }
            return false;
        });

        props.removeIf(AbstractFlyingObject::notValid);
    }

    private void saveGameRecord(String userName) {
        long currentTime = System.currentTimeMillis();
        Rank rank = new Rank(userName, score, currentTime);
        rankDao.add(rank);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景
        g.drawImage(this.backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(this.backgroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 绘制飞行物
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, props);
        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        // 绘制得分和生命值
        paintScoreAndLife(g);
    }

    // 根据飞行物位置绘制图像，图像中心对齐位置
    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            if (image != null) {
                g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                        object.getLocationY() - image.getHeight() / 2, null);
            }
        }
    }

    // 绘制分数和生命值
    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }
}