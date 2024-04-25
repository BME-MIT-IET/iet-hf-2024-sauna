package macaroni.commandHandler.commands;

import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.character.Character;
import macaroni.model.element.Pipe;

import java.util.ArrayList;

public final class PierceCommand extends Command {

    /**
     * Constructor.
     */
    public PierceCommand() {
        super("pierce");
        description = """
                Pierce <character: Character> <pipe: Pipe>
                - The given character pierces the given pipe if it is possible.""";
    }

    /**
     * Executes the Pierce command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            CommandInterpreter.printMissingParameter();
            return;
        }

        ArrayList<Object> objects = fetchObjects(args, 1, 2);
        if (objects == null) return;

        try {
            ((Character) objects.get(0)).pierce((Pipe) objects.get(1));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
