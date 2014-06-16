package oop.ex7.Logic.Exceptions;

/**
 * Exception for an invalid form of member declaration.
 */
public class InvalidMemberDeclaration extends CompilationException {
    public InvalidMemberDeclaration () {
        super("Member declaration is invalid.");
    }
}
