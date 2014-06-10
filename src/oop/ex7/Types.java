package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public enum Types {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double");

    private final String nameString;

    Types (String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }
}
