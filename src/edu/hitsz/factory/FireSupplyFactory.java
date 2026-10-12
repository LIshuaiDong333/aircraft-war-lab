package edu.hitsz.factory;

import edu.hitsz.prop.FireSupply;
import edu.hitsz.prop.AbstractProp;

public class FireSupplyFactory implements PropFactory {
    @Override
    public AbstractProp createBaseProps(int locationX, int locationY, int speedX, int speedY) {
        return new FireSupply(locationX, locationY, speedX, speedY);
    }
}
