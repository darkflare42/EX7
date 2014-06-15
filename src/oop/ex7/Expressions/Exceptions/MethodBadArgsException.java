package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when the Variables declaration in the header structure is invalid.
 */
public class MethodBadArgsException extends CompilationException {
    public MethodBadArgsException() {
        super("Declaration arguments structure is invalid");
    }
}
