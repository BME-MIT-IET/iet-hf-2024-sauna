package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.element.Cistern;
import macaroni.model.character.Plumber;

import java.util.ArrayList;

public final class GetPumpCommand extends Command {

    /**
     * Constructor.
     */
    public GetPumpCommand() {
        super("getpump");
        description = """
                GetPump <plumber: Plumber> <cistern: Cistern> <pumpName: string>
                - The given plumber picks up a pump from the given cistern if it is possible.""";
    }

    /**
     * Executes the GetPump command.
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
            ModelObjectFactory.setCisternCreatePumpName(args[3]);
            ((Plumber) objects.get(0)).pickUpPump((Cistern) objects.get(1));
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
