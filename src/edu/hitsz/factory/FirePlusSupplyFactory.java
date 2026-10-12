package edu.hitsz.factory;

import edu.hitsz.prop.FirePlusProp;
import edu.hitsz.prop.AbstractProp;

public class FirePlusSupplyFactory implements PropFactory {
    @Override
    public AbstractProp createBaseProps(int locationX, int locationY, int speedX, int speedY) {
        return new FirePlusProp(locationX, locationY, speedX, speedY);
    }
}