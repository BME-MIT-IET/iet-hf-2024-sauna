package macaroni.command_handler.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class HelpCommand extends Command {

    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    /**
     * The list of commands that will be printed out.
     */
    private final ArrayList<Command> commandList;

    /**
     * Constructor.
     *
     * @param commandList the list of commands that need to be displayed
     */
    public HelpCommand(ArrayList<Command> commandList) {
        super("help");
        description = """
                Help [command]
                - Writes out the list of available commands.
                - If command parameter is given and a command with given name exists, writes out its description.""";
        this.commandList = commandList;
    }

    /**
     * Executes the Help command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            listAllCommands();
        } else {
            listCommand(args[1]);
        }
    }

    /**
     * Lists out all available commands.
     */
    private void listAllCommands() {
        ArrayList<String> nameList = new ArrayList<>();
        for (Command command : commandList) {
            nameList.add(command.name);
        }
        logger.info("Available commands: " + String.join(", ", nameList));
    }

    /**
     * Lists out a specific command.
     * If no command exists with given name, lists out all commands.
     *
     * @param commandName the command that needs to be specified
     */
    private void listCommand(String commandName) {
        for (Command command : commandList) {
            if (command.name.equals(commandName)) {
                logger.info("Command description: " + command.description);
                return;
            }
        }
        listAllCommands();
    }
}
