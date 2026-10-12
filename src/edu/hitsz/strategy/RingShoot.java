package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class RingShoot implements ShootStrategy {
    private final int shootNum;
    private static final int RADIUS = 10;

    public RingShoot(int shootNum) {
        if (shootNum < 10) {
            throw new IllegalArgumentException("环射子弹数量不能少于10");
        }
        this.shootNum = shootNum;
    }

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int power = aircraft.getPower();
        int direction = aircraft.getDirection();

        double angleIncrement = 2 * Math.PI / shootNum;

        for (int i = 0; i < shootNum; i++) {
            double angle = i * angleIncrement;

            int bulletX = (int) (locationX + RADIUS * Math.cos(angle));
            int bulletY = (int) (locationY + RADIUS * Math.sin(angle));

            int bulletSpeedX = (int) (12 * Math.cos(angle));
            int bulletSpeedY = (int) (12 * Math.sin(angle));

            BaseBullet bullet = (direction < 0) ?
                    new HeroBullet(bulletX, bulletY, bulletSpeedX, bulletSpeedY, power) :
                    new EnemyBullet(bulletX, bulletY, bulletSpeedX, bulletSpeedY, power);

            bullets.add(bullet);
        }

        return bullets;
    }
}