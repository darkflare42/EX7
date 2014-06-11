package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Variable implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized = false;


    public Variable (String varType, String varName) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
    }

    public Variable (String varType, String varName, boolean isInitialized) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isInitialized;
    }

    public String getName () {
        return name;
    }

    public VariableEnum getType () {
        return type;
    }

    public boolean isInitialized () {
        return initialized;
    }
}
