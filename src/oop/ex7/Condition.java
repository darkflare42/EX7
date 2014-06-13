package oop.ex7;

import java.util.ArrayList;

/**
 * Created by Oded on 12/06/14.
 */
public class Condition {

    public static boolean isValid (String condition, ArrayList<Expression> expressions)
            throws ConditionExpressionNotBooleanException, ConditionUnknownExpressionException, VariableUninitializedException{
        condition = condition.trim();
        if (condition.matches("\\b(true|false)\\b")) {
            // Is the string a boolean.
            return true;
        }
        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)\\b")) {
            // Is the string a variable.
            int index = indexOf(condition, expressions);
            if (index != -1) {
                Variable variable = (Variable)expressions.get(index);
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
            int index = indexOf(condition, expressions);
            if (index != -1) {
                Method method = (Method)expressions.get(index);
                if (method.getType() == VariableEnum.BOOLEAN) {
                    return true;
                } else {
                    throw new ConditionExpressionNotBooleanException();
                }
            }

        }
        throw new ConditionUnknownExpressionException();
    }

    private static int indexOf (String condition, ArrayList<Expression> expressions) {
        int i = 0;
        for (Expression expression: expressions) {
            if (expression.getName().equals(condition)) {
                return (i);
            }
            i++;
        }
        return (-1);
    }
}
