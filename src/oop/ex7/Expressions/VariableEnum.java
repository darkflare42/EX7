package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.VariableTypeException;

/**
 * Created by Oded on 10/6/2014.
 */
public enum VariableEnum {
    INT("int"),
    STRING("String"),
    CHAR("char"),
    BOOLEAN("boolean"),
    DOUBLE("double"),
    VOID("void");

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

    /**
     * This method checks if a value can be assigned to another value (e.g int can go in double, but not in string)
     * @param member
     * @param value
     * @return
     */
    public static boolean checkValidAssignment(VariableEnum member, VariableEnum value){

        if(member == value) return true;
        if(member == DOUBLE && value == INT) return true;
        return false;
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
