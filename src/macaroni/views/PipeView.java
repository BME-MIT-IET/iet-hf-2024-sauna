package macaroni.views;

import macaroni.actions.Action;
import macaroni.app.AssetManager;
import macaroni.app.GameColors;
import macaroni.math.Vector2D;
import macaroni.model.element.Pipe;

import java.awt.*;

/**
 * A csövek megjelenítéséért felelős osztály.
 */
public class PipeView extends View {

    /**
     * A cső, amit meg kell jeleníteni
     */
    private final Pipe pipe;

    /**
     * A cső 2 végének pozíciója.
     */
    private final Position[] endpointsPos = new Position[2];

    /**
     * A cső szélessége
     */
    private final int outerWidth = 21;

    /**
     * Új cső-e.
     */
    private boolean isNew = false;

    /**
     * Beállítja a csövet, hogy új cső legyen.
     *
     * @param value az érték
     */
    public void setNew(boolean value) {
        isNew = value;
    }

    /**
     * Létrehoz egy PipeView példányt.
     *
     * @param pipe a cső, amit megjelenít
     * @param endpointsPos a cső 2 végpontjának pozíciója
     */
    public PipeView(Pipe pipe, Position[] endpointsPos) {
        super(endpointsPos[0].add(endpointsPos[1]).scale(0.5));
        this.pipe = pipe;
        this.endpointsPos[0] = endpointsPos[0];
        this.endpointsPos[1] = endpointsPos[1];
    }

    /**
     * A cső megjelenítését végző függvény.
     *
     * @param g graphics object, amire rajzol
     */
    @Override
    public void draw(Graphics g) {
        int innerWidth = 7;

        Graphics2D g2 = (Graphics2D) g.create(); // necessary
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // outline
        g2.setColor(GameColors.oceanBlue);
        g2.setStroke(new BasicStroke(outerWidth));
        g2.drawLine(endpointsPos[0].x(), endpointsPos[0].y(),
                endpointsPos[1].x(), endpointsPos[1].y());
        // inner area
        if (pipe.getStoredWater() == 0) g2.setColor(GameColors.desertYellow);
        else g2.setColor(GameColors.lateSunset);
        g2.setStroke(new BasicStroke(innerWidth));
        g2.drawLine(endpointsPos[0].x(), endpointsPos[0].y(),
                endpointsPos[1].x(), endpointsPos[1].y());

        // is pierced?
        if (pipe.isPierced()) {
            int brokenSize = 50;
            g2.drawImage(AssetManager.getImage("Broken.png"),
                    position.x() - brokenSize / 2, position.y() - brokenSize / 2,
                    brokenSize, brokenSize, null);
        }
    }

    /**
     * A cső kiválasztását végző függvény.
     *
     * @param action az action, amit el kell végezni
     * @return sikerült-e az action
     */
    @Override
    public boolean select(Action action) {
        return action.doAction(pipe);
    }

    /**
     * Megnézi, hogy az adott koordináták ebben a view-ben vannak-e.
     *
     * @param x az x koordináta
     * @param y az y koordináta
     * @return Igazzal tér vissza, ha ebben a view-ben vannak.
     */
    @Override
    public boolean isInside(int x, int y) {
        Vector2D endToClick = new Vector2D(endpointsPos[0].scale(-1).add(new Position(x, y)));
        Vector2D dir = new Vector2D(endpointsPos[0].scale(-1).add(endpointsPos[1]));
        Vector2D normal = new Vector2D(dir.getY(), -dir.getX()).normalize();
        return Math.abs(normal.dot(endToClick)) <= outerWidth // inside width
                && dir.normalize().dot(endToClick) <= dir.length() // inside length
                && dir.normalize().dot(endToClick) > 0;
    }

    /**
     * Egy pozíciót a megadott új pozícióra cseréli le.
     *
     * @param target a pozíció amit le kell cserélni
     * @param newPos az új pozíció
     */
    public void replaceEndpointPos(Position target, Position newPos) {
        if ((isNew && endpointsPos[1] == target) || (!isNew && endpointsPos[0] == target)) {
            System.out.println("replaced endpoint 0");
            endpointsPos[0] = newPos;

        } else if ((isNew && endpointsPos[0] == target) || (!isNew && endpointsPos[1] == target)) {
            System.out.println("replaced endpoint 1");
            endpointsPos[1] = newPos;
        }
        isNew = false;
        position = endpointsPos[0].add(endpointsPos[1]).scale(0.5);
    }
}
