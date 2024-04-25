package macaroni.app.menuView;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The label used by the MenuPanels.
 */
public final class MenuLabel extends JLabel {
    /**
     * Constructs a new TitleLabel.
     *
     * @param text the text of the label
     * @param preferredSize the preferred size of the label
     */
    public MenuLabel(String text, Dimension preferredSize) {
        setText(text);
        setPreferredSize(preferredSize);
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf")
                .deriveFont(Font.BOLD, preferredSize.height / 2.0f));
        EmptyBorder border = new EmptyBorder(0, preferredSize.height / 4, 0, 0);
        setBorder(border);
        setLayout(new BorderLayout());
        setBackground(GameColors.lateSunset);
        setForeground(GameColors.camelFur);
        setOpaque(true);
    }
}
