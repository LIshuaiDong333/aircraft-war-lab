package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class DirectShoot implements ShootStrategy {
    private final int shootNum;

    public DirectShoot(int shootNum) {
        this.shootNum = shootNum;
    }

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        // 从飞行器对象中获取所需参数
        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int direction = aircraft.getDirection();
        int power = aircraft.getPower();

        int bulletY = locationY + direction * 2;
        int baseSpeedY = direction * 13;

        for (int i = 0; i < shootNum; i++) {
            int bulletX = locationX + (i * 2 - shootNum + 1) * 10;

            BaseBullet bullet = (direction < 0) ?
                    new HeroBullet(bulletX, bulletY, 0, baseSpeedY, power) :
                    new EnemyBullet(bulletX, bulletY, 0, baseSpeedY, power);

            bullets.add(bullet);
        }

        return bullets;
    }
}
