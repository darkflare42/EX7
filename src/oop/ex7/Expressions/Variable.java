package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.AssignMismatchException;
import oop.ex7.Expressions.Exceptions.VariableTypeException;
import oop.ex7.Expressions.Exceptions.VariableUninitializedException;

/**
 * Class that represents a Variable expression.
 * Contains members for the type of the Variable, its' name, a boolean if its initialized and a boolean if the object
 * represents an array.
 */
public class Variable implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized;
    private boolean m_isGlobal;
    public boolean m_isArray;

    /**
     * Constructor for a variable declaration with an assignment.
     * @param varType VariableEnum of the type of the Variable.
     * @param varName String of the name of the Variable.
     * @param isInitialized boolean value if the Variable is initialized.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable(VariableEnum varType, String varName, boolean isInitialized) throws VariableTypeException{
        type = varType;
        name = varName;
        initialized = isInitialized;
        m_isGlobal = false;
    }

    /**
     * Constructor for a variable declaration without a value.
     * @param varType String of the type of the Variable.
     * @param varName String of the name of the Variable.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable (String varType, String varName) throws VariableTypeException {
        this(VariableEnum.toEnum(varType), varName, false);
    }

    /**
     * Constructor for a variable declaration with an assignment.
     * @param varType String of the type of the Variable.
     * @param varName String of the name of the Variable.
     * @param isInitialized boolean value if the Variable is initialized.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable (String varType, String varName, boolean isInitialized) throws VariableTypeException {
        this(VariableEnum.toEnum(varType), varName, isInitialized);
    }



    /**
     * Constructor for an array declaration.
     * @param varType String of the type of the array.
     * @param varName String of the name of the array.
     * @param isInitialized boolean value if the array is initialized.
     * @param isarray boolean value if the Variable is an array.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable (String varType, String varName, boolean isInitialized, boolean isarray) throws VariableTypeException {
        this(VariableEnum.toEnum(varType), varName, isInitialized);
        m_isArray = isarray;
    }

    /**
     * Assign a value to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign VariableEnum to assign to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of the Variable.
     */
    // TODO there is a redundant method of the same functionality in VariableEnum.
    public void Assign (VariableEnum assign) throws AssignMismatchException {
        if(type != assign){
            if(type == VariableEnum.DOUBLE && assign == VariableEnum.INT) return;
            throw new AssignMismatchException();
        }
        else if(m_isArray)
            throw new AssignMismatchException();

        initialized = true;
    }

    /**
     * Assign the value of an expression to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign Expression to assign its' value to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of the Variable.
     * @throws VariableUninitializedException if assign is not an initialized Expression.
     */
    // TODO there is a redundant method of the same functionality in VariableEnum.
    public void Assign (Expression assign) throws AssignMismatchException, VariableUninitializedException {
        if (!assign.isInitialized()) {
            throw new VariableUninitializedException();
        }
        Assign(assign.getType());
    }

    /**
     * Return the Variable's name.
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Return the Variable's type.
     * @return type
     */
    public VariableEnum getType () {
        return type;
    }

    /**
     * Return a boolean if the Variable is initialized.
     * @return initialized
     */
    public boolean isInitialized () {
        return initialized;
    }

    /**
     * Return a boolean if the Variable is an array.
     * @return array
     */
    public boolean isArray () {
        return m_isArray;
    }

    /**
     * Return a boolean if the Variable is a global member.
     * @return m_isGlobal.
     */
    public boolean isGlobal(){
        return m_isGlobal;
    }

    /**
     * Set the global status of the Variable.
     * @param isGlobal true if the Variable is a global, false if not.
     */
    // TODO consider moving this to the constructor?
    public void setGlobal(boolean isGlobal){
        m_isGlobal = isGlobal;
    }
}
