package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Variable {
    private VariableType type;
    private String name;
    private String value = "";


    public Variable (String varType, String varName) throws InvalidTypeException{
        type = findType(varType);
        name = varName;
    }

    public Variable (String varType, String varName, String varValue) throws InvalidTypeException{
        type = findType(varType);
        name = varName;
        value = varValue;
    }

    private VariableType findType (String varType) throws InvalidTypeException{
        if (varType.equals("int")) {
            return VariableType.INT;
        } else if (varType.equals("char")) {
            return VariableType.CHAR;
        } else if (varType.equals("double")) {
            return VariableType.DOUBLE;
        } else if (varType.equals("boolean")) {
            return VariableType.BOOLEAN;
        } else if (varType.equals("String")) {
            return VariableType.STRING;
        } else {
            throw new InvalidTypeException();
        }
    }

    public String getName () {
        return name;
    }

    public VariableType getType () {
        return type;
    }

    public String getValue () {
        return value;
    }
}
