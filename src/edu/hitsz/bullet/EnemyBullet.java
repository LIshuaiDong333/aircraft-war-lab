package edu.hitsz.bullet;

import edu.hitsz.observer.Observer;

public class EnemyBullet extends BaseBullet implements Observer {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public int update() {
        vanish();
        return 0;
    }
}