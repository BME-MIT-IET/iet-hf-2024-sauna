package macaroni.app.game_view;

import macaroni.actions.*;
import macaroni.actions.Action;
import macaroni.app.Controller;
import macaroni.app.GameColors;
import macaroni.app.logic.Game;
import macaroni.model.character.Character;
import macaroni.model.character.Plumber;
import macaroni.model.character.Saboteur;
import macaroni.model.effects.BananaEffect;
import macaroni.model.effects.TechnokolEffect;
import macaroni.model.element.*;
import macaroni.utils.ModelObjectFactory;
import macaroni.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * the game menu of the application
 */
public final class GameMenu extends JPanel {
    private static final Logger logger = Logger.getLogger(GameMenu.class.getName());
    /**
     * the game over screen
     */
    private GameOverPanel gameOverPanel;
    /**
     * the gamel panel (on the left)
     */
    private final GamePanel gamePanel;
    /**
     * the map of the game
     */
    private final MapPanel mapPanel;
    /**
     * the game
     */
    private final transient Game game = new Game();
    /**
     * the dragger utility for a dragging effect
     */
    private final Dragger dragger = new Dragger();
    /**
     * true if the position is locked
     */
    private boolean positionLock = false;
    /**
     * the currently selected action
     */
    private transient Action selectedAction = null;

    /**
     * a counter for the newly created pump ids
     */
    private int cisternCreatePumpCounter = 1;
    /**
     * a counter for the newly created pipe ids
     */
    private int pipeCreatePipeCounter = 1;

