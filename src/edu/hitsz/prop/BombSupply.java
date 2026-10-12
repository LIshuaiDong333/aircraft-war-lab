package edu.hitsz.prop;

//炸弹道具
public class BombSupply extends AbstractProp {

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate() {
        // 观察者模式，激活逻辑由GameTemplate中的发布者处理
    }
}