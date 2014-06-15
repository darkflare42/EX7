package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when a variable is called as an array, or vice versa.
 */
public class ConditionArrayCallMismatch extends CompilationException{
    public ConditionArrayCallMismatch () {
        super("Attempted to call a variable as an array or vice versa");
    }
}
