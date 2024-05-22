package macaroni.views;

import macaroni.actions.Action;

import java.awt.*;

/**
 * Objektumok grafikus megjelenítéséért és a rajtuk kijelölt akciók elvégzéséért felelős absztrakt osztály.
 */
public abstract class View {

    /**
     * A pozíció, ahova ki kell rajzolni az objektumot.
     */
    protected Position position;

    protected View(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * A kirajzolásért felelős függvény, absztrakt.
     *
     * @param g graphics object, amire rajzol
     */
    public abstract void draw(Graphics g);

    /**
     * Amennyiben a felhasználó erre a példányra kattintott, elvégzi a paraméterként kapott action-t.
     *
     * @param action az action, amit el kell végezni
     * @return Igazzal tér vissza amennyiben sikeres.
     */
    public boolean select(Action action) {
        return false;
    }

    /**
     * Megnézi, hogy az adott koordináták ebben a view-ben vannak-e.
     *
     * @param x az x koordináta
     * @param y az y koordináta
     * @return Igazzal tér vissza, ha ebben a view-ben vannak.
     */
    public boolean isInside(int x, int y) {
        return false;
    }
}
