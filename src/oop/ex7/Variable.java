package oop.ex7;

/**
 * Class that represents a Variable expression.
 * Contains members for the type of the Variable, its' name, a boolean if its initliazed and a boolean if the object
 * represents an array.
 */
public class Variable implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized;
    private boolean array;

    /**
     * Constructor for a variable declaration without a value.
     * @param varType String of the type of the Variable.
     * @param varName String of the name of the Variable.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable (String varType, String varName) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = false;
        array = false;
    }

    /**
     * Constructor for a variable declaration with an assignment.
     * @param varType String of the type of the Variable.
     * @param varName String of the name of the Variable.
     * @param isInitialized boolean value if the Variable is initialized.
     * @throws VariableTypeException if varType is an invalid Variable type.
     */
    public Variable (String varType, String varName, boolean isInitialized) throws VariableTypeException {
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isInitialized;
        array = false;
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
        type = VariableEnum.toEnum(varType);
        name = varName;
        initialized = isInitialized;
        array = isarray;
    }

    /**
     * Assign a value to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign VariableEnum to assign to the variable.
     * @throws VariableAssignMismatchException if assign is a type that mismatches the type of the Variable.
     */
    public void Assign (VariableEnum assign) throws VariableAssignMismatchException{
        if (assign!=type) {
            throw new VariableAssignMismatchException();
        }
        initialized = true;
    }

    /**
     * Assign the value of an expression to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign Expression to assign its' value to the variable.
     * @throws VariableAssignMismatchException if assign is a type that mismatches the type of the Variable.
     * @throws VariableUninitializedException if assign is not an initialized Expression.
     */
    public void Assign (Expression assign) throws VariableAssignMismatchException, VariableUninitializedException{
        if (!assign.isInitialized()) {
            throw new VariableUninitializedException();
        }
        if (assign.getType()!=type) {
            throw new VariableAssignMismatchException();
        }
        initialized = true;
    }

    /**
     * Returns the name of the Variable.
     * @return name.
     */
    public String getName () {
        return name;
    }

    /**
     * Returns the VariableEnum type of the Variable.
     * @return type.
     */
    public VariableEnum getType () {
        return type;
    }

    /**
     * Returns a boolean for if the Variable is initialized.
     * @return initialized.
     */
    public boolean isInitialized () {
        return initialized;
    }

    /**
     * Returns a boolean for fi the Variable represents an array.
     * @return array.
     */
    public boolean isArray () {
        return array;
    }
}
