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
//    public static final String TYPES="(int|double|String|boolean|char)";
    private static String TYPES = null;

    VariableEnum(String name) {
        nameString = name;
    }

    public String toString() {
        return nameString;
    }

    public static VariableEnum toEnum (String string) throws VariableTypeException {
        for (VariableEnum type: VariableEnum.values()) {
            if (string.equals(type.toString())) {
                return type;
            }
        }
        throw new VariableTypeException();
    }

//    public static VariableEnum toEnum (String string) throws VariableTypeException {
//        if (string.equals("int")) {
//            return VariableEnum.INT;
//        } else if (string.equals("String")) {
//            return VariableEnum.STRING;
//        } else if (string.equals("char")) {
//            return VariableEnum.CHAR;
//        } else if (string.equals("boolean")) {
//            return VariableEnum.BOOLEAN;
//        } else if (string.equals("double")) {
//            return VariableEnum.DOUBLE;
//        } else {
//            throw new VariableTypeException();
//        }
//    }

    /**
     * Dynamically create the regex expression for all the Variable types.
     * @return
     */
    public static String Types () {
        if (TYPES != null) {
            return TYPES;
        }
        TYPES = "(";
        for (VariableEnum type : VariableEnum.values()) {
            TYPES += type.toString() + "|";
        }
        TYPES = TYPES.substring(0, TYPES.length()-1) + ")";
        return TYPES;
    }
}
