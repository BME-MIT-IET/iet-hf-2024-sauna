package macaroni.app.gameView;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static javax.swing.text.StyleConstants.setAlignment;

/**
 * Represents the notification label of the game menu
 */
public final class NotificationLabel extends JTextArea {
    /**
     * Creates a new NotificationPanel.
     *
     * @param size the size of the panel
     */
    public NotificationLabel(Dimension size) {
        setSize(size);
        setPreferredSize(size);
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.BOLD, size.height / 4.5f));
        setForeground(GameColors.oceanBlue);
        setBorder(new EmptyBorder(size.width / 30,size.width / 15,size.width / 12,size.width / 15));
        setBackground(GameColors.camelFur);
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setHighlighter(null);
        setOpaque(true);
    }
}
