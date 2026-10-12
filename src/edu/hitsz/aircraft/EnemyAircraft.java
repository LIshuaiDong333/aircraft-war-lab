package edu.hitsz.aircraft;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.observer.Observer;
import java.util.List;

public abstract class EnemyAircraft extends AbstractAircraft implements Observer {
    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public abstract List<AbstractProp> dropProps();

    @Override
    public int update() {
        // 默认不受影响
        return 0;
    }
}