package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.element.Pump;

public final class BreakCommand extends Command {

    /**
     * Constructor.
     */
    public BreakCommand() {
        super("break");
        description = """
                Break <pump: Pump>
                - Breaks the given pump.""";
    }

    /**
     * Executes the Break command.
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
            CommandInterpreter.printInvalidArgument();
            return;
        }
        try {
            ((Pump) object).Break();
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
