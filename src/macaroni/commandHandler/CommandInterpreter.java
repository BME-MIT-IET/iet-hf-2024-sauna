package macaroni.commandHandler;

import macaroni.utils.ModelObjectFactory;
import macaroni.commandHandler.commands.*;

import java.util.ArrayList;
import java.util.Scanner;

public final class CommandInterpreter {

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
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] args = scanner.nextLine().split(" ");
            if (args[0].isEmpty()) {
                System.out.print("> ");
                continue;
            }
            processCommand(args);
            System.out.print("> ");
        }
        scanner.close();
    }

    /**
     * Executes the given command.
     * If no command matches the first argument, it notifies the user.
     *
     * @param args the arguments of the command
     */
    private void processCommand(String[] args) {
        for (Command command : commands) {
            if (command.getName().equals(args[0].toLowerCase())) {
                command.execute(args);
                return;
            }
        }
        printUnknownCommand();
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
            System.out.println("! name can only contain english letters and numbers");
            return true;
        }
        if (ModelObjectFactory.getObject(nameToCheck) != null) {
            System.out.println("! object with name already exists");
            return true;
        }
        return false;
    }

    /**
     * Notifies the user that an invalid argument was given.
     */
    public static void printInvalidArgument() {
        System.out.println("! invalid parameter found in command");
    }

    /**
     * Notifies the user that an unknown command was given.
     */
    public static void printUnknownCommand() {
        System.out.println("! unknown command, use ‘help’ to see the full list of commands");
    }

    /**
     * Notifies the user that a parameter is missing from the command arguments.
     */
    public static void printMissingParameter() {
        System.out.println("! missing parameter");
    }
}
