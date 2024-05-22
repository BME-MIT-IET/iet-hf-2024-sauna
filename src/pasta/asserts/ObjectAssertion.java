package pasta.asserts;

import java.util.Objects;

/**
 * Static class that describes the rules of asserting an entity's existence.
 */
public final class ObjectAssertion {
    private ObjectAssertion() {}

    /**
     * Checks whether the given line is a well formatted assertion according to the class's rules.
     *
     * @param assertion the line that is supposed to contain the assertion's description
     * @return true if the line is well formatted for this kind of assertion
     */
    public static boolean wellFormatted(String assertion) {
        return assertion.startsWith("@") &&
                assertion.replaceFirst("!", "").substring(1).strip().length() > 1;
    }

    /**
     * Makes an assertion from the given arguments.
     *
     * @param assertion   the line that holds the assertion's description
     *                    this should be validated using {@link  pasta.asserts.ObjectAssertion#wellFormatted(String)}
     * @param lineCounter the number of the line holding the assertion
     * @param input       the lines that should be asserted
     * @return the result of the assertion
     */
    public static Result doAssertion(String assertion, int lineCounter, String[] input) {
        assertion = assertion.strip();

        if (assertion.substring(1).startsWith("!")) {
            if (Objects.equals(input[0].strip(), "! object not found")) {
                return new Result.Success();
            }
            return new Result.Failure(
                    "(line_" + lineCounter + ") " + assertion + "  =>  Object exists"
            );
        }

        if (Objects.equals(input[0].strip(), "! object not found")) {
            return new Result.Failure(
                    "(line_" + lineCounter + ") " + assertion + "  =>  Object does not exist"
            );
        }
        return new Result.Success();
    }
}
