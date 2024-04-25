package macaroni.app.gameView;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;
import macaroni.model.character.Character;
import macaroni.model.character.Plumber;

import javax.swing.*;
import java.awt.*;

public class CharacterDisplayPanel extends JPanel {

    private String name;
    private Character character;

    Image profilePicture = null;

    /**
     * Constructor, creates a new CharacterDisplayPanel.
     *
     * @param positionOfCenter the position of the center of the panel
     * @param size the size of the panel
     */
    public CharacterDisplayPanel(Point positionOfCenter, Dimension size) {
        setSize(size);
        setLocation(positionOfCenter.x - size.width / 2, positionOfCenter.y - size.height / 2);
    }

    /**
     * Sets the character currently being displayed.
     *
     * @param character the character to display
     * @param name the name of the character
     */
    public void displayCharacter(Character character, String name) {
        this.character = character;
        this.name = name;
        profilePicture = (character instanceof Plumber ?
                AssetManager.getImage("PlumberProfile.png") : AssetManager.getImage("SaboteurProfile.png"))
                .getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        repaint();
    }

    /**
     * Draws the CharacterDisplayPanel.
     *
     * @param g the graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float border = getHeight() / 18.0f;

        if (character == null) return;

        g2.setColor(GameColors.oceanBlue);
        g2.setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.BOLD, getHeight() / 6.0f));

        if (profilePicture != null) {
            g2.drawImage(profilePicture, 0, 0, null);
        }

        FontMetrics fontMetrics = g2.getFontMetrics();
        float textX = 2.1f * border;
        float textY = (getHeight() - fontMetrics.getHeight()) + fontMetrics.getAscent() - 0.7f * border;
        g2.drawString(name, textX, textY);
    }
}
