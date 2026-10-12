package edu.hitsz.aircraft;

import edu.hitsz.factory.BombSupplyFactory;
import edu.hitsz.factory.FireSupplyFactory;
import edu.hitsz.factory.HpSupplyFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ScatterShoot;

import java.util.ArrayList;
import java.util.List;

public class SuperEliteEnemy extends EnemyAircraft {

    private final PropFactory hpSupplyFactory = new HpSupplyFactory();
    private final PropFactory fireSupplyFactory = new FireSupplyFactory();
    private final PropFactory bombSupplyFactory = new BombSupplyFactory();
    public SuperEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.power = 15;
        this.direction = 1;
        this.shootStrategy = new ScatterShoot(3);
    }

    @Override
    public void forward() {
        super.forward();
        //判断是否飞出边界
        if (locationY >= edu.hitsz.application.Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> props = new ArrayList<>();
        double dropChance = Math.random();

        if (dropChance < 0.15) {
            props.add(hpSupplyFactory.createBaseProps(getLocationX(), getLocationY(), 0, 6));
        }
        else if (dropChance < 0.50) {
            props.add(fireSupplyFactory.createBaseProps(getLocationX() + 20, getLocationY(), 0, 6));
        }
        else if (dropChance < 0.70) {
            props.add(bombSupplyFactory.createBaseProps(getLocationX() - 20, getLocationY(), 0, 6));
        }

        return props;
    }

    @Override
    public int update() {
        decreaseHp(100); // 扣除100点血量
        if (notValid()) {
            return 40; // 如果血量扣除后死亡，则加分
        }
        return 0;
    }
}