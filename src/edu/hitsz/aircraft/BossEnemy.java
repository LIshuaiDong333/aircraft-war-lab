package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.RingShoot;

import java.util.ArrayList;
import java.util.List;

public class BossEnemy extends EnemyAircraft {

    private static final int SHOOT_NUM = 20;
    private static final int BULLET_POWER = 25;

    private final int speedX;
    private int directionX = 1;
    private long lastDirectionChangeTime = 0;
    private static final int DIRECTION_CHANGE_INTERVAL = 2000;

    private final PropFactory hpSupplyFactory = new HpSupplyFactory();
    private final PropFactory fireSupplyFactory = new FireSupplyFactory();
    private final PropFactory bombSupplyFactory = new BombSupplyFactory();
    private final PropFactory firePlusSupplyFactory = new FirePlusSupplyFactory();


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = speedX;
        this.power = BULLET_POWER;
        this.direction = 1;
        this.shootStrategy = new RingShoot(SHOOT_NUM); //环射策略
    }

    @Override
    public void forward() {
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - lastDirectionChangeTime > DIRECTION_CHANGE_INTERVAL) {
//            directionX *= -1;
//            lastDirectionChangeTime = currentTime;
//        }
//        locationX += speedX * directionX;
//        if (locationX <= 0) {
//            locationX = 0;
//            directionX = 1;
//        } else if (locationX >= Main.WINDOW_WIDTH) {
//            locationX = Main.WINDOW_WIDTH;
//            directionX = -1;
//        }
        locationX += speedX * directionX;
        // 获取Boss图片宽度
        //int bossWidth = ImageManager.BOSS_ENEMY_IMAGE.getWidth();
        if (locationX <= 0) {
            locationX = 0;
            directionX = 1; // 向右反向
        }
        else if (locationX >= Main.WINDOW_WIDTH) {
            locationX = Main.WINDOW_WIDTH;
            directionX = -1; // 向左反向
        }
//        else if (locationX + bossWidth >= Main.WINDOW_WIDTH) {
//            locationX = Main.WINDOW_WIDTH - bossWidth;
//            directionX = -1; // 向左反向
//        }
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> props = new ArrayList<>();
        //随机掉落1-3个道具
        int propCount = (int) (Math.random() * 3) + 1;
        for (int i = 0; i < propCount; i++) {
            double propType = Math.random();
            int x = getLocationX() + (int)(Math.random()*40 - 20);
            int y = getLocationY() + (int)(Math.random()*40 - 20);
            if (propType < 0.15) {
                props.add(hpSupplyFactory.createBaseProps(x, y, 0, 5));
            } else if (propType < 0.5) {
                props.add(fireSupplyFactory.createBaseProps(x, y, 0, 5));
            } else if (propType < 0.7) {
                props.add(bombSupplyFactory.createBaseProps(x, y, 0, 5));
            } else {
                props.add(firePlusSupplyFactory.createBaseProps(x, y, 0, 5));
            }
        }
        return props;
    }
}