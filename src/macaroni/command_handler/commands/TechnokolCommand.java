package macaroni.command_handler.commands;

import macaroni.command_handler.CommandInterpreter;
import macaroni.model.character.Character;
import macaroni.model.element.Pipe;
import macaroni.utils.Random;

import java.util.ArrayList;

public final class TechnokolCommand extends Command {

    /**
     * Constructor.
     */
    public TechnokolCommand() {
        super("technokol");
        description = """
                Technokol <character: Character> <pipe: Pipe> [technokolTime: int]
                - The given character covers the given pipe in Technokol[TM] if it is possible.
                - If technokolTime is given, the outcome ttl is not random.""";
    }

    /**
     * Executes the Technokol command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 3){
            CommandInterpreter.printMissingParameter();
            return;
        }

        ArrayList<Object> objects = fetchObjects(args, 1, 2);
        if (objects == null) return;

        try {
            Character character = (Character) objects.get(0);
            Pipe pipe = (Pipe) objects.get(1);
            if (args.length >= 4) {
                Random.setDeterministicValue(Integer.parseInt(args[3]));
            }
            character.applyTechnokol(pipe);
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
