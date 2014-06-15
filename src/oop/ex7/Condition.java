package oop.ex7;

import java.util.LinkedHashMap;

/**
 * Represents a boolean condition inside parentheses of a while / if blocks.
 */
public class Condition {

    /**
     * Checks if a given string is a valid boolean condition within the rules of s-java.
     * @param condition Condition string of a if / while block, excluding parentheses
     * @param expressions A LinkedHashMap collection of all the expressions the condition is supposed to know.
     * @return true if the condition is a valid boolean condition. Throws an exception otherwise.
     * @throws ConditionExpressionNotBooleanException Condition represents a VariableType which is not boolean.
     * @throws ConditionUnknownExpressionException Condition call an expression that does not exist.
     * @throws VariableUninitializedException Condition contains a variable which is uninitialized.
     */
    public static boolean isValid (String condition, LinkedHashMap<String, Expression> expressions)
            throws ConditionExpressionNotBooleanException, ConditionUnknownExpressionException, VariableUninitializedException, ConditionArrayCallMismatch{
        condition = condition.trim();

        if (condition.matches("\\b(true|false)\\b")) {
            // Is the string a boolean.
            return true;
        }

        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)\\b")) {
            // Is the string a variable.
            Variable variable = (Variable)expressions.get(condition);
            if (variable != null) {
                if (variable.isArray()) {
                    throw new ConditionArrayCallMismatch();
                }
                if (variable.isInitialized()) {
                    if (variable.getType() == VariableEnum.BOOLEAN) {
                        return true;
                    } else {
                        throw new ConditionExpressionNotBooleanException();
                    }
                } else {
                    throw new VariableUninitializedException();
                }
            }
        }

        if (condition.matches("(([a-zA-Z_]+)([\\w]*))\\s*\\[((\\d+)|(([a-zA-Z_]+)([\\w]*)))]")) {
            // Is the string an array.
            Variable variable = (Variable)expressions.get(condition.substring(0, condition.indexOf("[")));
            if (variable != null) {
                if (!variable.isArray()) {
                    throw new ConditionArrayCallMismatch();
                }
                if (variable.isInitialized()) {
                    if (variable.getType() == VariableEnum.BOOLEAN) {
                        return true;
                    } else {
                        throw new ConditionExpressionNotBooleanException();
                    }
                } else {
                    throw new VariableUninitializedException();
                }
            }
        }

        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)[ ]*[(][ ]*[)]\\b")) {
            // Is the string a method. Assumes method are saved with parentheses in their names.
            Method method = (Method)expressions.get(condition);
            if (method != null) {
                if (method.getType() == VariableEnum.BOOLEAN) {
                    return true;
                } else {
                    throw new ConditionExpressionNotBooleanException();
                }
            }

        }

        throw new ConditionUnknownExpressionException();
    }
}
