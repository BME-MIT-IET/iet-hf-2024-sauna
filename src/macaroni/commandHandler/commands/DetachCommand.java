package macaroni.commandHandler.commands;

import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.element.ActiveElement;
import macaroni.model.element.Pipe;
import macaroni.model.character.Plumber;

import java.util.ArrayList;

public final class DetachCommand extends Command {

    /**
     * Constructor.
     */
    public DetachCommand() {
        super("detach");
        description = """
                Detach <pipe: Pipe> <element: ActiveElement>
                - Detaches the given pipe from the given active element
                Detach <plumber: Plumber> <element: ActiveElement> <pipe: Pipe>
                - Instructs the given plumber to detach the given pipe from the active element""";
    }

    /**
     * Executes the Detach command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            CommandInterpreter.printMissingParameter();
        } else if (args.length < 4) {
            handleMapDetach(fetchObjects(args, 1, 2));
        } else {
            handlePlumberDetach(fetchObjects(args, 1, 3));
        }
    }

    /**
     * Detaches the pipe as the map.
     *
     * @param objects the objects of the command
     */
    private void handleMapDetach(ArrayList<Object> objects) {
        if (objects == null) return;

        try {
            ((ActiveElement) objects.get(1)).removePipe((Pipe) objects.get(0));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Detaches the pipe as the plumber.
     *
     * @param objects the objects of the command
     */
    private void handlePlumberDetach(ArrayList<Object> objects) {
        if (objects == null) return;

        try {
            ((Plumber) objects.get(0)).detachPipe((ActiveElement) objects.get(1), (Pipe) objects.get(2));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
