package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.utils.ModelObjectSerializer;
import macaroni.commandHandler.CommandInterpreter;

import java.io.File;
import java.util.logging.Logger;

public final class LoadCommand extends Command {

    private static final Logger logger = Logger.getLogger(LoadCommand.class.getName());

    /**
     * Constructor.
     */
    public LoadCommand() {
        super("load");
        description = """
                Load <path: string>
                - Loads in the config file from the given path.
                - Objects created before Load will be deleted.""";
    }

    /**
     * Executes the Load command.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            CommandInterpreter.printMissingParameter();
            return;
        }

        StringBuilder path = new StringBuilder(args[1]);

        for (int i = 2; i < args.length; i++) {
            path.append(" ").append(args[i]);
        }

        File f = new File(path.toString());
        if (!f.exists() || f.isDirectory()) {
            logger.warning("File not found: " + path.toString());
            return;
        }

        // delete previous objects
        ModelObjectFactory.reset();

        try {
            ModelObjectSerializer.deserializeFromFile(f);
            logger.info("Load successful");
        } catch (Exception e) {
            logger.severe("Failed to load file: " + e.getMessage());
            ModelObjectFactory.reset();
        }
    }
}
