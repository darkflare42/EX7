package oop.ex7.Logic.Exceptions;

/**
 * Created by Oded on 15/6/2014.
 */
public class InvalidArrayMembersDeclaration extends CompilationException{
    public InvalidArrayMembersDeclaration() {
        super("Array members declaration are invalid.");
    }
}
