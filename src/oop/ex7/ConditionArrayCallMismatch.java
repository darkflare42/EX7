package oop.ex7;

/**
 * Exception for when a variable is called as an array, or vice versa.
 */
public class ConditionArrayCallMismatch extends Exception{
    public ConditionArrayCallMismatch () {
        super("Attempted to call a variable as an array or vice versa");
    }
}
