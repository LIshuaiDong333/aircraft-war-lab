package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

//血包
public class HpSupply extends AbstractProp {

    private int hpIncrease = 66;  //恢复66滴血

    public HpSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate() {
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.increaseHp(hpIncrease);
    }
}