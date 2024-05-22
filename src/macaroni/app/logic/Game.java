package macaroni.app.logic;

import macaroni.app.gameView.ViewRepository;
import macaroni.model.character.Character;
import macaroni.model.character.Plumber;
import macaroni.model.character.Saboteur;
import macaroni.model.element.*;
import macaroni.model.misc.WaterCollector;
import macaroni.utils.ModelObjectFactory;
import macaroni.utils.ModelObjectSerializer;
import macaroni.views.PipeView;
import macaroni.views.Position;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents the game and its logic
 */
public final class Game {

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    /**
     * Used with GameEndedListener
     *
     * @param winners the winning team
     */
    public record GameEndedEvent(Winners winners) {
        /**
         * For representing the winning team
         */
        public enum Winners {
            /**
             * the team of the plumbers
             */
            PLUMBERS,
            /**
             * the team of the saboteurs
             */
            SABOTEURS
        }
    }

    /**
     * Functional interface to be called as the game ends
     */
    public interface GameEndedListener {
        void onGameEnd(GameEndedEvent e);
    }


    /**
     * true if the game is running
     */
    private boolean running = false;
    /**
     * the number of the current round
     */
    private int currentRoundNumber = 0;
    /**
     * the maximum amount of rounds
     */
    public static final int maximumNumberOfRounds = 15;
    /**
     * the number of the current turn
     */
    private int currentTurnNumber = 0;
    /**
     * the maximum amount of turns in a round
     */
    private int maximumNumberOfTurns;
    /**
     * listeners to be called when the game ends
     */
    private final List<GameEndedListener> gameEndedListeners = new ArrayList<>();

    /**
     * maps the characters to their playerNames
     */
    private final Map<Character, String> characterNames = new HashMap<>();
    /**
     * the plumbers of the game
     */
    private final List<Plumber> plumbers = new ArrayList<>();
    /**
     * the saboteurs of the game
     */
    private final List<Saboteur> saboteurs = new ArrayList<>();

    /**
     * the collector of the plumbers
     */
    private WaterCollector plumberCollector = null;
    /**
     * the collector of the saboteurs
     */
    private WaterCollector saboteurCollector = null;

    /**
     * the id of the next pipe to be spawned by a cistern
     */
    private int cisternSpawnPipeCounter = 1;

    /**
     * an instance of the Random utility class
     */
    private final Random random = new Random();


    /**
     * Loads the game from the filesystem and sets it up
     *
     * @param gameInfo the initial information for the setup
     * @return true if the load was successful
     */
    public boolean load(GameInfo gameInfo) {
        running = true;
        currentRoundNumber = 0;
        currentTurnNumber = 0;
        maximumNumberOfTurns = gameInfo.plumberNicknames().length + gameInfo.saboteurNicknames().length;

        ModelObjectFactory.reset();
        try {
            ModelObjectSerializer.deserializeFromFile(gameInfo.mapFile());
        } catch (Exception e) {
            e.printStackTrace();
            ModelObjectFactory.reset();
            return false;
        }

        // find plumber and saboteur water collectors
        var plumberCollector = ModelObjectFactory.getObject("plumberCollector");
        if (plumberCollector instanceof WaterCollector pwc) {
            this.plumberCollector = pwc;
        } else {
            logger.severe("Map file doesn't contain a water collector named plumberCollector!");
            return false;
        }

        var saboteurCollector = ModelObjectFactory.getObject("saboteurCollector");
        if (saboteurCollector instanceof WaterCollector swc) {
            this.saboteurCollector = swc;
        } else {
            logger.severe("Map file doesn't contain a water collector named saboteurCollector!");
            return false;
        }

        ModelObjectFactory.setCisternCreatePipeGround(this.saboteurCollector);

        // get randomized list of active elements
        var activeElements = ModelObjectFactory.getRandomizedObjectList(ActiveElement.class);

        // return if there are not at least 2 active elements
        if (activeElements.size() < 2) {
            logger.severe("Map file has less than 2 active elements!");
            return false;
        }

        int index = 0;

        // create characters, place them on random locations
        for (String name : gameInfo.plumberNicknames()) {
            if (index == activeElements.size()) {
                index = 0;
            }
            var p = new Plumber(activeElements.get(index++));
            plumbers.add(p);
            characterNames.put(p, name);

        }
        for (String name : gameInfo.saboteurNicknames()) {
            if (index == activeElements.size()) {
                index = 0;
            }
            var s = new Saboteur(activeElements.get(index++));
            saboteurs.add(s);
            characterNames.put(s, name);
        }

        return true;
    }

