package macaroni.views;

import macaroni.app.AssetManager;
import macaroni.model.effects.TechnokolEffect;

import java.awt.*;

/**
 * A technokol effekt megjelenítéséért felelős osztály.
 */
public class TechnokolEffectView extends View {

    /**
     * A technokol effekt, amit meg kell jeleníteni
     */
    private final TechnokolEffect effect;
    /**
     * Az effekt textúrája
     */
    private final Image texture = AssetManager.getImage("Technokol.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    /**
     * Az árnyék textúrája
     */
    private final Image shadowTexture = AssetManager.getImage("EffectShadow.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    /**
     * Létrehoz egy TechnokolEffectView példányt.
     *
     * @param position az effekt pozíciója
     * @param effect a megjelenítendő effekt
     */
    public TechnokolEffectView(Position position, TechnokolEffect effect) {
        super(position);
        this.effect = effect;
    }

    /**
     * A technokol effekt megjelenítését végző függvény.
     *
     * @param g graphics object, amire rajzol
     */
    @Override
    public void draw(Graphics g) {
        if (effect.getCountdown() == 0) return;

        int yOffset = 10;
        // shadow
        g.drawImage(
                shadowTexture,
                position.x() - texture.getWidth(null) / 2,
                position.y() - texture.getHeight(null) / 2,
                null
        );
        // effect
        g.drawImage(texture,
                position.x() - texture.getWidth(null) / 2,
                position.y() - texture.getHeight(null) / 2 - yOffset,
                null);
    }
}
