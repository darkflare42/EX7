package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public enum OperationType {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String nameString;

    OperationType(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }
}
