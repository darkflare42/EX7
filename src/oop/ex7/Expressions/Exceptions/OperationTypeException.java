package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when trying to resolve an operation that does not exist
 */
public class OperationTypeException extends CompilationException {
    public OperationTypeException() {
        super("Type of Operation does not exist");
    }
}
