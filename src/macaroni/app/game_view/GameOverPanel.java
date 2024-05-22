package macaroni.app.game_view;

import macaroni.app.AssetManager;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel for drawing the game over state
 */
public final class GameOverPanel extends JPanel {
    /**
     * the texture of the game over state
     */
    private final transient Image texture;

    /**
     * Constructs a GameOverPanel
     *
     * @param size the size of the panel
     * @param gameOverImageFileName the name of the texture's file
     */
    public GameOverPanel(Dimension size, String gameOverImageFileName) {
        setSize(size);
        texture = AssetManager.getImage(gameOverImageFileName).getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
    }

    /**
     * Draws the given texture to its fullest
     *
     * @param graphics the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(texture, 0, 0, null);
    }
}
