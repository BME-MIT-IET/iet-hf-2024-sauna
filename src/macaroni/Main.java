package macaroni;

import macaroni.app.App;
import macaroni.commandHandler.CommandInterpreter;

import java.util.Arrays;

public final class Main {
    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch(s -> s.equalsIgnoreCase("-cmd"))) {
            new CommandInterpreter().processInput();
        } else {
            new App().run();
        }
    }
}
