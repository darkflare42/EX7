package oop.ex7;

/**
 * Exception for when a condition tries to treat a non-boolean type as a boolean.
 */
public class ConditionExpressionNotBooleanException extends Exception {
    public ConditionExpressionNotBooleanException () {
        super("Condition tried to treat a non-boolean type as a boolean");
    }
}
