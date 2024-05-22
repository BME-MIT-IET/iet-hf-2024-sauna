package macaroni.app.game_view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

/**
 * Utility class for a dragging effect
 */
public final class Dragger extends MouseAdapter {

    /**
     * the position of the cursor
     */
    private Point cursor = new Point();
    /**
     * the transform of the effect
     */
    private final AffineTransform transform = new AffineTransform();

    /**
     * Gets the transform of the effect
     *
     * @return the transform of the effect
     */
    public AffineTransform getTransform() {
        return (AffineTransform) transform.clone();
    }

    /**
     * Updates the mouse cursor stored position.
     * Updates the transform.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        transform.translate(e.getX() - cursor.getX(), e.getY() - cursor.getY());
        cursor = e.getPoint();
    }

    /**
     * Updates the mouse cursor stored position.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        cursor = e.getPoint();
    }

    /**
     *  * Updates the mouse cursor stored position.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        cursor = e.getPoint();
    }
}
