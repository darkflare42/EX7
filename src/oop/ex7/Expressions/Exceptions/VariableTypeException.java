package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when trying to resolve a Variable that is not supported.
 */
public class VariableTypeException extends CompilationException{
    public VariableTypeException() {
        super("Variable type is not recognized");
    }
}
