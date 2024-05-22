package macaroni.app.menuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

public final class TeamPanel extends MenuPanel {
    private final TeamTextBox textBox;
    private PanelListener listener;
    /**
     * Constructor, creates a new TeamPanel.
     *
     * @param teamName          the name of the team to be displayed
     * @param centerOnScreen    the center of the panel on the screen in pixel coordinates
     * @param size              the size of the panel in pixels
     * @param listElementHeight the height of the elements inside the panel in pixels
     */
    public TeamPanel(String teamName, Point centerOnScreen, Dimension size, int listElementHeight, PanelListener listener) {
        super(teamName, centerOnScreen, size, listElementHeight, false, listener);
        this.listener = listener;
        int footerHeight = listElementHeight * 3 / 2;

        MenuLabel label = new MenuLabel("", new Dimension(size.width, footerHeight));
        add(label, BorderLayout.SOUTH);

        JButton button = new MenuTeamButton(new Dimension(footerHeight, footerHeight - 10), this::addTeammate);
        label.add(button, BorderLayout.WEST);

        textBox = new TeamTextBox(new Point(0, 0), new Dimension(size.width / 3 * 2, footerHeight), this::addTeammate);
        label.add(textBox, BorderLayout.CENTER);
    }

    /**
     * Method used by the MenuTeamButton to add new teammates.
     *
     * @param e the ActionEvent
     */
    public void addTeammate(ActionEvent e) {
        String newName = textBox.takeTeamName();
        if (newName == null || newName.trim().equals("")) return;
        if (!elementList.getElements().contains(newName)) {
            elementList.addElement(newName);
            if(listener != null) {
                listener.onPanelChange(); // Trigger the event
            }
        }
    }
}

