package macaroni.views;

import macaroni.actions.Action;
import macaroni.app.AssetManager;
import macaroni.model.element.Cistern;

import java.awt.*;

/**
 * A ciszternák megjelenítéséért felelős osztály.
 */
public class CisternView extends View {

    /**
     * A ciszterna, amit meg kell jeleníteni
     */
    private final Cistern cistern;
    /**
     * a ciszterna textúrája
     */
    private final Image texture = AssetManager.getImage("Cistern.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    /**
     * Létrehoz egy CisternView példányt.
     *
     * @param position a ciszterna pozíciója
     * @param cistern a megjelenítendő ciszterna
     */
    public CisternView(Position position, Cistern cistern) {
        super(position);
        this.cistern = cistern;
    }

    /**
     * A ciszterna megjelenítését végző függvény.
     *
     * @param g graphics object, amire rajzol
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(texture,
                position.x() - texture.getWidth(null) / 2,
                position.y() - texture.getHeight(null) / 2,
                null);
    }

    /**
     * A ciszterna kiválasztását végző függvény.
     *
     * @param action az action, amit el kell végezni
     * @return sikerült-e az action
     */
    @Override
    public boolean select(Action action) {
        return action.doAction(cistern);
    }

    /**
     * Megnézi, hogy az adott koordináták ebben a view-ben vannak-e.
     *
     * @param x az x koordináta
     * @param y az y koordináta
     * @return Igazzal tér vissza, ha ebben a view-ben vannak.
     */
    @Override
    public boolean isInside(int x, int y) {
        return position.x() - texture.getWidth(null) / 2 <= x &&
                x <= position.x() + texture.getWidth(null) / 2 &&
                position.y() - texture.getHeight(null) / 2 <= y &&
                y <= position.y() + texture.getHeight(null) / 2;
    }
}
