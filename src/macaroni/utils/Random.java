package macaroni.utils;

import macaroni.model.element.ActiveElement;
import macaroni.model.element.Element;
import macaroni.model.element.Pipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static class for generating random and deterministic values.
 * Part of the Prototype phase.
 */
public final class Random {
    private static final java.util.Random generator = new java.util.Random();

    /**
     * Whether the next randomly generated number should be
     * random (true) or deterministic (false)
     */
    private static boolean nonDeterministic = true;

    /**
     * The deterministic value that will be returned when
     * a randomly generated number is needed but random is set to false.
     */
    private static int deterministicValue = -1;

    /**
     * Stores the deterministic slideBack value for each pipe that has a BananaEffect.
     */
    private static final Map<Pipe, Boolean> deterministicSlideBacks = new HashMap<>();

    /**
     * Tracks the location of characters before they enter a pipe. Used for determining
     * which endpoint of the pipe to push back characters when they enter a pipe that has
     * BananaEffect and a deterministicSlideBack value is set.
     */
    private static final Map<Pipe, Element> characterLocationsBeforeEnteringPipe = new HashMap<>();

    private Random() {}

    /**
     * Sets a deterministic value that will be returned instead of a random value
     * on the next call to {@link #generateRandomInt(int, int)}. Consequent calls
     * to {@link #generateRandomInt(int, int)} will still return a random value.
     *
     * @param value the deterministic value
     */
    public static void setDeterministicValue(int value) {
        nonDeterministic = false;
        deterministicValue = value;
    }

    /**
     * Generates a random integer between the specified start (inclusive) and stop (exclusive).
     * <p></p>
     * If a deterministic value is set using {@link #setDeterministicValue(int)}, then the first
     * call to this will return that deterministic value instead. Consequent calls
     * to this will still return a random value. (it "consumes" the set deterministic value)
     *
     * @param start the lower bound of the randomly generated number (inclusive)
     * @param stop the upper bound of the randomly generated number (exclusive)
     * @return a randomly generated integer, or a deterministic value if it is set
     * @throws IllegalArgumentException
     */
    public static int generateRandomInt(int start, int stop) throws IllegalArgumentException {
        if (nonDeterministic) {
            return generator.nextInt(start, stop);
        } else {
            // consume deterministic value
            nonDeterministic = true;
            return deterministicValue;
        }
    }

    /**
     * Sets the deterministic slideBack for the given pipe to the given value.
     * <p></p>
     * This causes the BananaEffect on that pipe to always slide the players back
     * to the same location they stepped onto the pipe from (if slideBack is true),
     * or to the opposite location (if slideBack is false), until the BananaEffect
     * expires.
     *
     * @param pipe the affected pipe
     * @param deterministicSlideBack the value of the deterministic slideBack to set,
     *                               true if players should slide back to the location they
     *                               stepped onto the pipe from, or false if they should slide
     *                               back to the opposite location, while the BananaEffect is
     *                               active.
     */
    public static void setDeterministicSlideBack(Pipe pipe, boolean deterministicSlideBack) {
        deterministicSlideBacks.put(pipe, deterministicSlideBack);
    }

    /**
     * Removes the deterministic slideBack previously set for the given pipe.
     *
     * @param pipe the affected pipe
     */
    public static void removeDeterministicSlideBack(Pipe pipe) {
        deterministicSlideBacks.remove(pipe);
    }

    /**
     * Stores the character's location before entering the pipe.
     *
     * @param pipe the pipe the character is trying to enter
     * @param location the current location of the character,
     *                 where they are trying to enter from
     */
    public static void setCharacterLocationBeforeEnteringPipe(Pipe pipe, Element location) {
        characterLocationsBeforeEnteringPipe.put(pipe, location);
    }

    /**
     * Generates a random endpoint index (0 or 1) that determines which
     * endpoint of the pipe the character will slide onto, when the pipe has
     * a BananaEffect.
     *
     * @param pipe the pipe the character is stepping on
     * @param endpoints the endpoints of the pipe
     * @return the randomly generated endpoint index, or the deterministic index
     * if the deterministic slideBack has been set for this pipe.
     */
    public static int generateRandomEndpointIndex(Pipe pipe, List<ActiveElement> endpoints) {
        if (!deterministicSlideBacks.containsKey(pipe)) {
            return generator.nextInt(0, 2);
        } else {
            int indexOfCharLoc = endpoints.indexOf(characterLocationsBeforeEnteringPipe.get(pipe));
            characterLocationsBeforeEnteringPipe.remove(pipe);
            if (Boolean.TRUE.equals(deterministicSlideBacks.get(pipe))) {
                return indexOfCharLoc;
            } else {
                return indexOfCharLoc == 0 ? 1 : 0;
            }
        }
    }
}
