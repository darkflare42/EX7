package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when a condition tries to treat a non-boolean type as a boolean.
 */
public class ConditionExpressionNotBooleanException extends CompilationException {
    public ConditionExpressionNotBooleanException () {
        super("Condition tried to treat a non-boolean type as a boolean");
    }
}
