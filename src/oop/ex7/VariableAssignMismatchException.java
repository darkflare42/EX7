package oop.ex7;

/**
 * Exception for when trying to assign a value of the wrong type to a variable.
 */
public class VariableAssignMismatchException extends Exception {
    public VariableAssignMismatchException() {
        super("Attempted to assign a value of a type that mismatches the Variable type");
    }
}
