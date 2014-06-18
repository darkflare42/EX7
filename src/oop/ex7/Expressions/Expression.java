package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.AssignMismatchException;

/**
 * Interface class to every expression, e.g. Variables and Methods
 */
public interface Expression {
    public String getName();
    public VariableEnum getType();
    public boolean isInitialized();
    public boolean isArray();
    public boolean isGlobal();
    public void Assign(VariableEnum assign) throws AssignMismatchException;
}
