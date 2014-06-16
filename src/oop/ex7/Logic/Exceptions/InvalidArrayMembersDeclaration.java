package oop.ex7.Logic.Exceptions;

/**
 * Exception for an invalid form of Array's member declaration.
 */
public class InvalidArrayMembersDeclaration extends CompilationException{
    public InvalidArrayMembersDeclaration() {
        super("Array members declaration is invalid.");
    }
}
