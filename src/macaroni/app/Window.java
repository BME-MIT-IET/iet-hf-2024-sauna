package macaroni.app;

import macaroni.app.game_view.GameMenu;
import macaroni.app.menu_view.MainMenu;

import javax.swing.*;
import java.awt.*;

/**
 * The window of the application
 */
public final class Window extends JFrame {
    /**
     * represents the state when the window needs to be closed
     */
    private boolean isClosing = true;

    /**
     * Constructs a window with the application's menus
     */
    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setIgnoreRepaint(true);
        setUndecorated(true);

        getContentPane().setLayout(new CardLayout());
        Controller controller = new Controller(this);
        add(new MainMenu(controller, getSize()), Controller.MenuTag.MAIN.toString());
        add(new GameMenu(controller, getSize()), Controller.MenuTag.GAME.toString());
    }

    /**
     * Opens the window.
     */
    public void open() {
        isClosing = false;
        pack();
        setVisible(true);
    }

    /**
     * Closes the window.
     */
    public void close() {
        dispose();
    }

    /**
     * @return true if the window should be closed
     */
    public boolean shouldClose() {
        return isClosing;
    }
}
