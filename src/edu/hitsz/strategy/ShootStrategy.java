package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

public interface ShootStrategy {
    //直接接收飞行器对象
    List<BaseBullet> doShoot(AbstractAircraft aircraft);
}