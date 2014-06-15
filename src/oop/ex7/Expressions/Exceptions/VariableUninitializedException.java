package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when a variable or an array are attempted to be accessed, as an assignment or in an operation before initialized.
 */
public class VariableUninitializedException extends CompilationException {
    public VariableUninitializedException() {
        super("Variable or array were accessed before initialized");
    }
}
