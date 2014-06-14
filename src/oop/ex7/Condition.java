package oop.ex7;

import java.util.LinkedHashMap;

/**
 * Created by Oded on 12/06/14.
 */
public class Condition {

    public static boolean isValid (String condition, LinkedHashMap<String, Expression> expressions)
            throws ConditionExpressionNotBooleanException, ConditionUnknownExpressionException, VariableUninitializedException{
        condition = condition.trim();
        if (condition.matches("\\b(true|false)\\b")) {
            // Is the string a boolean.
            return true;
        }
        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)\\b")) {
            // Is the string a variable.
            Variable variable = (Variable)expressions.get(condition);
            if (variable != null) {
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
