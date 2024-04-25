package macaroni.model.effects;

import macaroni.model.character.Character;
import macaroni.model.element.Pipe;

/**
 * Class that represents a permissive {@link Effect} with no side effects
 */
public final class NoEffect extends Effect {

    /**
     * Creates a new NoEffect.
     *
     * @param pipe the pipe this effect is applied on
     */
    public NoEffect(Pipe pipe) {
        super(pipe);
    }

    /**
     * Handles the character's entrance to the pipe.
     *
     * @param character the character that wants to enter the pipe
     * @return true if the character could leave its previous location
     */
    @Override
    public boolean enter(Character character) {
        return character.leave(pipe);
    }

    /**
     * Signals that the character can leave gracefully.
     *
     * @return true
     */
    @Override
    public boolean leave() {
        return true;
    }

    /**
     * Does nothing - Empty function.
     */
    @Override
    public void tick() {}
}
