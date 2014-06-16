package oop.ex7.Expressions;

/**
 * Interface class to every expression, e.g. Variables and Methods
 */
public interface Expression {
    public String getName();
    public VariableEnum getType();
    public boolean isInitialized();
    public boolean isArray();
    public boolean isGlobal();
}
