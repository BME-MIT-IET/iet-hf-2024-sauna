package macaroni.app.gameView;

import macaroni.app.logic.Game;
import macaroni.model.character.Plumber;
import macaroni.model.character.Saboteur;
import macaroni.model.element.Cistern;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;
import macaroni.model.element.Spring;
import macaroni.utils.ModelObjectFactory;
import macaroni.utils.ModelObjectLoadedListener;
import macaroni.utils.ModelObjectSerializer;
import macaroni.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for painting the map of the game
 */
public final class MapPanel extends JPanel implements ModelObjectLoadedListener {

    /**
     * the positions of the game objects
     */
    private final Map<Object, Position> objectPositions = new HashMap<>();
    /**
     * utility for a dragging effect
     */
    private final Dragger dragger;

    /**
     * Constructs a map panel.
     *
     * @param size the size of the panel
     * @param dragger the dragging utility to be used
     */
    public MapPanel(Dimension size, Dragger dragger) {
        this.dragger = dragger;
        ModelObjectSerializer.setModelObjectLoadedListener(this);

        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);

        setPreferredSize(size);
        setSize(size);
    }

    public void load(Game game) {
        // create views for active elements
        for (Object obj : objectPositions.keySet()) {
            switch (obj.getClass().getSimpleName()) {
                case "Cistern" -> ViewRepository.add(obj, new CisternView(objectPositions.get(obj), (Cistern) obj));
                case "Pump" -> ViewRepository.add(obj, new PumpView(objectPositions.get(obj), (Pump) obj));
                case "Spring" -> ViewRepository.add(obj, new SpringView(objectPositions.get(obj), (Spring) obj));
            }
        }

        // create views for pipes
        for (Object obj : ModelObjectFactory.getObjectList()) {
            if (obj instanceof Pipe p) {
                ViewRepository.add(obj, new PipeView(p, new Position[] {
                        ViewRepository.getViewOfObject(p.getEndpoint(0)).getPosition(),
                        ViewRepository.getViewOfObject(p.getEndpoint(1)).getPosition()
                }));
            }
        }

        // set pumpViews input/output pipe position
        for (Pump pump : ModelObjectFactory.getObjectList(Pump.class)) {
            Pipe input = pump.getInputPipe();
            if (input != null) {
                PumpView view = (PumpView) ViewRepository.getViewOfObject(pump);
                view.setInputPipePos(ViewRepository.getViewOfObject(input).getPosition());
            }
            Pipe output = pump.getOutputPipe();
            if (output != null) {
                PumpView view = (PumpView) ViewRepository.getViewOfObject(pump);
                view.setOutputPipePos(ViewRepository.getViewOfObject(output).getPosition());
            }
        }

        // create views for characters
        for (Plumber plumber : game.getPlumbers()) {
            View locationView = ViewRepository.getViewOfObject(plumber.getLocation());
            ViewRepository.add(plumber, new PlumberView(
                    locationView.getPosition(), plumber, game.getCharacterName(plumber)
            ));
        }
        for (Saboteur saboteur : game.getSaboteurs()) {
            View locationView = ViewRepository.getViewOfObject(saboteur.getLocation());
            ViewRepository.add(saboteur, new SaboteurView(
                    locationView.getPosition(), saboteur, game.getCharacterName(saboteur)
            ));
        }
    }

    /**
     * Puts the object and its position into cache
     *
     * @param object the object that was loaded
     * @param name   the name of the object
     * @param pos    the positional data of the object, or null if this object had no positional data
     */
    @Override
    public void modelObjectLoaded(Object object, String name, Position pos) {
        objectPositions.put(object, pos);
    }

    /**
     * Draws each map element with the dragger's transform
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        if (g instanceof Graphics2D graphics2D) {
            AffineTransform transform = graphics2D.getTransform();
            transform.concatenate(dragger.getTransform());
            graphics2D.setTransform(transform);
        }

        super.paintComponent(g);

        // draw pipes first
        ViewRepository.forEach(v -> v instanceof PipeView, v -> v.draw(g));

        // draw active elements
        ViewRepository.forEach(
                v -> v instanceof CisternView || v instanceof PumpView || v instanceof SpringView,
                v -> v.draw(g)
        );

        // draw effects
        ViewRepository.forEach(v -> v instanceof BananaEffectView || v instanceof TechnokolEffectView, v -> v.draw(g));

        // draw characters
        ViewRepository.forEach(v -> v instanceof CharacterView, v -> v.draw(g));
    }
}
