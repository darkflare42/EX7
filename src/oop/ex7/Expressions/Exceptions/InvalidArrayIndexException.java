package oop.ex7.Expressions.Exceptions;

import oop.ex7.Logic.Exceptions.CompilationException;

/**
 * Exception for when an array is explicitly accessed in a negative index.
 */
public class InvalidArrayIndexException extends CompilationException {
    public InvalidArrayIndexException () {
        super("Array was explicitly accessed in a negative index");
    }
}
