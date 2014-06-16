package oop.ex7.Logic.Exceptions;


/**
 * Exception for encountering an unfamiliar line in the sjava file.
 */
public class UnknownCodeLineException extends CompilationException {
    public UnknownCodeLineException() {
        super("Encountered a line that doesn't follow any known pattern.");
    }
}
