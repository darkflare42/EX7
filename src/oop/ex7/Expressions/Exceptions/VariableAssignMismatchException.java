package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when trying to assign a value of the wrong type to a variable.
 */
public class VariableAssignMismatchException extends CompilationException {
    public VariableAssignMismatchException() {
        super("Attempted to assign a value of a type that mismatches the Variable type");
    }
}
