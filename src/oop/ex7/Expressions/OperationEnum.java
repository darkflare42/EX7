package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.OperationTypeException;

/**
 * Created by Oded on 10/6/2014.
 */
public enum OperationEnum {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String nameString;
    private static String OPERATIONS = null;


    OperationEnum(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }

    public static OperationEnum toEnum (String string) throws OperationTypeException {
        for (OperationEnum operation: OperationEnum.values()) {
            if (string.equals(operation.toString())) {
                return operation;
            }
        }
        throw new OperationTypeException();
    }

    public static String operationValues () {
        if (OPERATIONS != null) {
                return OPERATIONS;
        }
        OPERATIONS = "(";
        for (OperationEnum type : OperationEnum.values()) {
            OPERATIONS += type.toString() + "|";
        }
        OPERATIONS = OPERATIONS.substring(0, OPERATIONS.length()-1) + ")";
        return OPERATIONS;
    }

//    public static OperationEnum toEnum (String string) throws OperationTypeException{
//        if (string.equals("+")) {
//            return OperationEnum.ADD;
//        } else if (string.equals("-")) {
//            return OperationEnum.SUBTRACT;
//        } else if (string.equals("*")) {
//            return OperationEnum.MULTIPLY;
//        } else if (string.equals("/")) {
//            return OperationEnum.DIVIDE;
//        } else {
//            throw new OperationTypeException();
//        }
//    }
}
