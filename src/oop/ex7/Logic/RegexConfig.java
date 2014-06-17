package oop.ex7.Logic;

import oop.ex7.Expressions.VariableEnum;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class RegexConfig {


    public static final String VALID_NAME = "(_?[a-zA-Z]+[\\w]*)";

    private static final String VALID_OPERATIONS = "(\\+|\\-|\\*|\\/)";
    private static final String SPACES = "\\s*";
    private static final char END_OF_LINE = ';';
    private static final char SET_START = '(';
    private static final char SET_END = ')';
    private static final char OR = '|';
    private static final String MINUS_OR_NOT = "-?";


    public static final String VARIABLE_REG = MINUS_OR_NOT + VALID_NAME;
    public static final String METHOD_REG = MINUS_OR_NOT + VALID_NAME + "([(].*[)])";
    public static final String DOUBLE_REG = MINUS_OR_NOT + "(\\d+\\.\\d+)";
    public static final String INT_REG = MINUS_OR_NOT + "(\\d+)";
    public static final String STRING_REG = MINUS_OR_NOT + "[\"][\\w]+[\"]";
    public static final String VARIABLE_ARRAY_REG = MINUS_OR_NOT + VALID_NAME + "\\s*(\\[" + SET_START + INT_REG + OR + VARIABLE_REG + SET_END +"])";


    private static final String ValidOperationTypes = SET_START + VARIABLE_REG + OR + METHOD_REG + OR + DOUBLE_REG + OR + INT_REG + OR + VARIABLE_ARRAY_REG + OR + STRING_REG + SET_END;
    private static final String Operators = SPACES + VALID_OPERATIONS + SPACES;


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
