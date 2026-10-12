package edu.hitsz.aircraft;
import edu.hitsz.factory.BombSupplyFactory;
import edu.hitsz.factory.FireSupplyFactory;
import edu.hitsz.factory.HpSupplyFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.DirectShoot;

import java.util.ArrayList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft {

    private final PropFactory hpSupplyFactory = new HpSupplyFactory();
    private final PropFactory fireSupplyFactory = new FireSupplyFactory();
    private final PropFactory bombSupplyFactory = new BombSupplyFactory();
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.power = 10;
        this.direction = 1;
        this.shootStrategy = new DirectShoot(1);
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
            props.add(hpSupplyFactory.createBaseProps(getLocationX(), getLocationY(), 0, 5));
        } else if (dropChance < 0.4) {
            props.add(fireSupplyFactory.createBaseProps(getLocationX(), getLocationY(), 0, 5));
        } else if (dropChance < 0.5) {
            props.add(bombSupplyFactory.createBaseProps(getLocationX(), getLocationY(), 0, 5));
        }
        return props;
    }

    /**
     * 响应炸弹：被摧毁
     * @return 20分
     */
    @Override
    public int update() {
        vanish();
        return 20; // 精英敌机20分
    }
}