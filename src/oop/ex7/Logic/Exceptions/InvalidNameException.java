package oop.ex7.Logic.Exceptions;

/**
 * Exception for when a method or a variable are declared with an invalid name.
 */
public class InvalidNameException extends CompilationException {
    public InvalidNameException () {
        super("Name for a variable or a method was invalid.");
    }
}
