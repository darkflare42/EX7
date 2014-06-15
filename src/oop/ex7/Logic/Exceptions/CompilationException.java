package oop.ex7.Logic.Exceptions;

/**
 * Created by Or Keren on 13/06/14.
 */
public class CompilationException extends Exception {
    public CompilationException(){
        super();
    }
    public CompilationException (String message) {
        super(message);
    }
}
