package macaroni.commandHandler.commands;

import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.character.Character;
import macaroni.model.element.Element;

import java.util.ArrayList;

public final class MoveCommand extends Command {

    /**
     * Constructor.
     */
    public MoveCommand() {
        super("move");
        description = """
                Move <character: Character> <element: Element>
                - Moves the given character to the given element if it is possible.""";
    }

    /**
     * Executes the Move command.
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
            ((Character) objects.get(0)).moveTo((Element) objects.get(1));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
