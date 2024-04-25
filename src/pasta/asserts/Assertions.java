package pasta.asserts;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class for making assertions.
 */
public final class Assertions {

    /**
     * Utility class that holds a pair of functions for making validated assertions.
     *
     * @param assertion     the lambda object of the assertion
     * @param wellFormatted the function for checking whether making such assertion is legal
     */
    private record Pair(Assertion assertion, Function<String, Boolean> wellFormatted) {}

    /**
     * A list of assertion functions and their validators.
     */
    private static final List<Pair> assertions = List.of(
            new Pair(AttributeAssertion::doAssertion, AttributeAssertion::wellFormatted),
            new Pair(ObjectAssertion::doAssertion, ObjectAssertion::wellFormatted)
    );

    /**
     * Attempts to make an assertion from the given arguments.
     *
     * @param line        the line describing the assertion
     * @param lineCounter the number of the line containing the assertion
     * @param input       the lines that should be asserted
     * @return the result of the assertion
     * @throws IllegalArgumentException if an assertion can't be made from the line
     */
    public static Result makeAssertion(String line, int lineCounter, String[] input) {
        for (var entry : assertions) {
            if (entry.wellFormatted.apply(line)) {
                return entry.assertion.doAssertion(line, lineCounter, input);
            }
        }

        throw new IllegalArgumentException(
                "Cannot make assertion from \"" + line + "\" (line_" + lineCounter + ")"
        );
    }
}