    /**
     * Steps the game's turns by one.
     * Updates its state accordingly.
     */
    public void stepGame() {
        if (running) {
            randomBreakPump();
            randomSpawnPipe();

            // tick all elements
            for (var element : ModelObjectFactory.getObjectList(Element.class)) {
                element.tick();
            }

            currentTurnNumber++;
            if (currentTurnNumber >= maximumNumberOfTurns) {
                currentTurnNumber = 0;
                currentRoundNumber++;
                if (currentRoundNumber >= maximumNumberOfRounds) {
                    running = false;
                    var e = new GameEndedEvent(
                            plumberCollector.getStoredAmount() >= saboteurCollector.getStoredAmount()
                                    ? GameEndedEvent.Winners.PLUMBERS
                                    : GameEndedEvent.Winners.SABOTEURS
                    );
                    for (var listener : gameEndedListeners) {
                        listener.onGameEnd(e);
                    }
                }
            }
        }
    }

    /**
     * Randomly breaks a pump.
     */
    private void randomBreakPump() {
        // if this is the end of this round, break a random pump
        if (currentTurnNumber == maximumNumberOfTurns - 1) {
            for (var pump : ModelObjectFactory.getRandomizedObjectList(Pump.class)) {
                if (pump.Break()) {
                    break;
                }
            }
        }
    }

    /**
     * Randomly spawns a pipe on a cistern.
     */
    private void randomSpawnPipe() {
        for (var cistern : ModelObjectFactory.getRandomizedObjectList(Cistern.class)) {
            if (random.nextInt(8) == 7) {
                Position cisternPosition = ViewRepository.getViewOfObject(cistern).getPosition();
                String nameOfPipe = "cisternSpawnedPipe" + cisternSpawnPipeCounter++;
                ModelObjectFactory.setCisternCreatePipeName(nameOfPipe);
                cistern.spawnPipe();
                var pipe = (Pipe) ModelObjectFactory.getObject(nameOfPipe);

                double randomDirection = random.nextDouble(0, 2 * Math.PI);
                logger.info("Random direction: " + randomDirection);
                int x = (int) (cisternPosition.x() + Math.cos(randomDirection) * 120.0);
                int y = (int) (cisternPosition.y() + Math.sin(randomDirection) * 120.0);

                logger.info("x: " + x + " y: " + y);

                // create view for new pipe
                var pipeView = new PipeView(pipe , new Position[] {cisternPosition, new Position(x, y)});
                pipeView.setNew(true);
                ViewRepository.add(pipe, pipeView);

                break;
            }
        }
    }

    /**
     * Gets the plumbers of the game.
     *
     * @return the plumbers of the game
     */
    public Collection<Plumber> getPlumbers() {
        return plumbers;
    }

    /**
     * Gets the saboteurs of the game.
     *
     * @return the saboteurs of the game
     */
    public Collection<Saboteur> getSaboteurs() {
        return saboteurs;
    }

    /**
     * Adds the game listener to be called on game over
     *
     * @param gameEndedListener the game listener
     */
    public void addGameEndedListener(GameEndedListener gameEndedListener) {
        gameEndedListeners.add(gameEndedListener);
    }

    /**
     * Gets the character of the current turn
     *
     * @return the character of the current turn
     */
    public Character getCurrentCharacter() {
        if (currentTurnNumber < saboteurs.size()) {
            return saboteurs.get(currentTurnNumber);
        } else {
            return plumbers.get(currentTurnNumber - saboteurs.size());
        }
    }

    /**
     * Gets the current character's name
     *
     * @return the current character's name
     */
    public String getCurrentCharacterName() {
        return characterNames.get(getCurrentCharacter());
    }

    /**
     * Gets the name of the character
     *
     * @param character the character
     * @return the name of the character
     */
    public String getCharacterName(Character character) {
        return characterNames.get(character);
    }

    /**
     * Gets the score of the plumbers
     *
     * @return the score of the plumbers
     */
    public int getPlumberScore() {
        return plumberCollector.getStoredAmount();
    }

    /**
     * Gets the score of the saboteurs
     *
     * @return the score of the saboteurs
     */
    public int getSaboteurScore() {
        return saboteurCollector.getStoredAmount();
    }

    /**
     * Gets the number of the current round.
     *
     * @return the number of the current round
     */
    public int getCurrentRound() {
        return currentRoundNumber;
    }
}
