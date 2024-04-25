package macaroni.commandHandler.commands;

import macaroni.commandHandler.CommandInterpreter;
import macaroni.model.element.Pipe;
import macaroni.model.character.Plumber;
import macaroni.model.element.Pump;
import macaroni.utils.Random;

import java.util.ArrayList;

public final class RepairCommand extends Command {

    /**
     * Constructor.
     */
    public RepairCommand() {
        super("repair");
        description = """
                Repair <plumber: Plumber> <pipe: Pipe> [invulnerability: int]
                - The given plumber repairs the given pipe if it is possible.
                - If invulnerability is given, its value will not be random.
                Repair <plumber: Plumber> <pump: Pump>
                - The given plumber repairs the given pump if it is possible.""";
    }

    /**
     * Executes the Repair command.
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
            Plumber plumber = (Plumber) objects.get(0);
            try {
                // Try interpreting second argument as pipe
                if (args.length >= 4) {
                    Random.setDeterministicValue(Integer.parseInt(args[3]));
                }
                plumber.repair((Pipe) objects.get(1));
            } catch (ClassCastException exception) {
                // Try interpreting second argument as pump
                plumber.repair((Pump) objects.get(1));
            }

        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
