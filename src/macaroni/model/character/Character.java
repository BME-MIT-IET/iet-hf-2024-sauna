package macaroni.model.character;

import macaroni.model.effects.TechnokolEffect;
import macaroni.model.element.Element;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;

/**
 * Character class that moves on elements. It can alter the inputs and outputs of pumps.
 * Also, it can pierce and apply technokol on pipes.
 */
public abstract class Character {

    /**
     * The element on which the character is standing.
     */
    protected Element location;

    /**
     * Creates a new Character.
     *
     * @param location the initial location of the character
     */
    public Character(Element location) {
        this.location = location;
    }

    /**
     * The character tries to move to the given destination.
     *
     * @param destination the element where the character is trying to move to
     * @return true if successful
     */
    public boolean moveTo(Element destination) {
        return destination.enter(this, location);
    }

    /**
     * Tells the character to leave its place
     *
     * @param to the destination where the character has to leave
     * @return true if the character left to {@code to}
     */
    public boolean leave(Element to) {
        if (location.leave()) {
            location = to;
            return true;
        } else {
            return false;
        }
    }

    /**
     * The character sets the input pipe of a given pump.
     * <p></p>
     * This action will only be successful if the character is standing on the given pump.
     *
     * @param pump the pump whose input pipe is to be altered
     * @param pipe the potential new input pipe of the pump
     * @return true if successful
     */
    public boolean setInputPipe(Pump pump, Pipe pipe) {
        if (pump == location) {
            return pump.setInputPipe(pipe);
        } else {
            return false;
        }
    }

    /**
     * The character sets the output pipe of a given pump.
     * <p></p>
     * This action will only be successful if the character is standing on the given pump.
     *
     * @param pump the pump whose output pipe is to be altered
     * @param pipe the potential new output pipe of the pump
     * @return true if successful
     */
    public boolean setOutputPipe(Pump pump, Pipe pipe) {
        if (pump == location) {
            return pump.setOutputPipe(pipe);
        } else {
            return false;
        }
    }

    /**
     * Pierces the pipe.
     * <p></p>
     * This action will only be successful if the character is standing on the given pipe.
     *
     * @param pipe the pipe that should be pierced
     * @return true if successful
     */
    public boolean pierce(Pipe pipe) {
        if (pipe == location) {
            return pipe.pierce();
        } else {
            return false;
        }
    }

    /**
     * Applies a sticky "Technokol" effect on the pipe.
     * <p></p>
     * This action will only be successful if the character is standing on the given pipe.
     *
     * @param pipe the given pipe to be made sticky
     * @return true if successful
     */
    public boolean applyTechnokol(Pipe pipe) {
        if (pipe == location) {
            pipe.setEffect(new TechnokolEffect(pipe));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the element on which the character is standing.
     *
     * @return the element on which the character is standing
     */
    public Element getLocation() {
        // TODO document function
        return location;
    }
}
