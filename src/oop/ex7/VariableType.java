package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public enum VariableType {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double");

    private final String nameString;

    VariableType(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }
}
