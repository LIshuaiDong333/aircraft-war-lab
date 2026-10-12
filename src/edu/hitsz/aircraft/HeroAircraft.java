package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.strategy.ShootStrategy;

public class HeroAircraft extends AbstractAircraft {

    private static volatile HeroAircraft instance;
    /**
     * 英雄机的基础射击策略
     */
    private final ShootStrategy baseStrategy;
    /**
     * 用于恢复射击策略的线程
     */
    private Thread strategyRevertThread = null;
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.maxHp = hp;
        this.direction = -1; //向上发射
        this.power = 30;
        // 存储基础策略，并将当前策略设为基础策略
        this.baseStrategy = new DirectShoot(2);
        this.shootStrategy = this.baseStrategy;
    }

    public static HeroAircraft getInstance() {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(

                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 1000);
                }
            }
        }
        return instance;
    }

    // 获取基础射击策略
    public ShootStrategy getBaseStrategy() {
        return this.baseStrategy;
    }

    public synchronized void setTimedShootStrategy(ShootStrategy newStrategy, long durationMs) {
        // 中断上一个道具的恢复线程
        if (strategyRevertThread != null && strategyRevertThread.isAlive()) {
            strategyRevertThread.interrupt();
        }

        // 设置新的射击策略
        setShootStrategy(newStrategy);
        // 创建并启动一个新的恢复线程
        strategyRevertThread = new Thread(() -> {
            try {
                Thread.sleep(durationMs);
                setShootStrategy(baseStrategy);
            } catch (InterruptedException e) {
                // 如果在sleep时吃了新道具，此线程提前终止
                System.out.println("切换到新道具");
            }
        });
        strategyRevertThread.start();
    }


    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

}