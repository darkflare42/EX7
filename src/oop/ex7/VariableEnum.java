package oop.ex7;

/**
 * Enum for all Variable types.
 */
public enum VariableEnum {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double");

    private final String nameString;
    private static String TYPES = null;

    VariableEnum(String name) {
        nameString = name;
    }

    /**
     * Return the String value of a VariableEnum.
     * @return nameString value.
     */
    public String toString() {
        return nameString;
    }

    /**
     * Convert a String value to enum.
     * @param string String to convert.
     * @return VariableEnum type according to the string.
     * @throws VariableTypeException if string does not represent a valid VariableEnum.
     */
    public static VariableEnum toEnum (String string) throws VariableTypeException {
        for (VariableEnum type: VariableEnum.values()) {
            if (string.equals(type.toString())) {
                return type;
            }
        }
        throw new VariableTypeException();
    }

    /**
     * Dynamically create the regex expression for all the Variable types.
     * @return Sring in regex format for a set of all the different types, separated with "|" symbols.
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

    /**
     * Dynamically create the regex expression for all the Variable types, including Void.
     * @return Sring in regex format for a set of all the different types, separated with "|" symbols.
     */
    public static String TypesInclVoid(){
        if (TYPES != null) {
            return TYPES.substring(0, TYPES.length()-1) + "void)";
        }
        return Types();
    }
}
