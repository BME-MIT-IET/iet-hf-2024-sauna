package macaroni.app.menuView;

import macaroni.app.GameColors;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.util.EventListener;

/**
 * Class used for the panels in the menu.
 */
public class MenuPanel extends JPanel {
    /**
     * The list of elements in the panel
     */
    protected MenuList<String> elementList;
    private PanelListener listener;

    /**
     * Constructor, creates a new MenuPanel.
     *
     * @param title the title of thw panel
     * @param centerOnScreen the center of the panel on the screen in pixel coordinates
     * @param size the size of the panel in pixels
     * @param listElementHeight the height of the elements inside the panel in pixels
     * @param elementsSelectable set to true if the elements in the list can be selected
     */
    public MenuPanel(String title, Point centerOnScreen, Dimension size, int listElementHeight, boolean elementsSelectable, PanelListener listener) {
        this.listener=listener;
        setBounds(centerOnScreen.x - size.width / 2, centerOnScreen.y - size.height / 2, size.width, size.height);
        setBorder(BorderFactory.createLineBorder(GameColors.oceanBlue, size.width / 25));
        setLayout(new BorderLayout());
        setBackground(GameColors.camelFur);

        MenuLabel titleLabel = new MenuLabel(title, new Dimension(size.width, listElementHeight * 3 / 2));
        add(titleLabel, BorderLayout.NORTH);

        elementList = new MenuList<>(listElementHeight, elementsSelectable);
        elementList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    listener.onPanelChange();
                }
            }
        });
        

        JScrollPane scrollPane = new JScrollPane(elementList);
        scrollPane.setBorder(new EmptyBorder(0,20,0,20));
        scrollPane.setBackground(GameColors.camelFur);
        scrollPane.getVerticalScrollBar().setUI(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds an element to the elementList.
     *
     * @param element the element to be added
     */
    public void addElement(String element) {
        elementList.addElement(element);
    }

    public interface PanelListener extends EventListener {
        void onPanelChange();
    }
}
