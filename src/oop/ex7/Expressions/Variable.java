package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.VariableTypeException;
import oop.ex7.Expressions.Exceptions.VariableUninitializedException;

/**
 * Created by Oded on 10/6/2014.
 */
public class Variable implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized;
    private boolean array;
    private boolean m_isGlobal;


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

    public Variable (String varType, String varName, boolean isInitialized, boolean isarray) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isInitialized;
        array = isarray;
    }



    public void Assign (VariableEnum assign) throws VariableTypeException{
        if (assign!=type) {
            throw new VariableTypeException();
        }
        initialized = true;
    }

    public void Assign (Expression assign) throws VariableTypeException, VariableUninitializedException {
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

    public boolean isGlobal(){return m_isGlobal;}

    public void setGlobal(boolean isGlobal){
        m_isGlobal = isGlobal;
    }
}
