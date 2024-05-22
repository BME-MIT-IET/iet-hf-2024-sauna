package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.command_handler.CommandInterpreter;
import macaroni.model.element.Cistern;
import macaroni.model.misc.WaterCollector;

import java.util.List;

public final class SpawnPipeCommand extends Command {

    /**
     * Constructor.
     */
    public SpawnPipeCommand() {
        super("spawnpipe");
        description = """
                SpawnPipe <cistern: Cistern> <ground: WaterCollector> <pipeName: string>
                - Spawns a pipe on the given cistern.""";
    }

    /**
     * Executes the SpawnPipe command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        List<Object> objects = fetchObjects(args, 1, 2);
        if (objects == null || CommandInterpreter.namingIsWrong(args[3])) {
            return;
        }
        try {
            Cistern cistern = (Cistern) objects.get(0);
            ModelObjectFactory.setCisternCreatePipeGround((WaterCollector) objects.get(1));
            ModelObjectFactory.setCisternCreatePipeName(args[3]);
            cistern.spawnPipe();
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
