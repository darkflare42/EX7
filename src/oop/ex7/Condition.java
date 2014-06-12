package oop.ex7;

import java.util.ArrayList;

/**
 * Created by Oded on 12/06/14.
 */
public class Condition {

    public static boolean isValid (String condition, ArrayList<Expression> expressions)
            throws ConditionExpressionNotBooleanException, ConditionUnknownExpressionException, ConditionExpressionNotInitializedException{
        condition = condition.trim();
        if (condition.matches("\\b(true|false)\\b")) {
            // Is the string a boolean.
            return true;
        }
        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)\\b")) {
            // Is the string a variable.
            int index = expressions.indexOf(condition);
            if (index != 0) {
                Variable variable = (Variable)expressions.get(index);
                if (variable.isInitialized()) {
                    if (variable.getType() == VariableEnum.BOOLEAN) {
                        return true;
                    } else {
                        throw new ConditionExpressionNotBooleanException();
                    }
                } else {
                    throw new ConditionExpressionNotInitializedException();
                }
            }
        }
        if (condition.matches("\\b([a-zA-Z_]+)([\\w]*)[ ]*[(][ ]*[)]\\b")) {
            // Is the string a method. Assumes method are saved without parentheses in their names.
            int index = expressions.indexOf(condition.substring(0, condition.indexOf("(")).trim());
            if (index != 0) {
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
}
