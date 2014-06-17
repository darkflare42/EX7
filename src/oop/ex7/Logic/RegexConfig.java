package oop.ex7.Logic;

import oop.ex7.Expressions.OperationEnum;
import oop.ex7.Expressions.VariableEnum;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class RegexConfig {


    public static final String VALID_NAME = "(_?[a-zA-Z]+[\\w]*)";
    public static final String VALID_OPERATIONS= OperationEnum.operationRegexSet();
    public static final String SPACES = "\\s*";
    public static final char END_OF_LINE = ';';
    private static final char SET_START = '(';
    private static final char SET_END = ')';
    private static final char OR = '|';
    public static final char BLOCK_START_CHAR = '{';
    public static final char BLOCK_END_CHAR = '}';
    public static final String MINUS_OR_NOT = "-?";
    public static final String PARENTHESES_BLOCK = "([(].*[)])";
    public static final String RETURN_REGEX = "return.*;";
    public static final String ASSIGNMENT_REGEX =  "\\w.*=.*;";
    public static final String BLOCK_TYPES = "(if|while)";
    public static final String COMMENT_TYPE_REGEX = "(//.*)";


    public static final String VARIABLE_REGEX = MINUS_OR_NOT + VALID_NAME;
    public static final String METHOD_REGEX = MINUS_OR_NOT + VALID_NAME + PARENTHESES_BLOCK;
    public static final String DOUBLE_REGEX = MINUS_OR_NOT + "(\\d+\\.\\d+)";
    public static final String INT_REGEX = MINUS_OR_NOT + "(\\d+)";
    public static final String STRING_REGEX = MINUS_OR_NOT + "[\"][\\w]+[\"]";
    public static final String ARRAY_TYPE_REGEX = VariableEnum.Types(false) + "\\[\\]";
    public static final String BLOCK_REGEX = BLOCK_TYPES + " ?\\(.*\\) ?\\"+ BLOCK_START_CHAR;


    public static final String MEMBER_DECLARATION_REGEX = VariableEnum.Types(false)+" "+ VALID_NAME+"( ?\\s*?=?\\s*?\"?-?\\w?.*\"?)?;$";
    public static final String METHOD_DECLARATION_REGEX = VariableEnum.Types(true) +"(\\[\\])? [a-zA-Z][_\\w]* ?\\(.*\\) ?\\" + BLOCK_START_CHAR;
    public static final String ARRAY_DECLARATION_REGEX = VariableEnum.Types(false) + " *\\[ *\\] *(_?[a-zA-Z][_\\w]*)(\\s?=\\s?\\{\\s?.*\\})?;";
    public static final String METHOD_CALL_REGEX = VALID_NAME + "\\(.*\\);"; // TODO why cant i change it to use PARENTHESES_BLOCK? the addition of a set seems to break it, but can't find out why.
    public static final String ARRAY_CALL_REGEX = MINUS_OR_NOT + VALID_NAME + "\\s*(\\[" + SET_START + INT_REGEX + OR + VARIABLE_REGEX + SET_END +"])";


    private static final String ValidOperationTypes = SET_START + VARIABLE_REGEX + OR + METHOD_REGEX + OR + DOUBLE_REGEX + OR + INT_REGEX + OR + ARRAY_CALL_REGEX + OR + STRING_REGEX + SET_END;
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
