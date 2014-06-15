package oop.ex7;

/**
 * Exception for when a method is called with an invalid amount of Expressions.
 */
public class MethodBadArgsCountException extends Exception {
    public MethodBadArgsCountException () {
        super("Attempted to call a method with an invalid amount of Expressions");
    }
}
