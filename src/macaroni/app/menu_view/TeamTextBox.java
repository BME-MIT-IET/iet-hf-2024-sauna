package macaroni.app.menu_view;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * TextBox used to enter the name of new teammates.
 */
public final class TeamTextBox extends JTextField {

    private boolean holdsText = false;
    private static final int MAX_NAME_LENGTH = 10; // Maximum allowed length for the name

    /**
     * Constructor, creates a new TeamTextBox.
     *
     * @param position the position of the TextBox
     * @param size     the preferred size of the TextBox
     */
    public TeamTextBox(Point position, Dimension size, ActionListener listener) {
        setLocation(position);
        setPreferredSize(size);
        setBackground(GameColors.lateSunset);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(size.height / 8, size.height / 4, 0, 0));
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.PLAIN, size.height / 2.5f));
        editingEnded();

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                editingStarted();
            }

            @Override
            public void focusLost(FocusEvent e) {
                editingEnded();
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_SPACE || getText().length() >= MAX_NAME_LENGTH) {
                    e.consume(); // Ignore the key input if it's a space or if maximum length is reached
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Event is not handled
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Event is not handled
            }
        });

        addActionListener(e -> {
            String name = getText().trim(); // Remove leading and trailing whitespace
            if (name.length() > MAX_NAME_LENGTH) {
                // Show an error message or handle the situation where the name is too long
                JOptionPane.showMessageDialog(null, "The name cannot exceed 10 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                setText(name.substring(0, MAX_NAME_LENGTH)); // Truncate the name to the maximum length
            } else {
                listener.actionPerformed(e);
                editingStarted();
            }
        });
    }

    /**
     * Gives back the name stored, then writes out the tooltip.
     *
     * @return the new player name, empty string if no name was entered prior.
     */
    public String takeTeamName() {
        if (!holdsText) return "";
        String text = getText();
        setText("");
        editingEnded();
        return text;
    }

    /**
     * Starts the name editing, removes the tooltip.
     */
    private void editingStarted() {
        if (!holdsText) {
            setText("");
            setFont(getFont().deriveFont(Font.PLAIN));
            holdsText = true;
        }
    }

    /**
     * Ends the name editing, if no name is stored, writes out the tooltip.
     */
    private void editingEnded() {
        String toolTip = "Enter player name";
        holdsText = !getText().isEmpty() && !getText().equals(toolTip);

        if (!holdsText) {
            setText(toolTip);
            setFont(getFont().deriveFont(Font.ITALIC));
        }
    }

}
