package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.DirectShoot;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractAircraft extends AbstractFlyingObject {
    protected int maxHp;
    protected int hp;
    protected ShootStrategy shootStrategy;
    protected int power = 10;
    protected int direction = 1;
    private final Lock strategyLock = new ReentrantLock();

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootStrategy = new DirectShoot(1);
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    public void increaseHp(int increase) {
        hp = Math.min(hp + increase, maxHp);
    }

    public int getPower() {
        return power;
    }

    public int getDirection() {
        return direction;
    }

    public void setShootStrategy(ShootStrategy strategy) {
        strategyLock.lock();
        try {
            this.shootStrategy = strategy;
        } finally {
            strategyLock.unlock();
        }
    }


    public List<BaseBullet> shoot() {
        strategyLock.lock();
        try {
            return shootStrategy.doShoot(this);
        } finally {
            strategyLock.unlock();
        }
    }
}
