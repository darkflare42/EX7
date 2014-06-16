package oop.ex7.Logic.Exceptions;

/**
 * Exception for when A member is declared with a name of an already existing member.
 */
public class ExistingVariableName extends CompilationException {
    public ExistingVariableName () {
        super("A member was declared with a name of an already existing member");
    }
}
