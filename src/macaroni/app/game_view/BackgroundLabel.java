package macaroni.app.game_view;

import macaroni.app.AssetManager;

import javax.swing.*;
import java.awt.*;

public class BackgroundLabel extends JLabel {

    public BackgroundLabel(Dimension size) {
        setSize(size);
        setPreferredSize(size);

        Image background = AssetManager.getImage("Background.png");
        background = background.getScaledInstance(
                (int) (background.getWidth(null) * ((double) size.height / background.getHeight(null))),
                size.height, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(background));
    }
}
