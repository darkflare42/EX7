package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public enum VariableEnum {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double");

    private final String nameString;
    public static final String TYPES="(int|double|String|boolean|char)";

    VariableEnum(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }

    public static VariableEnum toEnum (String string) throws VariableTypeException {
        if (string.equals("int")) {
            return VariableEnum.INT;
        } else if (string.equals("String")) {
            return VariableEnum.STRING;
        } else if (string.equals("char")) {
            return VariableEnum.CHAR;
        } else if (string.equals("boolean")) {
            return VariableEnum.BOOLEAN;
        } else if (string.equals("double")) {
            return VariableEnum.DOUBLE;
        } else {
            throw new VariableTypeException();
        }
    }
}
