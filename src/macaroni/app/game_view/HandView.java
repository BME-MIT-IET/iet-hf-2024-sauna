package macaroni.app.game_view;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;
import macaroni.model.character.Plumber;

import javax.swing.*;
import java.awt.*;

/**
 * Responsible for showing the hand of a plumber
 */
public final class HandView extends JComponent {
    /**
     * the number of pumps in the plumber's hand
     */
    private int pumpCount = 0;
    /**
     * the number of pipes in the plumber's hand
     */
    private int pipeCount = 10;
    /**
     * the pump's texture to be shown
     */
    private final Image pumpTexture;
    /**
     * the pipe's texture to be shown
     */
    private final Image pipeTexture;

    /**
     * Constructs a HandView instance
     *
     * @param positionOfCenter the position of the HandView's center
     * @param size the size of the HandView
     */
    public HandView(Point positionOfCenter, Dimension size) {
        setSize(size);
        setLocation(positionOfCenter.x - size.width / 2, positionOfCenter.y - size.height / 2);
        pumpTexture = AssetManager.getImage("MiniPump.png")
                .getScaledInstance(getWidth() / 3, getHeight() / 3, Image.SCALE_SMOOTH);
        pipeTexture = AssetManager.getImage("MiniPipe.png")
                .getScaledInstance(getWidth() / 3, getHeight() / 3, Image.SCALE_SMOOTH);
    }

    /**
     * Sets the look of the HandView
     *
     * @param character the current character on turn
     */
    public void set(macaroni.model.character.Character character) {
        if (character instanceof Plumber plumber) {
            setVisible(true);
            pumpCount = plumber.getHeldPumpCount();
            pipeCount = plumber.getHeldPipe() != null ? 1 : 0;
        } else {
            setVisible(false);
        }
    }

    /**
     * Draws the number of pumps and pipes in hand and an icon for each
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D graphics2D) {
            graphics2D.setColor(GameColors.oceanBlue);
            graphics2D.setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.BOLD, getHeight() / 3.5f));
            FontMetrics fontMetrics = graphics2D.getFontMetrics();
            float border = getHeight() / 18.0f;
            graphics2D.drawString(
                    Integer.toString(pumpCount),
                    5.f * getWidth() / 6.f - fontMetrics.stringWidth(Integer.toString(pumpCount)) / 2.f,
                    (getHeight() / 2.f - fontMetrics.getHeight()) + fontMetrics.getAscent() - 0.8f * border
            );
            graphics2D.drawString(
                    Integer.toString(pipeCount),
                    2.f * getWidth() / 6.f - fontMetrics.stringWidth(Integer.toString(pipeCount)) / 2.f,
                    (getHeight() / 2.f - fontMetrics.getHeight()) + fontMetrics.getAscent() - 0.8f * border
            );

            graphics2D.drawImage(
                    pipeTexture,
                    getWidth() / 6,
                    getHeight() / 2, null
            );
            graphics2D.drawImage(
                    pumpTexture,
                    4 * getWidth() / 6,
                    getHeight() / 2,
                    null
            );
        }
    }
}
