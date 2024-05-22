package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.command_handler.CommandInterpreter;
import macaroni.model.element.Element;

import java.util.Set;

public final class TickCommand extends Command {

    /**
     * Constructor.
     */
    public TickCommand() {
        super("tick");
        description = """
                Tick [element: Element]
                - Ticks the given element.
                - If no element was given, ticks all elements.""";
    }

    /**
     * Executes the Tick command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            tickOne(args[1]);
        } else {
            tickAll();
        }
    }

    /**
     * Ticks the given element.
     * If no element found, notify user.
     *
     * @param arg1 the name of the element
     */
    private void tickOne(String arg1) {
        Object object = ModelObjectFactory.getObject(arg1);
        if (object == null) {
            CommandInterpreter.printInvalidArgument();
            return;
        }
        try {
            ((Element) object).tick();
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Ticks all existing elements.
     */
    private void tickAll() {
        Set<String> names = ModelObjectFactory.getObjectNameList();
        for (String name : names) {
            try {
                Element element = (Element) ModelObjectFactory.getObject(name);
                element.tick();
            } catch (ClassCastException ignored) {
                // This block is empty on purpose
            }
        }
    }
}
