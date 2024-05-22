package macaroni.app.menu_view;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import javax.swing.*;
import java.awt.*;

/**
 * Custom cell renderer for rendering MenuList elements
 */
public class MenuListCellRenderer extends DefaultListCellRenderer {
    private final int cellHeight;
    private final float fontSize;
    private final boolean selectionEnabled;

    /**
     * Constructor, creates a new MenuListCellRenderer.
     *
     * @param cellHeight the height of each list item
     * @param selectionEnabled true if the items in the list can be selected
     */
    public MenuListCellRenderer(int cellHeight, boolean selectionEnabled) {
        this.cellHeight = cellHeight;
        this.fontSize = cellHeight / 3.0f * 2;
        this.selectionEnabled = selectionEnabled;
    }

    /**
     * Method used by the renderer.
     *
     * @param list the JList we're painting
     * @param value the value returned by list.getModel().getElementAt(index)
     * @param index the cells index
     * @param isSelected true if the specified cell was selected
     * @param cellHasFocus true if the specified cell has the focus
     * @return the component
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, selectionEnabled && isSelected, selectionEnabled && cellHasFocus);
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, cellHeight));
        component.setFont(AssetManager.getFont("ChakraPetch-Regular.ttf").deriveFont(Font.PLAIN, fontSize));
        component.setForeground(GameColors.snowWhite);
        return component;
    }

}
