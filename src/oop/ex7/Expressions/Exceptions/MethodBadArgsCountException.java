package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when a method is called with an invalid amount of Expressions.
 */
public class MethodBadArgsCountException extends CompilationException {
    public MethodBadArgsCountException () {
        super("Attempted to call a method with an invalid amount of Expressions");
    }
}
