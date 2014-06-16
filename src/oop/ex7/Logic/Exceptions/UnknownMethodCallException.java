package oop.ex7.Logic.Exceptions;

/**
 * Exception for when an unknown method is invoked.
 */
public class UnknownMethodCallException extends CompilationException {
    public UnknownMethodCallException () {
        super("An unknown method was called.");
    }
}
