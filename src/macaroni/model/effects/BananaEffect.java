package macaroni.model.effects;

import macaroni.model.character.Character;
import macaroni.model.element.Pipe;
import macaroni.utils.Random;

/**
 * Class for implementing a slippery effect on the pipe.
 * The character is sent to a random neighbouring element of the pipe on entering.
 */
public final class BananaEffect extends Effect {

    /**
     * The number of ticks before this expires
     */
    private int countdown;

    /**
     * Creates a new BananaEffect.
     *
     * @param pipe the pipe this effect is applied on
     */
    public BananaEffect(Pipe pipe) {
        super(pipe);
        this.countdown = Random.generateRandomInt(8, 20);
    }

    /**
     * Throws the character to a random endpoint of the pipe.
     *
     * @param character the character that wants to enter the pipe
     * @return false
     */
    @Override
    public boolean enter(Character character) {
        if (character.leave(pipe)) {
            character.moveTo(pipe.getRandomEndpoint());
        }
        return false;
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
     * Decreases the amount of time left for the effect.
     * Removes this effect from the pipe when the counter reaches zero.
     */
    @Override
    public void tick() {
        countdown--;
        if (countdown == 0) {
            pipe.setEffect(new NoEffect(pipe));
            Random.removeDeterministicSlideBack(pipe);
        }
    }

    /**
     * @return the remaining ticks of the effect
     */
    public int getCountdown() {
        return countdown;
    }
}
