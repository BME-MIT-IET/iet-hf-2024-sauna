package macaroni.app.game_view;

import macaroni.app.menu_view.CloseButton;
import macaroni.model.character.Character;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.WindowEvent;    

/**
 * Panel displaying the game menu.
 */
public class GamePanel extends JPanel {
    /**
     * Struct holding ActionButton text and OnClick methods.
     */
    static class MenuAction {
        public String title;
        public Runnable listener;

        public MenuAction(String title, Runnable listener) {
            this.title = title;
            this.listener = listener;
        }
    }

    /**
     * the offset of the buttons
     */
    private int buttonOffset;
    /**
     *  the height of the notification label
     */
    private int notificationLabelHeight;
    /**
     * the notification label
     */
    private NotificationLabel notificationLabel;
    /**
     * the currently possible actions
     */
    private final List<MenuAction> actionList = new ArrayList<>();
    /**
     * the score panel
     */
    private ScorePanel scorePanel;
    /**
     * the display for the current character's icon
     */
    private CharacterDisplayPanel characterLabel;
    /**
     * the hand view of the current character
     */
    private HandView handView;

    private JButton closeButton;
    /**
     * Constructor, creates a new GamePanel.
     */
    public GamePanel(Dimension size) {
        setPreferredSize(new Dimension(
                size.width / 3,
                size.height
        ));
        setSize(new Dimension(
                size.width / 3,
                size.height
        ));

        reloadComponents();
    }

    /**
     * Displays the components of the GamePanel.
     */
    public void reloadComponents() {
        if (getWidth() < 25 || getHeight() < 25) {
            return;
        }

        removeAll();
        setLayout(null);
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);

        notificationLabelHeight = (int) (getHeight() / 7.5f);
        buttonOffset = getHeight() / 13;

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setSize((int) (getWidth() * 0.585f), getHeight());
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(new Color(0, 0, 0, 0));
        mainContentPanel.setOpaque(false);
        //mainContentPanel.setBackground(GameColors.mohammedanMagic);

        notificationLabel = new NotificationLabel(new Dimension(getWidth(), notificationLabelHeight));
        mainContentPanel.add(notificationLabel, BorderLayout.SOUTH);

        for (int i = 1; i <= actionList.size(); i++) {
            Point position = new Point((int) (getWidth() / 3.7f), getHeight() - notificationLabelHeight - i * buttonOffset);
            Dimension size = new Dimension((int) (getWidth() * 2 / 4.2f), getHeight() / 16);
            ActionButton button = new ActionButton(actionList.get(i - 1).title, position, size, actionList.get(i - 1).listener);
            add(button);
        }

        scorePanel = new ScorePanel(
                new Point((int) (getWidth() / 4.5f), getHeight() / 10),
                new Dimension((int) (getWidth() * 2 / 4.2f), getHeight() / 7)
        );
        add(scorePanel);

        characterLabel = new CharacterDisplayPanel(
                new Point(
                        2 * scorePanel.getX() + scorePanel.getWidth() + getHeight() / 14,
                        getHeight() / 10),
                new Dimension(
                        getHeight() / 7,
                        getHeight() / 7)
        );
        add(characterLabel);

        handView = new HandView(
                new Point(
                        (int) (characterLabel.getX() + characterLabel.getWidth() * 1.5),
                        (int) (characterLabel.getY() + characterLabel.getHeight() * 0.5)
                ),
                characterLabel.getSize());
        add(handView);

        add(mainContentPanel);

        closeButton = new CloseButton(new Point(10, 10), new Dimension(80, 40), e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }
        }, "Exit");
        add(closeButton);

    }

    /**
     * Adds in a new action.
     *
     * @param title    the title of the action
     * @param listener the action method
     */
    public void addAction(String title, Runnable listener) {
        actionList.add(new MenuAction(title, listener));
        reloadComponents();
    }

    /**
     * Adds in a new action.
     *
     * @param action the new Action
     */
    public void addAction(MenuAction action) {
        actionList.add(action);
        reloadComponents();
    }

    /**
     * Sets the list of actions.
     * If defined null, the action list will be empty.
     *
     * @param actions the actions
     */
    public void setActionList(List<MenuAction> actions) {
        actionList.clear();
        if (actions == null) return;
        actionList.addAll(actions);
        reloadComponents();
    }

    /**
     * Sets the notification message.
     *
     * @param message the message
     */
    public void setNotification(String message) {
        notificationLabel.setText(message);
    }

    /**
     * Sets the displayed score.
     *
     * @param plumberScore  the score of the plumbers
     * @param saboteurScore the score of the saboteurs
     */
    public void setScore(int plumberScore, int saboteurScore) {
        scorePanel.updateScore(plumberScore, saboteurScore);
    }

    public void setRound(int currentRound) {
        scorePanel.updateRound(currentRound);
    }

    /**
     * Sets the displayed character.
     *
     * @param character the character
     * @param name      the name of the character
     */
    public void setCharacter(Character character, String name) {
        if (characterLabel != null) {
            characterLabel.displayCharacter(character, name);
        }
        if (handView != null) {
            handView.set(character);
        }
    }
}
