package oop.ex7.Logic;

import oop.ex7.Expressions.VariableEnum;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class CONFIG {

    //TODO consider merging this file with ExpressionTypeEnum
    public static final String VALID_NAME = "(([a-zA-Z_]+)([\\w]*))";

    private static final String operations = "(\\+|\\-|\\*|\\/)";
    private static final String SPACES = "\\s*";
    private static final String semicolon = "[;]";
    private static final String START = "(";
    private static final String END = ")";
    private static final String OR = "|";
    private static final String minusOrNot = "[-]?";

    public static final String variableReg = minusOrNot + VALID_NAME;
    public static final String methodReg = minusOrNot + VALID_NAME + "([(].*[)])";
    public static final String doubleReg = minusOrNot + "(\\d+\\.\\d+)";
    public static final String intReg = minusOrNot + "(\\d+)";
    public static final String variableArrayReg = minusOrNot + VALID_NAME + "\\s*(\\[" + START + intReg + OR + variableReg + END +"])";
    public static final String stringReg = minusOrNot + "[\"][\\w]+[\"]";

    public static final String VALUE_REGEX = "[-]?\\d*(\\.\\d+)?";
    //TODO: Insert variable saved names



    private static final String ValidOperationTypes = START + variableReg + OR + methodReg + OR + doubleReg + OR + intReg + OR + variableArrayReg + OR + stringReg + END;
    private static final String Operators = SPACES + operations + SPACES;

    public static final String OPERATION_REGEX = SPACES + ValidOperationTypes + Operators + ValidOperationTypes;
    public static Pattern VAR_MATH_OP = Pattern.compile(OPERATION_REGEX);

    private static String forbiddenWords = null;

    public static boolean isForbiddenWord(String string) {
        if (forbiddenWords != null) {
            return (string.matches(forbiddenWords));
        }
        forbiddenWords = VariableEnum.Types(true);
        forbiddenWords = forbiddenWords.substring(0,forbiddenWords.length()-1);
        forbiddenWords += "|if|while|return)";
        return (string.matches(forbiddenWords));
    }
}
