package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when a Condition contains a reference to an expression that does not exist.
 */
public class ConditionUnknownExpressionException extends CompilationException {
    public ConditionUnknownExpressionException() {
        super("Condition contains a reference to an expression that does not exist");
    }
}
