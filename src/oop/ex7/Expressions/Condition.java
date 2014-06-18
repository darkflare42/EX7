package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.ConditionArrayCallMismatch;
import oop.ex7.Expressions.Exceptions.ConditionExpressionNotBooleanException;
import oop.ex7.Expressions.Exceptions.ConditionUnknownExpressionException;
import oop.ex7.Expressions.Exceptions.VariableUninitializedException;
import oop.ex7.Logic.RegexConfig;

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
     * @throws oop.ex7.Expressions.Exceptions.ConditionExpressionNotBooleanException
     * @throws oop.ex7.Expressions.Exceptions.ConditionUnknownExpressionException
     * @throws oop.ex7.Expressions.Exceptions.VariableUninitializedException
     * @throws oop.ex7.Expressions.Exceptions.ConditionArrayCallMismatch
     */
    public static boolean isValid (String condition, LinkedHashMap<String, Expression> expressions)
            throws ConditionExpressionNotBooleanException, ConditionUnknownExpressionException,
            VariableUninitializedException, ConditionArrayCallMismatch {
        condition = condition.trim();

        if (condition.matches("(true|false)")) {
            // Is the string a boolean.
            return true;
        }

        if (condition.matches(RegexConfig.VALID_NAME)) {
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

        if (condition.matches(RegexConfig.ARRAY_CALL_REGEX)) {
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

        if (condition.matches(RegexConfig.METHOD_REGEX)) {
            Method method = (Method)expressions.get(condition.substring(0,condition.indexOf("(")).trim());
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
