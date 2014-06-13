package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Variable implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized;
    private boolean array;


    public Variable (String varType, String varName) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = false;
        array = false;
    }

    public Variable (String varType, String varName, boolean isInitialized) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isInitialized;
        array = false;
    }

    public Variable (String varType, String varName, boolean isinitialized, boolean isarray) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isinitialized;
        array = isarray;
    }

    public void Assign (VariableEnum assign) throws VariableTypeException{
        if (assign!=type) {
            throw new VariableTypeException();
        }
        initialized = true;
    }

    public void Assign (Expression assign) throws VariableTypeException, VariableUninitializedException{
        if (!assign.isInitialized()) {
            throw new VariableUninitializedException();
        }
        if (assign.getType()!=type) {
            throw new VariableTypeException();
        }
        initialized = true;
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

    public boolean isArray () {
        return array;
    }
}
