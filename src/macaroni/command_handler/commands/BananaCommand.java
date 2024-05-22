package macaroni.command_handler.commands;

import macaroni.command_handler.CommandInterpreter;
import macaroni.model.element.Pipe;
import macaroni.model.character.Saboteur;
import macaroni.utils.Random;

import java.util.ArrayList;

public final class BananaCommand extends Command {

    /**
     * Constructor.
     */
    public BananaCommand() {
        super("banana");
        description = """
                Banana <saboteur: Saboteur> <pipe: Pipe> [bananaTime: int] [slideBack: boolean]
                - The given saboteur makes the given pipe slippery if it is possible.
                - If bananaTime is given, the outcome ttl is not random.
                - If slideBack is given, the slide direction is not random.""";
    }

    /**
     * Executes the Banana command.
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
            Saboteur saboteur = (Saboteur) objects.get(0);
            Pipe pipe = (Pipe) objects.get(1);
            if (args.length >= 4) {
                Random.setDeterministicValue(Integer.parseInt(args[3]));
            }
            if (args.length >= 5) {
                Random.setDeterministicSlideBack(pipe, Boolean.parseBoolean(args[4]));
            }
            saboteur.dropBanana(pipe);
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
