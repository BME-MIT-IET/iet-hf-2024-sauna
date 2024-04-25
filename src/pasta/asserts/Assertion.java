package pasta.asserts;

/**
 * Functional interface for making an assertion.
 */
public interface Assertion {

    /**
     * Makes an assertion from the given arguments.
     *
     * @param assertion   the line describing the assertion
     * @param lineCounter the number of the line containing the assertion
     * @param input       the lines that should be asserted
     * @return the result of the assertion
     */
    Result doAssertion(String assertion, int lineCounter, String[] input);
}
