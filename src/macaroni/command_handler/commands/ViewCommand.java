package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.utils.ModelObjectSerializer;
import macaroni.command_handler.CommandInterpreter;

import java.util.logging.Logger;

public final class ViewCommand extends Command {

    private static final Logger logger = Logger.getLogger(ViewCommand.class.getName());

    /**
     * Constructor.
     */
    public ViewCommand() {
        super("view");
        description = """
                View <pump: Pump> [attributeName: string]
                View <collector: WaterCollector> [attributeName: string]
                View <pipe: Pipe> [attributeName: string]
                View <cistern: Cistern> [attributeName: string]
                View <spring: Spring> [attributeName: string]
                View <plumber: Plumber> [attributeName: string]
                View <saboteur: Saboteur> [attributeName: string]
                - Lists the parameters of the given object or character.
                """;
    }

    /**
     * Executes the View command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            CommandInterpreter.printMissingParameter();
            return;
        }

        Object object = ModelObjectFactory.getObject(args[1]);
        if (object == null) {
            logger.warning(() -> "Object not found: " + args[1]);
            return;
        }

        if (args.length < 3) {
            logger.info(() -> ModelObjectSerializer.serializeToString(object));
        } else {
            String message = ModelObjectSerializer.serializeToString(object, args[2]);
            if (message != null) {
                logger.info(message);
            } else {
                logger.warning(() -> "Attribute not found: " + args[2]);
            }
        }
    }
}
