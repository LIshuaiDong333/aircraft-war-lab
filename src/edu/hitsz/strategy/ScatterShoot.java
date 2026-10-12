package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class ScatterShoot implements ShootStrategy {
    private final int shootNum;
    private static final int SPEED_STEP = 2;

    public ScatterShoot(int shootNum) {
        if (shootNum < 1) {
            throw new IllegalArgumentException("散射子弹数量不能少于1");
        }
        this.shootNum = shootNum;
    }

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        //从飞行器对象中获取参数
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int direction = aircraft.getDirection();
        int speedY = aircraft.getSpeedY();
        int power = aircraft.getPower();

        int bulletY = y + direction * 2;
        int baseSpeedY = direction > 0 ? speedY + 5 : speedY - 15;

        //计算每颗子弹的水平速度,对称分布
        for (int i = 0; i < shootNum; i++) {
            double offset = i - (shootNum - 1) / 2.0;
            int bulletSpeedX = (int) (offset * SPEED_STEP * 2);

            BaseBullet bullet = direction < 0 ?
                    new HeroBullet(x, bulletY, bulletSpeedX, baseSpeedY, power) :
                    new EnemyBullet(x, bulletY, bulletSpeedX, baseSpeedY, power);
            bullets.add(bullet);
        }
        return bullets;
    }
}