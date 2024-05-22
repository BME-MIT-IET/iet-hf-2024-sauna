package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectSerializer;
import macaroni.command_handler.CommandInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public final class SaveCommand extends Command {

    private static final Logger logger = Logger.getLogger(SaveCommand.class.getName());

    /**
     * Constructor.
     */
    public SaveCommand() {
        super("save");
        description = """
                Save <path: string>
                - Saves the existing objects into a configuration file to the given path.""";
    }

    /**
     * Executes the Save command.
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
        if (f.exists()) {
            // delete file if already exists
            try {
                Files.delete(f.toPath());
            } catch (IOException e) {
                logger.warning("Cannot save file: File already exists and couldn't be deleted.");
            }
        }

        File folder = new File(f.getParent());

        if (!folder.exists()) {
            // create parent directories
            boolean success = folder.mkdirs();
            if (!success) {
                logger.warning("Cannot save file: Failed to create parent directories.");
                return;
            }
        }

        try {
            ModelObjectSerializer.serializeToFile(f);
            logger.info("Save successful");
        } catch (IOException e) {
            logger.severe("Failed to save file: " + e.getMessage());
        }
    }
}
