package oop.ex7;

/**
 * Exception for when trying to apply an operation on 2 expressions that can't be operated on, using that operation.
 */
public class OperationMismatchException extends Exception {
    public OperationMismatchException () {
        super("Operation attempted between 2 types that can't be operated together");
    }
}
