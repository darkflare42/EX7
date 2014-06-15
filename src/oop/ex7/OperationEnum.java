package oop.ex7;

/**
 * Enum for all the Operation types recognized.
 */
public enum OperationEnum {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String nameString;

    OperationEnum(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }

    /**
     * Convert a string to enum.
     * @param string String to convert.
     * @return OperationEnum according to the string.
     * @throws OperationTypeException if string is not a String of a valid operation.
     */
    public static OperationEnum toEnum (String string) throws OperationTypeException{
        for (OperationEnum operation: OperationEnum.values()) {
            if (string.equals(operation.toString())) {
                return operation;
            }
        }
        throw new OperationTypeException();
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
