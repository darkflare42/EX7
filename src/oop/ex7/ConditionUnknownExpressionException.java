package oop.ex7;

/**
 * Exception for when a Condition contains a reference to an expression that does not exist.
 */
public class ConditionUnknownExpressionException extends Exception {
    public ConditionUnknownExpressionException() {
        super("Condition contains a reference to an expression that does not exist");
    }
}
