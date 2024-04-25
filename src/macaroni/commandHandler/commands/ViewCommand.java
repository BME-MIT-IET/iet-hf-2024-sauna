package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.utils.ModelObjectSerializer;
import macaroni.commandHandler.CommandInterpreter;

import java.util.Objects;

public final class ViewCommand extends Command {

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
            System.out.println("! object not found");
            return;
        }

        if (args.length < 3) {
            System.out.println(ModelObjectSerializer.serializeToString(object));
        } else {
            String message = ModelObjectSerializer.serializeToString(object, args[2]);
            System.out.println(Objects.requireNonNullElse(message, "! attribute not found"));
        }
    }
}
