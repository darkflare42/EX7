package oop.ex7.Reader;

/**
 * Exception for when the reader encounters a problem reading from the given file.
 */
public class IOException extends Exception {
    public IOException () {
        super("There was a problem reading from the file given");
    }
}
