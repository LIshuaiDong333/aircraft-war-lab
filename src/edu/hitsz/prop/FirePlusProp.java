package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShoot;

public class FirePlusProp extends AbstractProp {
    public FirePlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate() {
        HeroAircraft.getInstance().setTimedShootStrategy(
                new RingShoot(20), // 20发环射
                5000                  // 5秒
        );
    }
}