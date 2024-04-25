package macaroni.app.menuView;

import macaroni.app.GameColors;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for displaying and managing list items in MenuPanels
 * @param <T> the list type
 */
public final class MenuList<T> extends JList<T> {
    List<T> elementList = new ArrayList<>();

    /**
     * Constructor, creates a new MenuList.
     *
     * @param cellHeight the height of each list item
     * @param selectable true if the items in the list can be selected
     */
    public MenuList(int cellHeight, boolean selectable) {
        setBackground(GameColors.camelFur);
        setCellRenderer(new MenuListCellRenderer(cellHeight, selectable));
        setSelectionBackground(GameColors.lateSunset);
    }

    /**
     * Adds an element to the list.
     *
     * @param element the element to be added
     */
    public void addElement(T element) {
        elementList.add(element);
        setListData((T[]) elementList.toArray());
    }

    /**
     * Gets the elements of the list.
     *
     * @return the list of elements
     */
    public List<T> getElements() {
        return elementList;
    }

    /**
     * Returns the currently selected element.
     * If no element is selected returns null.
     *
     * @return the selected list element
     */
    public T getSelectedElement() {
        if (getSelectedIndex() == -1) return null;
        return elementList.get(getSelectedIndex());
    }
}
