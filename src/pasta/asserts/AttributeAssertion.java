package pasta.asserts;

/**
 * Static class that describes the rules of asserting a specific attribute of an object.
 */
public final class AttributeAssertion {

    /**
     * Checks whether the given line is a well formatted assertion according to the class's rules.
     *
     * @param assertion the line that is supposed to contain the assertion's description
     * @return true if the line is well formatted for this kind of assertion
     */
    public static boolean wellFormatted(String assertion) {
        String[] expected = assertion.split(" ");
        return expected.length == 3 && expected[0].startsWith("@") && expected[0].contains(".") && expected[1].equals("=");
    }

    /**
     * Makes an assertion from the given arguments.
     *
     * @param assertion   the line that holds the assertion's description
     *                    this should be validated using {@link  pasta.asserts.AttributeAssertion#wellFormatted(String)}
     * @param lineCounter the number of the line holding the assertion
     * @param input       the lines that should be asserted
     * @return the result of the assertion
     */
    public static Result doAssertion(String assertion, int lineCounter, String[] input) {
        String attributeName = assertion.split(" ")[0].substring(1).split("\\.")[1];
        String expectedValue = assertion.split(" ")[2];

        if (input[0].startsWith("!")) {
            return new Result.Failure("(line_" + lineCounter + ") " + assertion.split(" ")[0] + "  =>  Object not found");
        }

        for (String line : input) {
            String[] expected = line.trim().split(" ");
            if (expected.length == 3 && expected[0].equals(attributeName) && expected[1].equals("=")) {
                if (expected[2].equals(expectedValue)) {
                    return new Result.Success();
                } else {
                    return new Result.Failure(formatFailureMessage(assertion, lineCounter, expectedValue, expected[2]));
                }
            }
        }

        return new Result.Failure(
                "(line_" + lineCounter + ") " + assertion.split(" ")[0] + "  =>  Invalid attribute name");
    }

    /**
     * Formats a failure message from the given arguments.
     *
     * @param line          the line that holds the assertion's description
     * @param lineCounter   the number of the line holding the assertion
     * @param expectedValue the expected value of the attribute as stated in the assertion
     * @param realValue     the real value of the attribute that is being asserted
     * @return the failure message, formatted from the given arguments
     */
    private static String formatFailureMessage(String line, int lineCounter, String expectedValue, String realValue) {
        return "(line_" + lineCounter + ") " + line.split(" ")[0] + "  =>  Expected: " + expectedValue + ", Got: " + realValue;
    }
}
