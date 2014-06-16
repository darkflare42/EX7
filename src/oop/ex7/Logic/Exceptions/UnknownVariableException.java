package oop.ex7.Logic.Exceptions;

/**
 * Exception for an unknown variable (I.E. valid variable name) is called in an assignment or operation.
 */
public class UnknownVariableException extends CompilationException {
    public UnknownVariableException () {
        super("An unknown variable was called.");
    }
}
