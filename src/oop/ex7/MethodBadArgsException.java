package oop.ex7;

/**
 * Exception for when the Variables declaration in the header structure is invalid.
 */
public class MethodBadArgsException extends Exception {
    public MethodBadArgsException() {
        super("Declaration arguments structure is invalid");
    }
}
