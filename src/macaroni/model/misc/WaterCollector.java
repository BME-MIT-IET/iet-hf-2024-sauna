package macaroni.model.misc;

/**
 * Stores water with infinite capacity.
 */
public class WaterCollector {

    /**
     * The amount of water stored by the water collector.
     */
    private int storedAmount = 0;

    /**
     * Stores the given amount of water.
     *
     * @param amount the amount of water that needs to be stored
     */
    public void storeAmount(int amount) {
        storedAmount += amount;
    }

    /**
     * @return the amount of water stored by this water collector
     */
    public int getStoredAmount() {
        return storedAmount;
    }
}
