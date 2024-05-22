package pasta;

import pasta.asserts.Assertions;
import pasta.asserts.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

import macaroni.views.PipeView;

/**
 * Class for representing a test case
 */
public final class Test {
    private static final Logger logger = Logger.getLogger(Test.class.getName());
    /**
     * the file that describes the commands and assertions of the test
     */
    private final File testFile;
    /**
     * the file where the full output of the test will be dumped
     */
    private final File outputFile;
    /**
     * the results of the assertions described in the testFile
     */
    private final ArrayList<Result> assertionResults = new ArrayList<>();
    /**
     * the process in which the tested java application is run
     */
    private Process process;
    /**
     * the number of the current line
     */
    int lineCounter = 0;


    /**
     * Tries to construct a Test instance from the given argument.
     *
     * @param testFile the file that contains the test's description
     * @throws IOException if an output file cannot be created for the test case
     */
    public Test(File testFile) throws IOException {
        this.testFile = testFile;
        outputFile = new File(String.valueOf(Paths.get(Main.outDir.getAbsolutePath(), outfileName())));
        Files.createFile(outputFile.toPath());
    }

    /**
     * Runs this test.
     * This includes opening a new process for running the tested java application.
     * In case the running of the test cannot be initiated properly, the case itself won't be tested.
     */
    public void run() {
        // Initializes a process and redirects its error stream to its output stream
        ProcessBuilder processBuilder = new ProcessBuilder("java", "macaroni.Main", "-cmd");
        processBuilder.redirectErrorStream(true);

        // Starts the process and initializes the test's variables
        try {
            process = processBuilder.start();
            Files.writeString(outputFile.toPath(), readFromProcess());
            assertionResults.clear();
            lineCounter = 0;
        } catch (IOException e) {
            logger.warning(e.getMessage());
            return;
        }

        // The scanner used for reading from the test file
        Scanner scanner;
        try {
            scanner = new Scanner(testFile);
        } catch (FileNotFoundException ignored) {
            return;
        }

        // Reads and interprets all the lines from the test file, one-by-one
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            lineCounter++;
            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("@")) {
                handleAssert(line);
            } else {
                handleCommand(line);
            }
        }

        // Shuts down the process (of the tested application)
        endProcess();
    }

    /**
     * @return the name of the file that contains the test's description
     */
    public String name() {
        return testFile.getName();
    }

    /**
     * @return the path of the file that contains the test's description
     */
    public Path path() {
        return testFile.toPath();
    }

    /**
     * @return the results of the assertions
     * the returned List will be empty if the test has not been run
     */
    public List<Result> assertionResults() {
        return assertionResults;
    }

    /**
     * Checks whether two tests are equal.
     * Two tests are equal when their testFiles are equal.
     *
     * @param o the other test object
     * @return true if this and the given object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(testFile, test.testFile);
    }

    /**
     * @return the hash of this
     * the hash is made from the testFile
     */
    @Override
    public int hashCode() {
        return Objects.hash(testFile);
    }

    /**
     * Handles the case of an assertion.
     * <p></p>
     * A `View` command is fed to the tested application's standard input.
     * The assertions result is decided based on the output of the `View` command.
     *
     * @param line the assertion
     */
    private void handleAssert(String line) {
        String response = writeToProcess(
                "view " + String.join(" ", line.split(" ")[0].replaceFirst("!", "").substring(1).split("\\.")));
        Result assertionResult;
        try {
            assertionResult = Assertions.makeAssertion(line, lineCounter, response.split("\n"));
        } catch (IllegalArgumentException ignored) {
            return;
        }
        assertionResults.add(assertionResult);
    }

    /**
     * Handles the save command.
     * <p></p>
     * A `Save` command is fed to the tested application's standard input.
     * The parameter of the command will be appended to the testFile's directory if it is not an absolute path.
     *
     * @param command the command, which should contain the actual command and its arguments
     */
    private void handleSaveCommand(String[] command) {
        Path mapPath = Paths.get(command[1]);
        if (mapPath.isAbsolute()) {
            writeToProcess("save " + mapPath);
        } else {
            writeToProcess("save " + testFile.getParent() + "/" + command[1]);
        }
    }

    /**
     * Handles the load command.
     * <p></p>
     * A `Load` command is fed to the tested application's standard input.
     * The parameter of the command will be appended to the testFile's directory if it is not an absolute path.
     * The given map file (argument of the whole command) is copied to the outDir of the testing process.
     *
     * @param command the command
     *                this should contain the command itself followed by its arguments
     */
    private void handleLoadCommand(String[] command) {
        Path mapPath = Paths.get(command[1]);
        if (mapPath.isAbsolute()) {
            writeToProcess("load " + mapPath);
            Main.addMapFile(mapPath, command[1]);
        } else {
            writeToProcess("load " + testFile.getParent() + "/" + command[1]);
            Main.addMapFile(Path.of(testFile.getParent() + "/" + command[1]), command[1]);
        }
    }

    /**
     * Handles a general line of command from the test file.
     * <p></p>
     * The line will either be handed over to {@link  pasta.Test#handleSaveCommand(String[])},
     * {@link  pasta.Test#handleLoadCommand(String[])}
     * or processed on its own, meaning that it will be fed to the tested application's standard input.
     *
     * @param line the line that contains the command
     */
    private void handleCommand(String line) {
        String[] command = line.split(" ");
        if (command[0].equalsIgnoreCase("load")) {
            handleLoadCommand(command);
        } else if (command[0].equalsIgnoreCase("save")) {
            handleSaveCommand(command);
        } else {
            writeToProcess(line);
        }
    }

    /**
     * Reads all characters up to the first '>' from the application's output.
     * The '>' character and the one after it will also be appended to the function's returned value
     * <p></p>
     * In case the tested application throws an exception,
     * or it happens that a read character does not adhere to the UNICODE's standard,
     * a Runtime exception will be thrown.
     * Runtime Exception can also be thrown if an IOException occurs.
     *
     * @return the read string from the tested application's standard output
     * @throws RuntimeException in case of an error while reading
     */
    private String readFromProcess() {
        StringBuilder result = new StringBuilder();
        int character = 0;
        try {
            while (character != '>') {
                character = process.inputReader().read();
                try {
                    result.append(Character.toChars(character));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("\nREAD THE FOLLOWING FROM MAIN PROCESS:\n" + result, e);
                }
            }
            character = process.inputReader().read();
            result.append(Character.toChars(character));
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
        return result.toString();
    }

    /**
     * Feeds the message to the tested application.
     * Reads the response of the message (using {@linkplain Test#readFromProcess()}).
     * Logs both the message and its response to the outputFile.
     *
     * @param message the message to be fed to the tested app
     * @return the response of the tested app
     */
    private String writeToProcess(String message) {
        try {
            process.outputWriter().write(message + "\n");
            process.outputWriter().flush();
            Files.writeString(outputFile.toPath(), message + "\n", StandardOpenOption.APPEND);
            String response = readFromProcess();
            Files.writeString(outputFile.toPath(), response, StandardOpenOption.APPEND);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes, waits, and destroys the process
     */
    private void endProcess() {
        try {
            process.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        process.destroy();
    }

    /**
     * @return the name of the output file
     */
    private String outfileName() {
        String fileName = testFile.getName();
        return fileName.substring(0, fileName.lastIndexOf(".")) + ".out.pasta";
    }
}
