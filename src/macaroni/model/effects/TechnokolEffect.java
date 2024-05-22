package macaroni.model.effects;

import macaroni.model.character.Character;
import macaroni.model.element.Pipe;
import macaroni.utils.Random;

/**
 * Class for implementing a sticky effect on the pipe
 */
public final class TechnokolEffect extends Effect {

    /**
     * The amount of ticks before this effect expires.
     */
    private int countdown;

    /**
     * Whether this effect is activated (armed). This will
     * be set to true on the first step off from the pipe, so that
     * the character applying the technokol can leave the pipe.
     */
    private boolean activated = false;

    /**
     * Creates a new TechnokolEffect.
     *
     * @param pipe the pipe this effect is applied on
     */
    public TechnokolEffect(Pipe pipe) {
        super(pipe);
        this.countdown = Random.generateRandomInt(8, 16);
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
     * Handles the event when a character wants to leave a pipe.
     *
     * @return false if the character got stuck on the pipe
     */
    @Override
    public boolean leave() {
        if (activated) {
            return false;
        } else {
            activated = true;
            return true;
        }
    }

    /**
     * Decreases the amount of time left for the effect.
     * Removes this effect from the pipe when the counter reaches zero.
     */
    @Override
    public void tick() {
        countdown--;
        if (countdown == 0) {
            pipe.setEffect(new NoEffect(pipe));
        }
    }

    /**
     * @return whether the technokol effect is activated or not
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * @return the remaining ticks of the effect
     */
    public int getCountdown() {
        return countdown;
    }
}
