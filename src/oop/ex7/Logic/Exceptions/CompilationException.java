package oop.ex7.Logic.Exceptions;

/**
 * Exception every compilation related exception should extend.
 */
public class CompilationException extends Exception {
    public CompilationException(){
        super();
    }
    public CompilationException (String message) {
        super(message);
    }
}
