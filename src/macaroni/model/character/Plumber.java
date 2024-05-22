package macaroni.model.character;

import macaroni.model.element.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Type of Character that can pick up, place and repair pumps and pipes.
 */
public class Plumber extends Character {

    /**
     * The pipe held by the plumber in between detach and attach actions,
     * null if the plumber doesn't hold a pipe.
     */
    private Pipe heldPipe;

    /**
     * The pumps that are picked up by the plumber from cisterns and can be placed on pipes.
     */
    private List<Pump> heldPumps = new ArrayList<>();

    /**
     * Creates a new Plumber.
     *
     * @param location the initial location of the plumber
     */
    public Plumber(Element location) {
        super(location);
    }

    /**
     * Repairs a chosen pipe.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given pipe.
     *
     * @param pipe the pipe to be repaired
     * @return true if successful
     */
    public boolean repair(Pipe pipe) {
        if (pipe == location) {
            return pipe.patch();
        } else {
            return false;
        }
    }

    /**
     * Repairs a chosen pump.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given pump.
     *
     * @param pump the pump to be repaired
     * @return true if successful
     */
    public boolean repair(Pump pump) {
        if (pump == location) {
            return pump.repair();
        } else {
            return false;
        }
    }

    /**
     * Attaches a pipe to the element they are standing on.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given target and
     * has a pipe in their hand.
     *
     * @param target where the pipe will be attached to
     * @return true if successful
     */
    public boolean attachPipe(ActiveElement target) {
        if (location == target && heldPipe != null && target.addPipe(heldPipe)) {
            heldPipe = null;
            return true;
        }

        return false;
    }

    /**
     * Detaches a pipe from the element they are standing on.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given target and
     * doesn't already have a pipe in their hand.
     *
     * @param target where the selected pipe will be removed from
     * @param pipe   the pipe that will be detached.
     * @return true if successful
     */
    public boolean detachPipe(ActiveElement target, Pipe pipe) {
        if (location == target && heldPipe == null && target.removePipe(pipe)) {
            heldPipe = pipe;
            return true;
        }

        return false;
    }

    /**
     * Picks up a new pump into their hand.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given cistern.
     *
     * @param cistern where the plumber is going to get their hands on a new pump
     * @return true if successful
     */
    public boolean pickUpPump(Cistern cistern) {
        if (cistern == location) {
            heldPumps.add(cistern.acquirePump());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Places a pump on a pipe, splitting it into two, then connecting
     * the new pipes to the pump, afterward stepping onto the pump.
     * <p></p>
     * This action will only be successful if the plumber is standing on the given pipe,
     * and has a pump in their hand.
     *
     * @param pipe where the plumber is going to place the pump
     * @return true if successful
     */
    public boolean placePump(Pipe pipe) {
        if (location == pipe && !heldPumps.isEmpty() && pipe.leave()) {
            Pipe newPipe = pipe.split();
            Pump pump = getHeldPump();
            heldPumps.remove(pump);
            pump.addPipe(pipe);
            pump.addPipe(newPipe);
            pump.enter(this, location);
            return true;
        }

        return false;
    }

    /**
     * @return pipe held by the plumber
     */
    public Pipe getHeldPipe() {
        return heldPipe;
    }

    /**
     * @return the first pump (that would be placed by {@link Plumber#placePump(Pipe)})
     * in the plumber's hand, or null if they hold no pumps
     */
    public Pump getHeldPump() {
        if (heldPumps.isEmpty()) {
            return null;
        } else {
            return heldPumps.get(0);
        }
    }

    /**
     * @return how many pumps does this plumber hold in their hand
     */
    public int getHeldPumpCount() {
        return heldPumps.size();
    }
}
