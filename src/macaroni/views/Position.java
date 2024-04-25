package macaroni.views;

/**
 * A kirajzolt objektumok helyét tartalmazó osztály (x és y koordinátákkal).
 *
 * @param x az x koordináta
 * @param y az y koordináta
 */
public record Position(int x, int y) implements Cloneable {

    /**
     * Hozzáad ehhez a positionhöz egy másikat.
     *
     * @param other a másik position
     * @return az összeg
     */
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    /**
     * Megszorozza egy számmal a positiont és egy új positionnel tér vissza.
     *
     * @param factor a szorzó
     * @return az új position
     */
    public Position scale(double factor) {
        return new Position((int) (x * factor), (int) (y * factor));
    }

    /**
     * Lemásolja a positiont.
     * @return position másolat
     */
    @Override
    public Position clone() {
        return new Position(this.x, this.y);
    }
}
