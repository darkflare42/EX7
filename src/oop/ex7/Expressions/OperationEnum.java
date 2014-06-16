package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.OperationTypeException;

/**
 * Enum for all recognized operation types.
 */
public enum OperationEnum {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String nameString;
    private static String OPERATIONS = null;

    /**
     * Private constructor.
     *
     * @param name String to be the String value for this enum.
     */
    OperationEnum(String name) {
        nameString = name;
    }

    /**
     * Return the String value of this enum.
     *
     * @return nameString
     */
    public String toString() {
        return nameString;
    }

    /**
     * Convert a String to enum.
     *
     * @param string String to convert.
     * @return OperationEnum corresponding to the String.
     * @throws OperationTypeException if string does not represent a valid operation.
     */
    public static OperationEnum toEnum(String string) throws OperationTypeException {
        for (OperationEnum operation : OperationEnum.values()) {
            if (string.equals(operation.toString())) {
                return operation;
            }
        }
        throw new OperationTypeException();
    }

    /**
     * Create upon first call, return a value on next calls.
     *
     * @return String of the regex set of all possible operations.
     */
    public static String operationValues() {
        if (OPERATIONS != null) {
            return OPERATIONS;
        }
        OPERATIONS = "(";
        for (OperationEnum type : OperationEnum.values()) {
            OPERATIONS += type.toString() + "|";
        }
        OPERATIONS = OPERATIONS.substring(0, OPERATIONS.length() - 1) + ")";
        return OPERATIONS;
    }
}