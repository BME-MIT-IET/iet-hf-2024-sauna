package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.element.Pipe;
import macaroni.model.character.Plumber;

import java.util.ArrayList;

public final class PlacePumpCommand extends Command {

    /**
     * Constructor.
     */
    public PlacePumpCommand() {
        super("placepump");
        description = """
                PlacePump <plumber: Plumber> <pipe: Pipe> <newPipeName: string>
                - The given plumber places a pump to the given pipe if it is possible.""";
    }

    /**
     * Executes the PlacePump command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }

        ArrayList<Object> objects = fetchObjects(args, 1, 2);
        if (objects == null || CommandInterpreter.namingIsWrong(args[3])) return;

        try {
            ModelObjectFactory.setPipeCreatePipeName(args[3]);
            ((Plumber) objects.get(0)).placePump((Pipe) objects.get(1));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
