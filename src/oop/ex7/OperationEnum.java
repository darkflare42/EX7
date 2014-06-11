package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
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

    public static OperationEnum toEnum (String string) throws OperationTypeException{
        if (string.equals("+")) {
            return OperationEnum.ADD;
        } else if (string.equals("-")) {
            return OperationEnum.SUBTRACT;
        } else if (string.equals("*")) {
            return OperationEnum.MULTIPLY;
        } else if (string.equals("/")) {
            return OperationEnum.DIVIDE;
        } else {
            throw new OperationTypeException();
        }
    }
}
