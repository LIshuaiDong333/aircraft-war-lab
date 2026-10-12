package edu.hitsz.factory;

import edu.hitsz.prop.BombSupply;
import edu.hitsz.prop.AbstractProp;

public class BombSupplyFactory implements PropFactory {
    @Override
    public AbstractProp createBaseProps(int locationX, int locationY, int speedX, int speedY) {
        return new BombSupply(locationX, locationY, speedX, speedY);
    }
}