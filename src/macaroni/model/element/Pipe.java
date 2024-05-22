package macaroni.model.element;

import macaroni.utils.ModelObjectFactory;
import macaroni.utils.Random;
import macaroni.model.character.Character;
import macaroni.model.misc.WaterCollector;
import macaroni.model.effects.Effect;
import macaroni.model.effects.NoEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the pipe in the pipe network. ActiveElements can store and take
 * water from them.
 */
public class Pipe extends Element {

    /**
     * The effect that happens when moving on and off of the pipe.
     */
    private Effect effect = new NoEffect(this);

    /**
     * The endpoints of the pipe.
     */
    private List<ActiveElement> endpoints = new ArrayList<>();

    /**
     * Whether there is a character standing on the pipe.
     */
    private boolean occupied = false;

    /**
     * Whether the pipe is pierced.
     */
    private boolean pierced = false;

    /**
     * The number of ticks before the pipe can be pierced again after getting patched.
     */
    private int pierceCooldown = 0;

    /**
     * The ground under the pipe, where the pipe stores the water that leaks from it.
     */
    private WaterCollector ground;

    /**
     * The maximum amount of water the pipe can hold.
     */
    private int capacity;

    /**
     * The current amount of water in the pipe.
     */
    private int storedWater = 0;


    /**
     * Creates a new Pipe.
     *
     * @param ground   the WaterCollector where the pipe should store the water that leaks from it
     * @param capacity the maximum amount of water that the pipe can hold
     */
    public Pipe(WaterCollector ground, int capacity) {
        this.ground = ground;
        this.capacity = capacity;
    }

    /**
     * Creates a new Pipe, with a default capacity of 10.
     *
     * @param ground the WaterCollector where the pipe should store the water that leaks from it
     */
    public Pipe(WaterCollector ground) {
        this(ground, 10);
    }

    /**
     * @return a random (not null) endpoint of the pipe.
     */
    public ActiveElement getRandomEndpoint() {
        if (!endpoints.isEmpty()) {
            return endpoints.get(Random.generateRandomEndpointIndex(this, endpoints));
        }
        return null;
    }

    /**
     * @param index the endpoint to get (0 or 1)
     * @return the endpoint, or null if the pipe doesn't have an endpoint with the specified index set
     */
    public ActiveElement getEndpoint(int index) {
        if (endpoints.size() > index) {
            return endpoints.get(index);
        } else {
            return null;
        }
    }

    /**
     * Guides the characters movement as it tries to enter the pipe.
     *
     * @param character the character that wants to enter the element
     * @param from      the location where the character is coming from
     */
    @Override
    public boolean enter(Character character, Element from) {
        Random.setCharacterLocationBeforeEnteringPipe(this, from);
        if (neighbours.contains(from) && !occupied && endpoints.size() == 2 && effect.enter(character)) {
            occupied = true;
            return true;
        }

        return false;
    }

    /**
     * @return true if the character could leave the pipe
     */
    @Override
    public boolean leave() {
        if (!occupied) {
            return true;
        }

        if (effect.leave()) {
            occupied = false;
            return true;
        }

        return false;
    }

    /**
     * Updates the state of the pipe.
     */
    @Override
    public void tick() {
        effect.tick();

        if (pierceCooldown > 0) {
            pierceCooldown--;
        }
    }

    /**
     * Patches the pipe.
     */
    public boolean patch() {
        if (pierced) {
            pierced = false;
            this.pierceCooldown = Random.generateRandomInt(1, 5);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pierces the pipe if it has not been patched for a while.
     */
    public boolean pierce() {
        if (pierceCooldown == 0) {
            pierced = true;
            ground.storeAmount(removeAllWater());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tries to store the given amount of water into the pipe.
     *
     * @param amount the amount to store
     * @return the amount of water that couldn't be stored
     */
    public int addWater(int amount) {
        if (endpoints.size() < 2 || pierced) {
            ground.storeAmount(amount);
            return 0;
        }

        if (storedWater + amount > capacity) {
            int tempStoredWater = storedWater;
            storedWater = capacity;
            return tempStoredWater + amount - capacity;
        }

        storedWater += amount;
        return 0;
    }

    /**
     * Tries to remove the given amount of water from the pipe.
     *
     * @param amount the amount to remove
     * @return the amount of water that could be removed
     */
    public int removeWater(int amount) {
        if (storedWater < amount) {
            int tempStoredWater = storedWater;
            storedWater = 0;
            return tempStoredWater;
        }

        storedWater -= amount;
        return amount;
    }

    /**
     * Removes all water from the pipe.
     *
     * @return the amount of water that was removed
     */
    public int removeAllWater() {
        int tempStoredWater = storedWater;
        storedWater = 0;
        return tempStoredWater;
    }

    /**
     * Tries to attach the specified endpoint to itself.
     *
     * @param activeElement the endpoint to attach
     * @return true if the endpoint was successfully attached, false otherwise
     */
    public boolean addEndpoint(ActiveElement activeElement) {
        if (endpoints.contains(activeElement)) {
            return false;
        }

        if (endpoints.size() < 2) {
            endpoints.add(activeElement);
            neighbours.add(activeElement);
            return true;
        }

        return false;
    }

    /**
     * Tries to detach the specified endpoint from itself.
     * <p>
     * The water that is in the pipe gets leaked to the ground.
     *
     * @param activeElement the endpoint to detach
     * @return true if the endpoint was successfully detached, false otherwise
     */
    public boolean removeEndpoint(ActiveElement activeElement) {
        if (occupied) {
            return false;
        }

        if (endpoints.contains(activeElement)) {
            endpoints.remove(activeElement);
            neighbours.remove(activeElement);
            ground.storeAmount(removeAllWater());
            return true;
        }

        return false;
    }

    /**
     * Splits the pipe into 2 pipes, returning the newly created pipe.
     * <p>
     * One of the endpoints of the pipe will be detached, and attached to the new pipe.
     * Both pipes will have 1 endpoint attached in the end.
     *
     * @return the new pipe that was created
     */
    public Pipe split() {
        Pipe newPipe = ModelObjectFactory.pipeCreatePipe(ground);

        ActiveElement endpoint = endpoints.get(0);
        endpoint.removePipe(this);
        endpoint.addPipe(newPipe);

        return newPipe;
    }

    /**
     * Sets the effect of the pipe.
     *
     * @param effect the effect on the pipe
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    /**
     * @return the effect on the pipe
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * @return whether the pipe is pierced or not
     */
    public boolean isPierced() {
        return pierced;
    }

    /**
     * @return the amount of water currently in this pipe
     */
    public int getStoredWater() {
        return storedWater;
    }
}
