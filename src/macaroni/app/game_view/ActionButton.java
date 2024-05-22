package macaroni.app.game_view;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ActionButton extends JButton {
    /**
     * Constructor, creates a new ActionButton.
     *
     * @param positionOfCenter the center of the button on the screen in pixel coordinates
     * @param size the size of the button in pixels
     * @param listener the method that gets called if the button is clicked
     */
    public ActionButton(String text, Point positionOfCenter, Dimension size, Runnable listener) {
        super(text);
        setSize(size);
        setLocation(positionOfCenter.x - getWidth() / 2, positionOfCenter.y - getHeight() / 2);
        addActionListener(e -> listener.run());
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.BOLD, size.height / 2.1f));
        setFocusPainted(false);
        setBorder(null);
    }

    /**
     * Method used to draw the button on screen.
     *
     * @param g the graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        float outerBorder = getHeight() / 5.0f;
        int arc = getHeight();

        RoundRectangle2D borderRectangle = new RoundRectangle2D.Double(0, 0, width, height, arc, arc);
        g2.setColor(GameColors.oceanBlue);
        g2.fill(borderRectangle);

        RoundRectangle2D baseRectangle = new RoundRectangle2D.Double(outerBorder / 2.0f, outerBorder / 2.0f, width - outerBorder, height - outerBorder, arc - outerBorder, arc - outerBorder);
        g2.setColor(getModel().isPressed() ? GameColors.oceanBlue : GameColors.camelFur);
        g2.fill(baseRectangle);

        g2.setColor(getModel().isRollover() ? GameColors.snowWhite : GameColors.oceanBlue);
        g2.setFont(getFont());

        FontMetrics fontMetrics = g2.getFontMetrics();
        int textX = (height) / 2;
        int textY = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        g2.drawString(getText(), textX, textY);
    }
}
