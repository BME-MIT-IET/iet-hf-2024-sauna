package macaroni.command_handler.commands;

import macaroni.command_handler.CommandInterpreter;
import macaroni.model.character.Character;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;

import java.util.ArrayList;

public final class SetCommand extends Command {

    /**
     * Constructor.
     */
    public SetCommand() {
        super("set");
        description = """
                Set input <character: Character> <pump: Pump> <pipe: Pipe>
                Set output <character: Character> <pump: Pump> <pipe: Pipe>""";
    }

    /**
     * Executes the Set command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 5) {
            CommandInterpreter.printMissingParameter();
            return;
        }

        ArrayList<Object> objects = fetchObjects(args, 2, 4);
        if (objects == null) return;

        try {
            switch (args[1]) {
                case "input" -> ((Character) objects.get(0)).setInputPipe((Pump) objects.get(1), (Pipe) objects.get(2));
                case "output" -> ((Character) objects.get(0)).setOutputPipe((Pump) objects.get(1), (Pipe) objects.get(2));
                default -> CommandInterpreter.printUnknownCommand();
            }
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
