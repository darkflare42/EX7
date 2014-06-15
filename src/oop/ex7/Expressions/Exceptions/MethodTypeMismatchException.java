package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for a method is called with a variable of a wrong type.
 */
public class MethodTypeMismatchException extends CompilationException {
    public MethodTypeMismatchException() {
        super("A method was called with a variable of a wrong type");
    }
}
