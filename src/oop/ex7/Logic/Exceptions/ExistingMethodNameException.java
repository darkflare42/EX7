package oop.ex7.Logic.Exceptions;

/**
 * Exception for when a method is declared with a name of an already existing method.
 */
public class ExistingMethodNameException extends CompilationException {
    public ExistingMethodNameException () {
        super("Method was declared with a name of an already existing method.");
    }
}
