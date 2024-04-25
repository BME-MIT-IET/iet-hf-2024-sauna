package pasta.asserts;

/**
 * Utility "enum" for holding the result of an assertion.
 * This concept is general enough that it could be used to represent the result of any function.
 * Use the {@link  pasta.asserts.Result.Success} and {@link  pasta.asserts.Result.Failure} records
 * for representing an outcome.
 * This concept was made with the new matching mechanisms (Java 18 (Preview) version) in mind.
 * It is safe to check for only the contained types below (and null).
 */
public sealed interface Result {

    /**
     * Represents a successful outcome.
     */
    record Success() implements Result {}

    /**
     * Represents a failed outcome.
     *
     * @param message the message containing extra information about the failure
     */
    record Failure(String message) implements Result {}
}
