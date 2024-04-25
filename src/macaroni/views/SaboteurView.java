package macaroni.views;

import macaroni.app.AssetManager;
import macaroni.model.character.Saboteur;

import java.awt.*;

/**
 * A szabotőrök megjelenítéséért felelős osztály.
 */
public class SaboteurView extends CharacterView {

    /**
     * A szabotőr, akit meg kell jeleníteni
     */
    private final Saboteur saboteur;
    /**
     * A szabotőr textúrája.
     */
    private final Image texture = AssetManager.getImage("Saboteur.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    /**
     * Létrehoz egy SaboteurView példányt.
     *
     * @param position a szabotőr helye
     * @param saboteur a szabotőr, akit meg kell jeleníteni
     * @param name a szabotőr neve
     */
    public SaboteurView(Position position, Saboteur saboteur, String name) {
        super(position, name);
        this.saboteur = saboteur;
    }

    /**
     * A szabotőr megjelenítését végző függvény.
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
