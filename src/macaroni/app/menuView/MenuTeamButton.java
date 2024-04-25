package macaroni.app.menuView;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Button used for adding in new players in a team.
 */
public final class MenuTeamButton extends JButton {
    /**
     * Constructor, creates a new MenuTeamButton.
     *
     * @param size the size of the button
     * @param listener the method that gets called if the button is clicked
     */
    public MenuTeamButton(Dimension size, ActionListener listener) {
        super("+");
        setPreferredSize(size);
        addActionListener(listener);
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.BOLD, size.height * 1.5f));
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setColor(GameColors.lateSunset);
        RoundRectangle2D baseRectangle = new RoundRectangle2D.Double(0, 0, width, height, 0, 0);
        g2.fill(baseRectangle);

        g2.setColor(getModel().isPressed() ? GameColors.oceanBlue : getModel().isRollover() ? GameColors.snowWhite : GameColors.camelFur);
        g2.setFont(getFont());

        FontMetrics fontMetrics = g2.getFontMetrics();
        int textX = (width - fontMetrics.stringWidth(getText())) / 2;
        int textY = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
