package macaroni.command_handler;

import macaroni.utils.ModelObjectFactory;
import macaroni.command_handler.commands.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public final class CommandInterpreter {

    private static final Logger logger = Logger.getLogger(CommandInterpreter.class.getName());

    /**
     * List containing the executable commands.
     */
    private final ArrayList<Command> commands = new ArrayList<>();

    /**
     * Constructor, initializes the commands.
     */
    public CommandInterpreter() {
        commands.add(new CreateCommand());
        commands.add(new AttachCommand());
        commands.add(new DetachCommand());
        commands.add(new ListCommand());
        commands.add(new ViewCommand());
        commands.add(new LoadCommand());
        commands.add(new SaveCommand());
        commands.add(new HelpCommand(commands));
        commands.add(new TickCommand());
        commands.add(new BreakCommand());
        commands.add(new SpawnPipeCommand());
        commands.add(new MoveCommand());
        commands.add(new SetCommand());
        commands.add(new PierceCommand());
        commands.add(new TechnokolCommand());
        commands.add(new BananaCommand());
        commands.add(new RepairCommand());
        commands.add(new GetPumpCommand());
        commands.add(new PlacePumpCommand());
    }

    /**
     * Takes in user input from System.in until EOF.
     */
    public void processInput() {
        logger.info("Starting command input processing");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] args = scanner.nextLine().split(" ");
            if (args[0].isEmpty()) {
                logger.info("Empty input, awaiting new input");
                System.out.print("> ");
                continue;
            }
            processCommand(args);
            System.out.print("> ");
        }
        scanner.close();
        logger.info("Command input processing finished");
    }

    /**
     * Executes the given command.
     * If no command matches the first argument, it notifies the user.
     *
     * @param args the arguments of the command
     */
    private void processCommand(String[] args) {
        logger.info("Processing command: %s".formatted(args[0]));
        for (Command command : commands) {
            if (command.getName().equals(args[0].toLowerCase())) {
                command.execute(args);
                logger.info("Command executed successfully: %s".formatted(args[0]));
                return;
            }
        }
        printUnknownCommand();
        logger.warning("Unknown command: %s".formatted(args[0]));
    }

    /**
     * Checks if the given name is valid and unique.
     * (Valid = contains english letters and numbers)
     *
     * @param nameToCheck the name to check
     * @return true if the name can be used to create a new object
     */
    public static boolean namingIsWrong(String nameToCheck) {
        if (!nameToCheck.matches("[a-zA-Z0-9]+")) {
            logger.warning("Invalid name format: %s".formatted(nameToCheck));
            return true;
        }
        if (ModelObjectFactory.getObject(nameToCheck) != null) {
            logger.warning("Duplicate object name: %s".formatted(nameToCheck));
            return true;
        }
        return false;
    }

    /**
     * Notifies the user that an invalid argument was given.
     */
    public static void printInvalidArgument() {
        logger.warning("Invalid argument found");
    }

    /**
     * Notifies the user that an unknown command was given.
     */
    public static void printUnknownCommand() {
        logger.warning("Unknown command");
    }

    /**
     * Notifies the user that a parameter is missing from the command arguments.
     */
    public static void printMissingParameter() {
        logger.warning("Missing parameter");
    }
}
