package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.VariableTypeException;

/**
 * Enum for all Variable types.
 */
public enum VariableEnum {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double"),
    VOID("void"),
    ARRAY_TYPE("");

    private final String nameString;
    private static String TYPES = null;

    /**
     * Private constructor.
     * @param name String for the name of the VariableEnum.
     */
    VariableEnum(String name) {
        nameString = name;
    }

    /**
     * Convert an enum to String.
     * @return The String value of an enum.
     */
    public String toString() {
        return nameString;
    }

    /**
     * Convert a String to enum.
     * @param string String to convert.
     * @return VariableEnum corresponding to the string.
     * @throws VariableTypeException if string is not a valid enum string.
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
     * Return a String of all possible VariableEnums.
     * @param withVoid boolean to include "Void" as part of the set.
     * @return String of a regex representation of all possible VariableEnums.
     */
    public static String GetRegexSet(boolean withVoid) {
        if (TYPES != null) {  //Modified by OR
            if((withVoid && TYPES.contains("void")) ||
                    !withVoid && !TYPES.contains("void"))
                return TYPES;
        }
        TYPES = "(";
        for (VariableEnum type : VariableEnum.values()) {
            if (type == VariableEnum.VOID && !withVoid) continue;


            TYPES += type.toString() + "|";
        }
        TYPES = TYPES.substring(0, TYPES.length()-1) + ")";
        return TYPES;
    }

}
