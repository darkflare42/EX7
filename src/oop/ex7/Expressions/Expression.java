package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.AssignMismatchException;
import oop.ex7.Expressions.Exceptions.VariableUninitializedException;

/**
 * Interface class to every expression, e.g. Variables and Methods
 */
public interface Expression {

    /**
     * Return the Variable's name.
     * @return name
     */
    public String getName();

    /**
     * Return the Variable's type.
     * @return type
     */
    public VariableEnum getType();

    /**
     * Return a boolean if the Variable is initialized.
     * @return initialized
     */
    public boolean isInitialized();

    /**
     * Return a boolean if the Variable is an array.
     * @return array
     */
    public boolean isArray();

    /**
     * Return a boolean if the Variable is a global member.
     * @return m_isGlobal.
     */
    public boolean isGlobal();


    /**
     * Assign a value to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign VariableEnum to assign to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of
     * the Variable.
     */
    public void Assign(VariableEnum assign) throws AssignMismatchException;


    /**
     * Assign the value of an expression to a Variable. Initializes the Variable (if it was not initialized).
     * @param expression Expression to assign its' value to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of
     * the Variable.
     * @throws VariableUninitializedException if assign is not an initialized Expression.
     */
    public void Assign(Expression expression) throws AssignMismatchException, VariableUninitializedException;
}
