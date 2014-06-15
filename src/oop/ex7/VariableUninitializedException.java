package oop.ex7;

/**
 * Exception for when a variable or an array are attempted to be accessed, as an assignment or in an operation before initialized.
 */
public class VariableUninitializedException extends Exception{
    public VariableUninitializedException() {
        super("Variable or array were accessed before initialized");
    }
}
