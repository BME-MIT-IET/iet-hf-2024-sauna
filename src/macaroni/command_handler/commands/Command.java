package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.command_handler.CommandInterpreter;

import java.util.ArrayList;

public abstract class Command {

    protected final String name;
    protected String description;

    /**
     * Constructor.
     *
     * @param name the name of the command
     */
    protected Command(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the command.
     *
     * @return the name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Executes the command.
     *
     * @param args the arguments of the command
     */
    public abstract void execute(String[] args);

    /**
     * Returns objects got by their names from the ModelObjectFactory.
     * If any of the given objects does not exist (no object found by name):
     * - it notifies the user (invalid argument)
     * - returns null.
     *
     * @param args  the command arguments (or array of names)
     * @param first the index of the first object name
     * @param last  the index of the last object name (inclusive!!!)
     * @return the list of got objects or null if any of the objects was null
     */
    protected ArrayList<Object> fetchObjects(String[] args, int first, int last) {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = first; i <= last; i++) {
            Object o = ModelObjectFactory.getObject(args[i]);
            if (o == null) {
                CommandInterpreter.printInvalidArgument();
                return null;
            }
            objects.add(o);
        }
        return objects;
    }
}
