package macaroni.model.element;

/**
 * A spring that pushes water to connected pipes.
 */
public class Spring extends ActiveElement {

    /**
     * The amount of water dispensed to each connected pipe per tick.
     */
    private int waterDispensedPerTick;

    /**
     * Creates a new Spring.
     *
     * @param waterDispensedPerTick the amount of water dispensed to each connected pipe per tick
     */
    public Spring(int waterDispensedPerTick) {
        this.waterDispensedPerTick = waterDispensedPerTick;
    }

    /**
     * Creates a new Spring with a default water dispensed per tick value of 4.
     */
    public Spring() {
        this(4);
    }

    /**
     * Steps the waterflow by pushing water to connected pipes.
     */
    @Override
    public void tick() {
        for (Pipe pipe : connectedPipes) {
            pipe.addWater(waterDispensedPerTick);
        }
    }
}
