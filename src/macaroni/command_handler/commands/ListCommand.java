package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.model.character.Plumber;
import macaroni.model.character.Saboteur;
import macaroni.model.element.Cistern;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;
import macaroni.model.element.Spring;
import macaroni.model.misc.WaterCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public final class ListCommand extends Command {

    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    /**
     * HashMap containing the names of the different types of objects in separate lists.
     */
    private final HashMap<Class<?>, ArrayList<String>> lists = new HashMap<>();

    /**
     * Constructor.
     */
    public ListCommand() {
        super("list");
        description = """
                List
                - Writes out the names of existing objects.""";

        lists.put(Pump.class, new ArrayList<>());
        lists.put(WaterCollector.class, new ArrayList<>());
        lists.put(Pipe.class, new ArrayList<>());
        lists.put(Cistern.class, new ArrayList<>());
        lists.put(Spring.class, new ArrayList<>());
        lists.put(Plumber.class, new ArrayList<>());
        lists.put(Saboteur.class, new ArrayList<>());
    }

    /**
     * Executes the List command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        loadInLists();
        printLists();
    }

    /**
     * Loads in the object names into the lists.
     */
    private void loadInLists() {
        Set<String> set = ModelObjectFactory.getObjectNameList();
        for (Class<?> c : lists.keySet()) {
            lists.get(c).clear();
        }
        for (String name : set) {
            Object object = ModelObjectFactory.getObject(name);
            lists.get(object.getClass()).add(name);
        }
    }

    /**
     * Prints out the object names in separate lines, by type.
     */
    private void printLists() {
        for (Map.Entry<Class<?>, ArrayList<String>> entry : lists.entrySet()) {
            if (entry.getValue().size() == 0) {
                continue;
            }
            logger.info(entry.getKey().getSimpleName() + ": " + String.join(", ", entry.getValue()));
        }
    }
}
