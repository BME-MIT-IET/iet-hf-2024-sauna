package macaroni.app;

import macaroni.app.logic.GameInfo;

import java.awt.*;

/**
 * Controller class for managing state between the main panels of the window
 */
public final class Controller {
    /**
     *  Used with CardLayout inside the window for tagging each menu
     */
    public static final class MenuTag {
        /**
         * the tag of the main menu
         */
        public static final MenuTag MAIN = new MenuTag("MainMenu");
        /**
         * the tag of the game menu
         */
        public static final MenuTag GAME = new MenuTag("GameMenu");

        /**
         * the string under the tag
         */
        private final String name;

        /**
         * Constructs a new MenuTag
         *
         * @param name the underlying string
         */
        private MenuTag(String name) {
            this.name = name;
        }

        /**
         * @return the underlying string of the tag
         */
        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * the window
     */
    private final Window window;
    /**
     * gameInfo for passing state between menus
     */
    private GameInfo gameInfo;

    /**
     * Constructs the Controller
     *
     * @param window the window of the application
     */
    public Controller(Window window) {
        this.window = window;
    }

    /**
     * Sets the menu with the given tag as active
     *
     * @param menuTag the tag of the menu to be set
     */
    public void setCurrentMenu(MenuTag menuTag) {
        ((CardLayout) window.getContentPane().getLayout()).show(window.getContentPane(), menuTag.toString());
    }

    /**
     * Sets the held game info
     *
     * @param gameInfo the game info
     */
    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    /**
     * Gets the game info
     *
     * @return the game info
     */
    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
