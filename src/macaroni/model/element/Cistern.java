package macaroni.model.element;

import macaroni.utils.ModelObjectFactory;
import macaroni.model.misc.WaterCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * A cistern that collects water from connected pipes.
 */
public class Cistern extends ActiveElement {

    /**
     * List of new pipes.
     */
    private List<Pipe> newPipes = new ArrayList<>();

    /**
     * Water collector object that stores the collected water.
     */
    private WaterCollector collector;

    /**
     * Creates a new cistern.
     *
     * @param waterCollector the collector that stores the collected water
     */
    public Cistern(WaterCollector waterCollector) {
        collector = waterCollector;
    }

    /**
     * Steps the waterflow by sucking the water out from connected pipes.
     */
    @Override
    public void tick() {
        for (Pipe pipe : connectedPipes) {
            collector.storeAmount(pipe.removeAllWater());
        }
    }

    /**
     * Removes a connected pipe. New pipes stop being new pipes
     * but aren't removed from lists of connected elements.
     *
     * @param pipe the pipe to be removed
     * @return true if the pipe was new, otherwise it's the
     * same as {@link ActiveElement#removePipe(Pipe)}
     */
    @Override
    public boolean removePipe(Pipe pipe) {
        if (newPipes.contains(pipe)) {
            newPipes.remove(pipe);
            return true;
        }
        return super.removePipe(pipe);
    }

    /**
     * Spawns a new pipe and connects it to the cistern.
     */
    public void spawnPipe() {
        Pipe pipe = ModelObjectFactory.cisternCreatePipe();
        addPipe(pipe);
        newPipes.add(pipe);
    }

    /**
     * Spawns and returns a new pump.
     *
     * @return the new pump
     */
    public Pump acquirePump() {
        return ModelObjectFactory.cisternCreatePump();
    }
}
