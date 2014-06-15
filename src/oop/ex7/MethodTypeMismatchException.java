package oop.ex7;

/**
 * Exception for a method is called with a variable of a wrong type.
 */
public class MethodTypeMismatchException extends Exception{
    public MethodTypeMismatchException() {
        super("A method was called with a variable of a wrong type");
    }
}