    /**
     * Constructs the game menu
     *
     * @param controller the controller of the windows
     * @param size the size of the game menu
     */
    public GameMenu(Controller controller, Dimension size) {
        game.addGameEndedListener(e -> {
            if (Objects.requireNonNull(e.winners()) == Game.GameEndedEvent.Winners.PLUMBERS) {
                gameOverPanel = new GameOverPanel(size, "PlumbersWin.png");
            } else if (e.winners() == Game.GameEndedEvent.Winners.SABOTEURS) {
                gameOverPanel = new GameOverPanel(size, "SaboteursWin.png");
            }
            add(gameOverPanel);
        });

        setPreferredSize(size);
        setSize(size);

        gamePanel = new GamePanel(getSize());
        add(gamePanel);

        add(new BackgroundLabel(size));

        mapPanel = new MapPanel(getSize(), dragger);
        add(mapPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                game.load(controller.getGameInfo());
                mapPanel.load(game);
                start();
            }
        });

        setFocusable(true);
        setLayout(null);
        setBackground(GameColors.desertYellow);
    }

    /**
     * Starts the game and inits the components
     */
    private void start() {
        // Refactor this method to reduce its Cognitive Complexity from 25 to the 15 allowed.
        if (gameOverPanel != null) {
            remove(gameOverPanel);
        }
        showValidActions();
        gamePanel.setCharacter(game.getCurrentCharacter(), game.getCurrentCharacterName());

        Toolkit.getDefaultToolkit().addAWTEventListener(this::eventDispatched, AWTEvent.MOUSE_EVENT_MASK);

        Toolkit.getDefaultToolkit().addAWTEventListener(this::eventDispatched2, AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    /**
     * Processes the event when a mouse press occurs
     *
     * @param e the mousePressedEvent
     */
    public void processMousePress(MouseEvent e) {
        int x = (int) (e.getX() - dragger.getTransform().getTranslateX());
        int y = (int) (e.getY() - dragger.getTransform().getTranslateY());

        logger.info("Mouse pressed: x: %d, y: %d".formatted(x, y));
        if (selectedAction != null) {
            handleSelectedAction(x, y);
        } else {
            logger.warning("Selected action is null");
            dragger.mousePressed(e);
        }
    }

    private void handleSelectedAction(int x, int y) {
        View foundView = findView(x, y);

        if (foundView != null) {
            logger.fine("View found: %s".formatted(foundView));
            selectView(foundView);
        } else {
            logger.warning("No view found");
            actionFailed(false);
        }
        selectedAction = null;
    }

    private View findView(int x, int y) {
        View foundView = findActiveElementView(x, y);
        if (foundView == null) {
            logger.warning("Couldn't find active elements, searching for pipes...");
            foundView = findPipeView(x, y);
        }
        return foundView;
    }

    private View findActiveElementView(int x, int y) {
        var activeElementViews = ViewRepository.getFiltered(
                v -> v instanceof CisternView || v instanceof PumpView || v instanceof SpringView);

        for (var view : activeElementViews) {
            if (view.isInside(x, y)) {
                return view;
            }
        }
        return null;
    }

    private View findPipeView(int x, int y) {
        for (View view : ViewRepository.getFiltered(PipeView.class::isInstance)) {
            if (view.isInside(x, y)) {
                return view;
            }
        }
        return null;
    }

    /**
     * Selects a view for the action
     *
     * @param view the view
     */
    private void selectView(View view) {
        logger.info("Trying action on view: %s".formatted(view));
        if (view.select(selectedAction)) {
            actionSuccess();
        } else {
            actionFailed(false);
        }
    }

    /**
     * Shows the valid actions
     */
    private void showValidActions() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();

        List<GamePanel.MenuAction> actions = new ArrayList<>();
        actions.add(new GamePanel.MenuAction("Skip turn", this::stepGame));
        actions.add(new GamePanel.MenuAction("Move", this::doMoveAction));

        if (location instanceof Pipe) {
            actions.add(new GamePanel.MenuAction("Pierce Pipe", this::doPiercePipeAction));
            actions.add(new GamePanel.MenuAction("Apply Technokol", this::doTechnokolPipeAction));
        } else if (location instanceof Pump) {
            actions.add(new GamePanel.MenuAction("Set Input Pipe", this::doSetPumpInputAction));
            actions.add(new GamePanel.MenuAction("Set Output Pipe", this::doSetPumpOutputAction));
        }

        if (currentCharacter instanceof Plumber) {
            if (location instanceof ActiveElement) {
                actions.add(new GamePanel.MenuAction("Attach Pipe", this::doAttachPipeAction));
                actions.add(new GamePanel.MenuAction("Detach Pipe", this::doDetachPipeAction));
            }

            if (location instanceof Cistern) {
                actions.add(new GamePanel.MenuAction("Pick up Pump", this::doPickUpPumpAction));
            } else if (location instanceof Pipe) {
                actions.add(new GamePanel.MenuAction("Patch Pipe", this::doRepairPipeAction));
                actions.add(new GamePanel.MenuAction("Place Pump", this::doPlacePumpAction));
            } else if (location instanceof Pump) {
                actions.add(new GamePanel.MenuAction("Repair Pump", this::doRepairPumpAction));
            }
        } else if (currentCharacter instanceof Saboteur && location instanceof Pipe) {
                actions.add(new GamePanel.MenuAction("Drop Banana", this::doBananaPipeAction));
            }


        gamePanel.setActionList(actions);
    }

    /**
     * Steps the game
     */
    private void stepGame() {
        game.stepGame();
        showValidActions();
        gamePanel.setNotification("");
        gamePanel.setScore(game.getPlumberScore(), game.getSaboteurScore());
        gamePanel.setRound(game.getCurrentRound());
        gamePanel.setCharacter(game.getCurrentCharacter(), game.getCurrentCharacterName());
    }

    /**
     * Handles the success of an action
     */
    private void actionSuccess() {
        logger.info("Action success");
        stepGame();
    }

    /**
     * Handles the failure of an action
     */
    private void actionFailed(boolean fatal) {
        gamePanel.setNotification((fatal ? "FATAL: " : "") + "Couldn't execute action!");
    }

    public void doMoveAction() {
        Character currentCharacter = game.getCurrentCharacter();
        selectedAction = new MoveAction(currentCharacter);
        gamePanel.setNotification("Select an element to move to");
    }

    public void doSetPumpInputAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (location instanceof Pump p) {
            selectedAction = new SetPumpInputAction(currentCharacter, p);
            gamePanel.setNotification("Select a pipe to use as input pipe");
        } else {
            actionFailed(true);
        }
    }

    public void doSetPumpOutputAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (location instanceof Pump p) {
            selectedAction = new SetPumpOutputAction(currentCharacter, p);
            gamePanel.setNotification("Select a pipe to use as output pipe");
        } else {
            actionFailed(true);
        }
    }

    public void doRepairPumpAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof Pump pump) {
            if (p.repair(pump)) {
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doPickUpPumpAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof Cistern c) {
            ModelObjectFactory.setCisternCreatePumpName("cisternCreatedPump" + cisternCreatePumpCounter);
            if (p.pickUpPump(c)) {
                cisternCreatePumpCounter++;
                logger.info("Pick up Pump success");
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doAttachPipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof ActiveElement ae) {
            Pipe heldPipe = p.getHeldPipe();
            if (p.attachPipe(ae)) {
                var attachedPipeView = (PipeView) ViewRepository.getViewOfObject(heldPipe);
                attachedPipeView.replaceEndpointPos(
                        ViewRepository.getViewOfObject(p).getPosition(),
                        ViewRepository.getViewOfObject(ae).getPosition()
                );
                logger.info("Attach pipe success");
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doDetachPipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof ActiveElement ae) {
            selectedAction = new DetachPipeAction(p, ae);
            gamePanel.setNotification("Select a pipe to detach");
        } else {
            actionFailed(true);
        }
    }

    public void doPiercePipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (location instanceof Pipe p) {
            if (currentCharacter.pierce(p)) {
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doRepairPipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof Pipe pipe) {
            if (p.repair(pipe)) {
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doPlacePumpAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Plumber p && location instanceof Pipe pipe) {
            var pumpToBePlaced = p.getHeldPump();
            var originalPipeView = (PipeView) ViewRepository.getViewOfObject(pipe);
            var endpointToBeDetachedView = ViewRepository.getViewOfObject(pipe.getEndpoint(0));

            var nameOfPipeToBeCreated = "pipeCreatedPipe" + pipeCreatePipeCounter;
            ModelObjectFactory.setPipeCreatePipeName(nameOfPipeToBeCreated);

            if (p.placePump(pipe)) {
                // create view for the placed pump
                var pumpView = new PumpView(new Position(ViewRepository.getViewOfObject(p).getPosition()), pumpToBePlaced);
                originalPipeView.replaceEndpointPos(endpointToBeDetachedView.getPosition(), pumpView.getPosition());
                ViewRepository.add(pumpToBePlaced, pumpView);

                // create view for the new pipe
                var createdPipe = (Pipe) ModelObjectFactory.getObject(nameOfPipeToBeCreated);
                var pipeView = new PipeView(createdPipe,
                        new Position[]{ pumpView.getPosition(), endpointToBeDetachedView.getPosition() });
                ViewRepository.add(createdPipe, pipeView);

                pipeCreatePipeCounter++;
                logger.info("Place Pump success");
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doBananaPipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (currentCharacter instanceof Saboteur s && location instanceof Pipe p) {
            if (s.dropBanana(p)) {
                var createdEffect = (BananaEffect) p.getEffect();
                ViewRepository.add(createdEffect, new BananaEffectView(
                        ViewRepository.getViewOfObject(p).getPosition(), createdEffect));
                logger.info("Banana Pipe success");
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    public void doTechnokolPipeAction() {
        Character currentCharacter = game.getCurrentCharacter();
        Element location = currentCharacter.getLocation();
        if (location instanceof Pipe p) {
            if (currentCharacter.applyTechnokol(p)) {
                var createdEffect = (TechnokolEffect) p.getEffect();
                ViewRepository.add(createdEffect, new TechnokolEffectView(
                        ViewRepository.getViewOfObject(p).getPosition(), createdEffect));
                logger.info("Technokol Pipe success");
                actionSuccess();
            } else {
                actionFailed(false);
            }
        } else {
            actionFailed(true);
        }
    }

    private void eventDispatched(AWTEvent event) {
        if (event instanceof MouseEvent evt) {
            if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
                if (selectedAction != null) {
                    positionLock = true;
                }
                processMousePress(evt);
            } else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
                positionLock = false;
            }
        }
    }

    private void eventDispatched2(AWTEvent event) {
        if (event instanceof MouseEvent evt) {
            if (evt.getID() == MouseEvent.MOUSE_DRAGGED) {
                if (!positionLock) dragger.mouseDragged(evt);
            } else if (evt.getID() == MouseEvent.MOUSE_MOVED && !positionLock) dragger.mouseMoved(evt);

        }
    }
}
