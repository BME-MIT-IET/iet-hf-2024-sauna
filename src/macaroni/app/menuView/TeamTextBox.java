package macaroni.app.menuView;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * TextBox used to enter the name of new teammates.
 */
public final class TeamTextBox extends JTextField {

    private boolean holdsText = false;

    /**
     * Constructor, creates a new TeamTextBox.
     *
     * @param position the position of the TextBox
     * @param size the preferred size of the TextBox
     */
    public TeamTextBox(Point position, Dimension size, ActionListener listener) {
        setLocation(position);
        setPreferredSize(size);
        setBackground(GameColors.lateSunset);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(size.height / 8,size.height / 4,0,0));
        setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.PLAIN, size.height / 2.5f));
        EditingEnded();

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                EditingStarted();
            }

            @Override
            public void focusLost(FocusEvent e) {
                EditingEnded();
            }
        });
        addActionListener(e -> {
            listener.actionPerformed(e);
            EditingStarted();
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
        EditingEnded();
        return text;
    }

    /**
     * Starts the name editing, removes the tooltip.
     */
    private void EditingStarted() {
        if (!holdsText) {
            setText("");
            setFont(getFont().deriveFont(Font.PLAIN));
            holdsText = true;
        }
    }

    /**
     * Ends the name editing, if no name is stored, writes out the tooltip.
     */
    private void EditingEnded() {
        String toolTip = "Enter player name";
        holdsText = !getText().equals("") && !getText().equals(toolTip);

        if (!holdsText) {
            setText(toolTip);
            setFont(getFont().deriveFont(Font.ITALIC));
        } else {
            if (getText().length() > 20) {
                setText(getText().substring(0, 20));
            }
        }
    }
}
