package pasta;

import pasta.asserts.Result;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * The entry point of the application
 * <p>
 * !!! Change working directory to proto .class !!!
 * - Basically if you are able to write "java macaroni.Main" you are good.
 */
public final class Main {
    /**
     * the extension of a config file
     */
    private static final String CONFIG_EXT = ".config.pasta";
    /**
     * the extension of a test file
     */
    private static final String TEST_EXT = ".pasta";
    /**
     * the tests to be run
     */
    private static final List<Test> tests = new ArrayList<>();
    /**
     * the directory for logging
     */
    private static File logsDir;
    /**
     * the directory for the tests' output
     */
    public static File outDir;
    /**
     * the map files for all the test cases
     */
    private static final ConcurrentMap<Path, Path> mapFiles = new ConcurrentHashMap<>();


    /**
     * Copies the given map file to the logs dir of the program.
     *
     * @param oldMapPath the path of the old map file
     * @param pathname   the path of the new map file
     */
    public static void addMapFile(Path oldMapPath, String pathname) {
        if (pathname.endsWith("/") || pathname.endsWith("\\")) {
            return;
        }

        String mapFileName = pathname.substring(Math.max(Math.max(pathname.lastIndexOf("/"), pathname.lastIndexOf("\\")), 0));
        mapFiles.put(oldMapPath, Path.of(logsDir.getAbsolutePath() + "/maps/" + mapFileName));
    }

    /**
     * Registers a test to be run later.
     * In case the test has been already registered, nothing happens.
     *
     * @param testFile the test file
     */
    private static void addTest(File testFile) {
        Test test;
        try {
            test = new Test(testFile);
        } catch (IOException ignored) {
            return;
        }
        if (!tests.contains(test)) {
            tests.add(test);
        }
    }

