package macaroni.command_handler.commands;

import macaroni.utils.ModelObjectFactory;
import macaroni.command_handler.CommandInterpreter;
import macaroni.model.element.Element;
import macaroni.model.misc.WaterCollector;

import java.util.HashMap;

public final class CreateCommand extends Command {

    /**
     * Interface used to store create sub-command methods in map
     */
    private interface SubCommand {
        void call(String[] args);
    }

    private final HashMap<String, SubCommand> subCommands = new HashMap<>();

    /**
     * Constructor.
     */
    public CreateCommand() {
        super("create");
        description = """
                Create pump <name: string> [ports: int] [tankCapacity: int]
                - Creates a pump.
                Create collector <name: string>
                - Creates a water collector.
                Create pipe <name: string> <ground: WaterCollector> [capacity: int]
                - Creates a pipe.
                Create cistern <name: string> <collector: WaterCollector>
                - Creates a cistern.
                Create spring <name: string> [waterDispensedPerTick: int]
                - Creates a spring.
                Create plumber <name:string> <startLocation: Element>
                - Creates a plumber.
                Create saboteur <name:string> <startLocation: Element>
                - Creates a saboteur.""";

        subCommands.put("pump", this::createPump);
        subCommands.put("collector", this::createCollector);
        subCommands.put("pipe", this::createPipe);
        subCommands.put("cistern", this::createCistern);
        subCommands.put("spring", this::createSpring);
        subCommands.put("plumber", this::createPlumber);
        subCommands.put("saboteur", this::createSaboteur);
    }

    /**
     * Executes the Create command.
     * If an object with the given name exists or arguments are missing / invalid,
     * it notifies the user, and stops executing.
     *
     * @param args the arguments of the command
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        if (CommandInterpreter.namingIsWrong(args[2])) {
            return;
        }

        SubCommand methodToCall = subCommands.get(args[1]);
        if (methodToCall == null) {
            CommandInterpreter.printInvalidArgument();
            return;
        }
        methodToCall.call(args);
    }

    /**
     * Creates a pump with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createPump(String[] args) {
        try {
            if (args.length == 3) {
                ModelObjectFactory.createPump(args[2]);
            } else if (args.length == 4) {
                ModelObjectFactory.createPump(args[2], Integer.parseInt(args[3]));
            } else {
                ModelObjectFactory.createPump(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }
        } catch (NumberFormatException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Creates a water collector with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createCollector(String[] args) {
        ModelObjectFactory.createWaterCollector(args[2]);
    }

    /**
     * Creates a pipe with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createPipe(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        try {
            Object ground = ModelObjectFactory.getObject(args[3]);
            if (ground == null) {
                CommandInterpreter.printInvalidArgument();
                return;
            }
            if (args.length >= 5) {
                ModelObjectFactory.createPipe(args[2], (WaterCollector) ground, Integer.parseInt(args[4]));
            } else {
                ModelObjectFactory.createPipe(args[2], (WaterCollector) ground);
            }
        } catch (NumberFormatException | ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Creates a cistern with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createCistern(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        try {
            Object collector = ModelObjectFactory.getObject(args[3]);
            if (collector == null) {
                CommandInterpreter.printInvalidArgument();
                return;
            }
            ModelObjectFactory.createCistern(args[2], (WaterCollector) collector);
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Creates a spring with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createSpring(String[] args) {
        try {
            if (args.length >= 4) {
                ModelObjectFactory.createSpring(args[2], Integer.parseInt(args[3]));
            } else {
                ModelObjectFactory.createSpring(args[2]);
            }

        } catch (NumberFormatException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Creates a plumber with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createPlumber(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        try {
            Object element = ModelObjectFactory.getObject(args[3]);
            if (element == null) {
                CommandInterpreter.printInvalidArgument();
                return;
            }
            ModelObjectFactory.createPlumber(args[2], (Element) element);
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }

    /**
     * Creates a saboteur with the given command arguments.
     * If a parameter is incorrect, notifies the user, and cancels creation.
     *
     * @param args the arguments of the command
     */
    private void createSaboteur(String[] args) {
        if (args.length < 4) {
            CommandInterpreter.printMissingParameter();
            return;
        }
        try {
            Object element = ModelObjectFactory.getObject(args[3]);
            if (element == null) {
                CommandInterpreter.printInvalidArgument();
                return;
            }
            ModelObjectFactory.createSaboteur(args[2], (Element) element);
        } catch (ClassCastException exception) {
            CommandInterpreter.printInvalidArgument();
        }
    }
}
