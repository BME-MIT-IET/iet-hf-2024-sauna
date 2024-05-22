package macaroni.views;

import macaroni.actions.Action;
import macaroni.app.AssetManager;
import macaroni.app.GameColors;
import macaroni.math.Vector2D;
import macaroni.model.element.Pipe;

import java.awt.*;
import java.util.logging.Logger;

public class PipeView extends View {

    private static final Logger logger = Logger.getLogger(PipeView.class.getName());

    private final Pipe pipe;
    private final Position[] endpointsPos = new Position[2];
    private static final int OUTER_WIDTH = 21;
    private boolean isNew = false;

    public void setNew(boolean value) {
        isNew = value;
    }

    public PipeView(Pipe pipe, Position[] endpointsPos) {
        super(endpointsPos[0].add(endpointsPos[1]).scale(0.5));
        this.pipe = pipe;
        this.endpointsPos[0] = endpointsPos[0];
        this.endpointsPos[1] = endpointsPos[1];
    }

    @Override
    public void draw(Graphics g) {
        int innerWidth = 7;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(GameColors.oceanBlue);
        g2.setStroke(new BasicStroke(OUTER_WIDTH));
        g2.drawLine(endpointsPos[0].x(), endpointsPos[0].y(),
                endpointsPos[1].x(), endpointsPos[1].y());

        if (pipe.getStoredWater() == 0) g2.setColor(GameColors.desertYellow);
        else g2.setColor(GameColors.lateSunset);
        g2.setStroke(new BasicStroke(innerWidth));
        g2.drawLine(endpointsPos[0].x(), endpointsPos[0].y(),
                endpointsPos[1].x(), endpointsPos[1].y());

        if (pipe.isPierced()) {
            int brokenSize = 50;
            g2.drawImage(AssetManager.getImage("Broken.png"),
                    position.x() - brokenSize / 2, position.y() - brokenSize / 2,
                    brokenSize, brokenSize, null);
        }
    }

    @Override
    public boolean select(Action action) {
        return action.doAction(pipe);
    }

    @Override
    public boolean isInside(int x, int y) {
        Vector2D endToClick = new Vector2D(endpointsPos[0].scale(-1).add(new Position(x, y)));
        Vector2D dir = new Vector2D(endpointsPos[0].scale(-1).add(endpointsPos[1]));
        Vector2D normal = new Vector2D(dir.getY(), -dir.getX()).normalize();
        return Math.abs(normal.dot(endToClick)) <= OUTER_WIDTH
                && dir.normalize().dot(endToClick) <= dir.length()
                && dir.normalize().dot(endToClick) > 0;
    }

    public void replaceEndpointPos(Position target, Position newPos) {
        if ((isNew && endpointsPos[1] == target) || (!isNew && endpointsPos[0] == target)) {
            logger.info("Replaced endpoint 0");
            endpointsPos[0] = newPos;
        } else if ((isNew && endpointsPos[0] == target) || (!isNew && endpointsPos[1] == target)) {
            logger.info("Replaced endpoint 1");
            endpointsPos[1] = newPos;
        }
        isNew = false;
        position = endpointsPos[0].add(endpointsPos[1]).scale(0.5);
    }
}
