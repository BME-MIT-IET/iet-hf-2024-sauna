package macaroni.views;

import macaroni.actions.Action;
import macaroni.app.AssetManager;
import macaroni.model.element.Spring;

import java.awt.*;

/**
 * A források megjelenítéséért felelős osztály.
 */
public class SpringView extends View {

    /**
     * A forrás, amit meg kell jeleníteni
     */
    private final Spring spring;
    /**
     * A forrás textúrája
     */
    private final Image texture = AssetManager.getImage("Spring.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    /**
     * Létrehoz egy új SpringView példányt.
     *
     * @param position a forrás pozíciója
     * @param spring a megjelenítendő forrás
     */
    public SpringView(Position position, Spring spring) {
        super(position);
        this.spring = spring;
    }

    /**
     * A forrás megjelenítését végző függvény.
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
     * A forrás kiválasztását végző függvény.
     *
     * @param action az action, amit el kell végezni
     * @return sikerült-e az action
     */
    @Override
    public boolean select(Action action) {
        return action.doAction(spring);
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
