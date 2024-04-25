package macaroni.model.element;

import java.util.ArrayList;
import java.util.List;

/**
 * An active element that connects pipes and makes water flow in the pipe network.
 */
public abstract class ActiveElement extends Element {

    /**
     * List of connected pipes.
     */
    protected List<Pipe> connectedPipes = new ArrayList<>();

    /**
     * Steps the waterflow through the ActiveElement.
     */
    @Override
    public abstract void tick();

    /**
     * Connects a pipe to this ActiveElement.
     *
     * @param pipe the pipe to be connected
     * @return false if the pipe is already connected or the pipe refuses connection, true if it succeeds
     */
    public boolean addPipe(Pipe pipe) {
        if (connectedPipes.contains(pipe) || !pipe.addEndpoint(this)) {
            return false;
        }

        connectedPipes.add(pipe);
        neighbours.add(pipe);

        return true;
    }

    /**
     * Removes a connected pipe.
     *
     * @param pipe the pipe to be removed
     * @return false if the pipe is not connected or the pipe refuses removal, true if it succeeds
     */
    public boolean removePipe(Pipe pipe) {
        if (!connectedPipes.contains(pipe) || !pipe.removeEndpoint(this)) {
            return false;
        }

        connectedPipes.remove(pipe);
        neighbours.remove(pipe);

        return true;
    }
}
