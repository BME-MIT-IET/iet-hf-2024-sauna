package macaroni.views;

import macaroni.app.AssetManager;
import macaroni.model.character.Plumber;

import java.awt.*;

/**
 * A szerelők megjelenítéséért felelős osztály.
 */
public class PlumberView extends CharacterView {

    /**
     * A szerelő textúrája.
     */
    private final Image texture = AssetManager.getImage("Plumber.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    /**
     * Létrehoz egy PlumberView példányt.
     *
     * @param position a szerelő helye.
     * @param plumber a szerelő, akit ez a view megjelenít
     * @param name a szerelő neve
     */
    public PlumberView(Position position, Plumber plumber, String name) {
        super(position, name);
        /**
         * A szerelő, akit meg kell jeleníteni.
         */
    }

    /**
     * A szerelő megjelenítését végző függvény.
     *
     * @param g graphics object, amire rajzol
     */
    @Override
    public void draw(Graphics g) {
        int yOffset = 20;
        int x = position.x() - texture.getWidth(null) / 2;
        int y = position.y() - texture.getHeight(null) / 2;
        g.drawImage(texture, x, y - yOffset, null);
        drawName(g, x, (int) (y - 1.5 * yOffset));
    }
}
