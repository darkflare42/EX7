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
    ARRAY_TYPE(""); //TODO wat

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
     * This method checks if a value can be assigned to another value (e.g int can go in double, but not in string)
     * @param member
     * @param value
     * @return
     */
    //TODO this exists in Variable's API and also makes more sense there.
    public static boolean checkValidAssignment(VariableEnum member, VariableEnum value){

        if(member == value) return true;
        if(member == DOUBLE && value == INT) return true;
        return false;
    }

    /**
     * Return a String of all possible VariableEnums.
     * @param withVoid boolean to include "Void" as part of the set.
     * @return String of a regex representation of all possible VariableEnums.
     */
    public static String Types (boolean withVoid) {
        if (TYPES != null) {  //Modified by OR
            if((withVoid == true && TYPES.contains("void")) ||
                    withVoid == false && !TYPES.contains("void"))
                return TYPES;
        }
        TYPES = "(";
        for (VariableEnum type : VariableEnum.values()) {
            if (type == VariableEnum.VOID && !withVoid) continue;

            //if(type == VariableEnum.VOID && !withVoid) //TODO: Check
//                continue;
            TYPES += type.toString() + "|";
        }
        TYPES = TYPES.substring(0, TYPES.length()-1) + ")";
        return TYPES;
    }

}
