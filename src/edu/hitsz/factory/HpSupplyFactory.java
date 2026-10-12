package edu.hitsz.factory;

import edu.hitsz.prop.HpSupply;
import edu.hitsz.prop.AbstractProp;

public class HpSupplyFactory implements PropFactory {
    @Override
    public AbstractProp createBaseProps(int locationX, int locationY, int speedX, int speedY) {
        return new HpSupply(locationX, locationY, speedX, speedY);
    }
}