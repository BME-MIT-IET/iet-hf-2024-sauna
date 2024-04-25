package macaroni.commandHandler.commands;

import macaroni.utils.ModelObjectSerializer;
import macaroni.commandHandler.CommandInterpreter;

import java.io.File;
import java.io.IOException;

public final class SaveCommand extends Command {

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
            boolean success = f.delete();
            if (!success) {
                System.out.println("! cannot save file");
                return;
            }
        }

        File folder = new File(f.getParent());

        if (!folder.exists()) {
            // create parent directories
            boolean success = folder.mkdirs();
            if (!success) {
                System.out.println("! cannot save file");
                return;
            }
        }

        try {
            ModelObjectSerializer.serializeToFile(f);
            System.out.println("save successful");
        } catch (IOException e) {
            System.out.println("! cannot save file");
        }
    }
}
