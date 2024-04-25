package macaroni.model.element;

/**
 * Represents the pump in the pipe network.
 */
public class Pump extends ActiveElement {

    /**
     * The maximum number of pipes connecting to this pump.
     */
    private int ports;

    /**
     * The maximum amount of water the pump can store.
     */
    private int tankCapacity;
    /**
     * The current amount of stored water.
     */
    private int tankStoredWater = 0;
    /**
     * Whether the pump is broken.
     */
    private boolean broken = false;
    /**
     * The input pipe of the pump where water is being taken from.
     */
    private Pipe inputPipe = null;
    /**
     * The output pipe of the pump where water is being pumped to.
     */
    private Pipe outputPipe = null;

    /**
     * Creates a new Pump.
     *
     * @param portCount    the number of ports on the pump
     * @param tankCapacity the capacity of the tank of the pump
     */
    public Pump(int portCount, int tankCapacity) {
        ports = portCount;
        this.tankCapacity = tankCapacity;
    }

    /**
     * Creates a new Pump, with a default tank capacity of 5.
     *
     * @param portCount the number of ports on the pump
     */
    public Pump(int portCount) {
        this(portCount, 5);
    }

    /**
     * Creates a new Pump, with a default tank capacity of 5,
     * and a default port count of 4.
     */
    public Pump() {
        this(4, 5);
    }

    /**
     * Gets the amount of stored water in the pump.
     * @return the amount of stored water
     */
    public int getStoredWater() {
        return tankStoredWater;
    }

    /**
     * If the pump isn't broken,
     * it tries to pump water from the tank to the output pipe,
     * then takes water from the input pipe to the tank.
     */
    @Override
    public void tick() {
        if (broken) {
            return;
        }

        if (outputPipe != null) {
            tankStoredWater = outputPipe.addWater(tankStoredWater);
        }

        if (inputPipe != null) {
            tankStoredWater += inputPipe.removeWater(tankCapacity - tankStoredWater);
        }
    }

    /**
     * If the pump still has a free port and the pipe is not yet connected,
     * it tries to connect the given pipe to itself.
     *
     * @param pipe the pipe that needs to be connected
     * @return true if the connection was successful
     */
    @Override
    public boolean addPipe(Pipe pipe) {
        if (connectedPipes.size() >= ports) {
            return false;
        }

        return super.addPipe(pipe);
    }

    /**
     * Tries to remove the given pipe from itself.
     *
     * @param pipe the pipe that needs to be removed
     * @return true if the removal was successful
     */
    @Override
    public boolean removePipe(Pipe pipe) {
        if (!super.removePipe(pipe)) {
            return false;
        }

        if (outputPipe == pipe) {
            outputPipe = null;
        }
        if (inputPipe == pipe) {
            inputPipe = null;
        }

        return true;
    }

    /**
     * Repairs the pump.
     */
    public boolean repair() {
        if (broken) {
            broken = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Breaks the pump.
     */
    public boolean Break() {
        if (!broken) {
            broken = true;
            return true;
        } else {
            return false;
        }
    }

    // TODO document this function
    /**
     * Gets the pump's input pipe.
     * @return the pipe which is the pump's input
     */
    public Pipe getInputPipe() {
        return inputPipe;
    }

    // TODO document this function
    /**
     * Gets the pump's output pipe.
     * @return the pipe which is the pump's output
     */
    public Pipe getOutputPipe() {
        return outputPipe;
    }

    /**
     * Sets the given pipe as its input if the pipe is connected and not set to output.
     *
     * @param pipe the pipe that needs to be the input
     */
    public boolean setInputPipe(Pipe pipe) {
        if (connectedPipes.contains(pipe) && outputPipe != pipe && inputPipe != pipe) {
            inputPipe = pipe;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the given pipe as its output if the pipe is connected and not set to input.
     *
     * @param pipe the pipe that needs to be the output
     */
    public boolean setOutputPipe(Pipe pipe) {
        if (connectedPipes.contains(pipe) && inputPipe != pipe && outputPipe != pipe) {
            outputPipe = pipe;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return whether the pump is broken or not
     */
    public boolean isBroken() {
        return broken;
    }
}
