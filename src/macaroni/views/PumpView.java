package macaroni.views;

import macaroni.actions.Action;
import macaroni.app.AssetManager;
import macaroni.math.Vector2D;
import macaroni.model.element.Pump;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A pumpák megjelenítéséért felelős osztály.
 */
public class PumpView extends View {

    /**
     * A pumpa, amit meg kell jeleníteni
     */
    private final Pump pump;
    /**
     * a pumpa üres állapotának textúrája
     */
    private final Image emptyTexture = AssetManager.getImage("PumpEmpty.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    /**
     * a pumpa vízzel töltött állapotának textúrája
     */
    private final Image fullTexture = AssetManager.getImage("PumpFull.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    /**
     * eltört textúra
     */
    private final Image brokenTexture = AssetManager.getImage("Broken.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    /**
     * pumpa ki-bemenetet jelző textúra (háromszög)
     */
    private final Image triangleTexture = AssetManager.getImage("Triangle.png").getScaledInstance(120, 120, Image.SCALE_SMOOTH);
    /**
     * a pumpa bemeneti csövének pozíciója
     */
    private Position inputPipePos;
    /**
     * a pumpa kimeneti csövének pozíciója
     */
    private Position outputPipePos;

    /**
     * Létrehoz egy új PumpView példányt.
     *
     * @param position a pumpa pozíciója.
     * @param pump     a megjelenítendő pumpa
     */
    public PumpView(Position position, Pump pump) {
        super(position);
        this.pump = pump;
    }

    /**
     * A pumpa megjelenítését végző függvény.
     *
     * @param g graphics object, amire rajzol
     */
    @Override
    public void draw(Graphics g) {
        // pump body
        if (pump.getStoredWater() == 0) {
            g.drawImage(emptyTexture,
                    position.x() - emptyTexture.getWidth(null) / 2,
                    position.y() - emptyTexture.getHeight(null) / 2,
                    null);
        } else {
            g.drawImage(fullTexture,
                    position.x() - fullTexture.getWidth(null) / 2,
                    position.y() - fullTexture.getHeight(null) / 2,
                    null);
        }

        // is broken?
        if (pump.isBroken()) {
            g.drawImage(brokenTexture,
                    position.x() - brokenTexture.getWidth(null) / 2,
                    position.y() - brokenTexture.getHeight(null) / 2,
                    null);
        }

        // IO pipe markers
        if (inputPipePos != null) drawTriangle(g, inputPipePos, true);
        if (outputPipePos != null) drawTriangle(g, outputPipePos, false);
    }

    /**
     * Draws a pipe marker.
     *
     * @param g        the graphics object to draw on
     * @param pipePos  position of the pipe
     * @param intoPump if triangle should face towards the pump
     */
    private void drawTriangle(Graphics g, Position pipePos, boolean intoPump) {
        int distFromPump = 50;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Vector2D triangleVec = new Vector2D(position.scale(-1).add(pipePos))
                .normalize().scale(distFromPump);

        // read from bottom to top             java math my beloved
        AffineTransform mx = AffineTransform.getTranslateInstance( // translate to map space
                position.x() + triangleVec.getX(),
                position.y() + triangleVec.getY()
        );
        mx.concatenate(AffineTransform.getRotateInstance( // rotate to pipe direction
                triangleVec.getX(), triangleVec.getY())
        );
        mx.concatenate(AffineTransform.getRotateInstance(Math.PI / 2)); // rotate to face X axis
        if (intoPump) { // flip if towards pump
            mx.concatenate(AffineTransform.getRotateInstance(Math.PI));
        }
        mx.concatenate(AffineTransform.getScaleInstance(0.25, 0.25)); // scaling
        mx.concatenate(AffineTransform.getTranslateInstance( // center image in image space
                -triangleTexture.getWidth(null) / 2.0,
                -triangleTexture.getHeight(null) / 2.0)
        );

        g2.drawImage(triangleTexture, mx, null);
    }

    /**
     * A pumpa kiválasztását végző függvény.
     *
     * @param action az action, amit el kell végezni
     * @return sikerült-e az action
     */
    @Override
    public boolean select(Action action) {
        return action.doAction(pump);
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
        return square((double) x - position.x()) + square((double) y - position.y()) <=
                square(fullTexture.getWidth(null) / 2.0);
    }

    /**
     * Quick helper function to square numbers. I looked inside the implementation of
     * Math.pow() and I am now convinced that it's the work of the devil.
     * As a Christian man, I'll distance myself from it as much as possible.
     *
     * @param a the number to be squared
     * @return the squared number
     */
    private double square(double a) {
        return a * a;
    }

    /**
     * Beállítja a pumpa input csövének helyét az új helyre.
     *
     * @param pos az új hely
     */
    public void setInputPipePos(Position pos) {
        inputPipePos = pos;
    }

    /**
     * Beállítja a pumpa output csövének helyét az új helyre.
     *
     * @param pos az új hely
     */
    public void setOutputPipePos(Position pos) {
        outputPipePos = pos;
    }
}
