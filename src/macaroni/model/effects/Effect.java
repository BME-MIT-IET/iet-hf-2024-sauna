package macaroni.model.effects;

import macaroni.model.character.Character;
import macaroni.model.element.Pipe;

/**
 * Class for implementing effects on {@link Pipe} instances.
 */
public abstract class Effect {

    /**
     * The pipe this effect is applied on.
     */
    protected final Pipe pipe;

    /**
     * Creates a new Effect.
     *
     * @param pipe the pipe this effect is applied on
     */
    public Effect(Pipe pipe) {
        this.pipe = pipe;
    }

    /**
     * Handles the character's entrance to the pipe.
     *
     * @param character the character that wants to enter the pipe
     * @return true if the character could successfully enter the pipe
     */
    abstract public boolean enter(Character character);

    /**
     * Handles the event when a character wants to leave a pipe.
     *
     * @return true if the character could successfully leave the pipe
     */
    abstract public boolean leave();

    /**
     * Updates the state of the effect.
     */
    abstract public void tick();
}
