package macaroni.command_handler.commands;

import macaroni.command_handler.CommandInterpreter;
import macaroni.model.element.ActiveElement;
import macaroni.model.element.Pipe;
import macaroni.model.character.Plumber;

import java.util.ArrayList;

public final class AttachCommand extends Command {

    /**
     * Constructor.
     */
    public AttachCommand() {
        super("attach");
        description = """
                Attach <pipe: Pipe> <element: ActiveElement>
                - Connects the given pipe to the given active element
                Attach <plumber: Plumber> <element: ActiveElement>
                - Instructs the given plumber to attach his held pipe to the active element""";
    }

    /**
     * Executes the Attach command.
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
            ActiveElement activeElement = (ActiveElement) objects.get(1);
            interpretingFirstArgument(activeElement, objects);

        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    private static void interpretingFirstArgument(ActiveElement activeElement, ArrayList<Object> objects) {
        try {
            // Try interpreting first argument as pipe
            activeElement.addPipe((Pipe) objects.get(0));
        } catch (ClassCastException exception) {
            // Try interpreting first argument as plumber
            ((Plumber) objects.get(0)).attachPipe(activeElement);
        }
    }
}
