package edu.hitsz.factory;
import edu.hitsz.prop.AbstractProp;

public interface PropFactory {
    AbstractProp createBaseProps(int locationX, int locationY, int speedX, int speedY);
}