    /**
     * Processes (interprets) the given test config file.
     * <p>
     * The contained file paths will be transformed to absolute paths.
     * <p>
     * Nested config files are further processed, if their paths are also nested inside the original config's directory.
     * <p>
     * Lines starting with a '#' are marked as comments and not interpreted.
     * <p>
     * In case a file is not found, it will not be processed.
     *
     * @param pathname the path of the config file
     */
    public static void handleConfigFile(String pathname) {
        File configFile = new File(pathname);
        try {
            Scanner scanner = new Scanner(configFile);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                Path path = Paths.get(line);
                Path configPath = path.isAbsolute() ? path : Paths.get(configFile.getParent() + "\\" + line);

                if (configPath.endsWith(CONFIG_EXT)) {
                    if (configPath.getParent() != null &&
                            configPath.getParent().getParent() != null &&
                            configPath.getParent().getParent().startsWith(configFile.toPath().getParent())) {
                        handleConfigFile(configPath.toString());
                    }
                } else {
                    if (path.isAbsolute()) {
                        addTest(new File(line));
                    } else {
                        addTest(new File(configFile.getParent() + "\\" + line));
                    }
                }
            }
        } catch (FileNotFoundException ignored) {
        }
    }

    /**
     * The entry point of the application.
     *
     * @param args the test and config files that should be run
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }

        // the format of the freshly created log dir
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        // create the log and out dirs
        try {
            logsDir = new File(Files.createDirectories(Path.of(
                    Paths.get(args[0]).getParent()
                            + "/logs/"
                            + dtf.format(now))).toUri());
            outDir = new File(Files.createDirectories(Path.of(
                    logsDir.getAbsolutePath() + "/out")).toUri());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        // print some feedback to the user that the application has actually started its job
        System.out.println("Running...");

        // process all the files given as args
        for (String pathname : args) {
            if (pathname.endsWith(CONFIG_EXT)) {
                handleConfigFile(pathname);
            } else if (pathname.endsWith(TEST_EXT)) {
                addTest(new File(pathname));
            }
        }

        // run all the tests in parallel and print their names upon their finish
        tests.parallelStream().forEach(test -> {
            test.run();
            System.out.println("... " + test.name());
        });
        System.out.println();

        // collect the results of all the tests
        List<String> successfulTests = new ArrayList<>();
        Map<String, List<Result.Failure>> failedTests = new TreeMap<>();
        tests.forEach(test -> {
            if (test.assertionResults().stream().allMatch(result -> result instanceof Result.Success)) {
                successfulTests.add(test.name());
                return;
            }
            test.assertionResults().forEach(assertion -> {
                if (assertion instanceof Result.Failure) {
                    failedTests.merge(test.name(), List.of((Result.Failure) assertion), (oldValue, newValue) ->
                            Stream.concat(oldValue.stream(), newValue.stream()).toList());
                }
            });
        });
        // print the result of the tests both to file and the standard output
        printSUM(successfulTests, failedTests);

        // copy all the test files to the logs dir
        for (Test test : tests) {
            try {
                Files.copy(test.path(), Path.of(logsDir.toPath() + "/" + test.name()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) {
            }
        }

        // copy all the map files to the logs dir
        try {
            Files.createDirectory(Path.of(logsDir.toPath() + "/maps"));
        } catch (IOException e) {
            System.out.println("Failed creating maps directory");
            return;
        }
        mapFiles.forEach((oldPath, newPath) -> {
            try {
                Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println(newPath);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Prints the given results in a nice format to both file and the standard out.
     * In case the given parameters are empty, nothing happens.
     *
     * @param successfulTests the names of the successfully run test cases
     * @param failedTests     the names and failed results of the failed tests
     */
    private static void printSUM(List<String> successfulTests, Map<String, List<Result.Failure>> failedTests) {
        if (successfulTests.isEmpty() && failedTests.isEmpty()) {
            return;
        }

        // create the _SUM.pasta file that will contain the nicely formatted test results
        File file = null;
        try {
            file = Files.createFile(Path.of(logsDir.toPath() + "/_SUM.pasta")).toFile();
        } catch (IOException ignored) {
        }

        // print all the success messages
        if (!successfulTests.isEmpty()) {
            // if the _SUM.pasta file could be created, write the results to there too
            if (file != null) {
                try {
                    Files.writeString(file.toPath(), "=== PASSED ===\n", StandardOpenOption.APPEND);
                    System.out.println("=== PASSED ===");
                    for (String test : successfulTests) {
                        Files.writeString(file.toPath(), " |> " + test + "\n", StandardOpenOption.APPEND);
                        System.out.println(" |> " + test);
                    }
                } catch (IOException ignored) {
                }
            } else {
                // print only to standard out in case _SUM.pasta could not be created
                System.out.println("=== PASSED ===");
                for (String test : successfulTests) {
                    System.out.println(" |> " + test);
                }
            }
        }

        // print all messages of failure
        if (!failedTests.isEmpty()) {
            // if the _SUM.pasta file could be created, write the results to there too
            if (file != null) {
                try {
                    if (!successfulTests.isEmpty()) {
                        Files.writeString(file.toPath(), "\n\n", StandardOpenOption.APPEND);
                        System.out.println("\n");
                    }
                    Files.writeString(file.toPath(), "!== FAILED !==\n", StandardOpenOption.APPEND);
                    System.out.println("!== FAILED !==");
                    for (Map.Entry<String, List<Result.Failure>> entry : failedTests.entrySet()) {
                        Files.writeString(file.toPath(), " |> " + entry.getKey() + "\n", StandardOpenOption.APPEND);
                        System.out.println(" |> " + entry.getKey());
                        // print all the assertion errors in their desired format
                        for (Result.Failure assertion : entry.getValue()) {
                            Files.writeString(file.toPath(), assertion.message() + "\n", StandardOpenOption.APPEND);
                            System.out.println(assertion.message());
                        }
                        Files.writeString(file.toPath(), "\n", StandardOpenOption.APPEND);
                        System.out.println();
                    }
                } catch (IOException ignored) {
                }
            } else {
                // print only to standard out in case _SUM.pasta could not be created
                if (!successfulTests.isEmpty()) {
                    System.out.println("\n");
                }
                System.out.println("!== FAILED !==");
                for (Map.Entry<String, List<Result.Failure>> entry : failedTests.entrySet()) {
                    System.out.println(" |> " + entry.getKey());
                    // print all the assertion errors in their desired format
                    for (Result.Failure assertion : entry.getValue()) {
                        System.out.println(assertion.message());
                    }
                    System.out.println();
                }
            }
        }
    }
}
