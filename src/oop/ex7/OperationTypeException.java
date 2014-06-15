package oop.ex7;

/**
 * Exception for when trying to resolve an operation that does not exist
 */
public class OperationTypeException extends Exception {
    public OperationTypeException() {
        super("Type of Operation does not exist");
    }
}
