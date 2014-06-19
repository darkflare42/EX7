package oop.ex7.Logic;

import oop.ex7.Expressions.OperationEnum;
import oop.ex7.Expressions.VariableEnum;

import java.util.regex.Pattern;

/** This class holds all the regex values needed for the program
 */
public class RegexConfig {

    public static final String VALID_NAME = "(_?[a-zA-Z]+[\\w]*)";
    public static final String VALID_OPERATIONS= OperationEnum.GetRegexSet();
    public static final String SPACES = "\\s*";
    public static final char END_OF_LINE = ';';
    public static final char SET_START = '(';
    public static final char SET_END = ')';
    public static final char EQUALS_CHAR = '=';
    public static final char APOSTROPHE_CHAR = '\'';
    public static final char DOUBLE_APOSTROPHE_CHAR = '\"';
    public static final String COMMA_SEPERATOR_CHAR = ",";
    public static final char OR = '|';
    public static final char SQUARE_BRACKETS_START = '[';
    public static final char SQUARE_BRACKETS_END = ']';
    public static final char BLOCK_START_CHAR = '{';
    public static final char BLOCK_END_CHAR = '}';
    public static final char MINUS_CHAR = '-';
    public static final String MINUS_OR_NOT = MINUS_CHAR + "?";
    public static final String PARENTHESES_BLOCK = "([(].*[)])";
    public static final String RETURN_REGEX = "return.*;";
    public static final String ASSIGNMENT_REGEX =  "\\w.*=.*;";
    public static final String BLOCK_TYPES = "(if|while)";
    public static final String BOOLEAN_VALUES = "(true|false)";
    public static final String COMMENT_TYPE_REGEX = "(//.*)";


    public static final String VARIABLE_CALL_REGEX = MINUS_OR_NOT + VALID_NAME;
    public static final String METHOD_REGEX = MINUS_OR_NOT + VALID_NAME + PARENTHESES_BLOCK;
    public static final String DOUBLE_CALL_REGEX = MINUS_OR_NOT + "(\\d+\\.\\d+)";
    public static final String INT_CALL_REGEX = MINUS_OR_NOT + "(\\d+)";
    public static final String STRING_CALL_REGEX = MINUS_OR_NOT + "[\"][\\w]+[\"]";
    public static final String BLOCK_CALL_REGEX = BLOCK_TYPES + " ?\\(.*\\) ?\\"+ BLOCK_START_CHAR;


    public static final String MEMBER_DECLARATION_REGEX = VariableEnum.GetRegexSet(false)+
            " "+ VALID_NAME+"( ?\\s*?=?\\s*?\"?-?\\w?.*\"?)?;$";
    public static final String METHOD_DECLARATION_REGEX = VariableEnum.GetRegexSet(true) +
            "(\\[\\])? [a-zA-Z][_\\w]* ?\\(.*\\) ?\\" + BLOCK_START_CHAR;
    public static final String ARRAY_DECLARATION_REGEX = VariableEnum.GetRegexSet(false) +
            " *\\[ *\\] *(_?[a-zA-Z][_\\w]*)(\\s?=\\s?\\{\\s?.*\\})?;";
    public static final String METHOD_CALL_REGEX = VALID_NAME + "\\(.*\\);";
    public static final String ARRAY_CALL_REGEX = MINUS_OR_NOT + VALID_NAME + "\\s*(\\[" +
            SET_START + INT_CALL_REGEX + OR + VARIABLE_CALL_REGEX + SET_END +"])";


    private static final String ValidOperationTypes = SET_START + VARIABLE_CALL_REGEX + OR + METHOD_REGEX + OR +
            DOUBLE_CALL_REGEX + OR + INT_CALL_REGEX + OR + ARRAY_CALL_REGEX + OR + STRING_CALL_REGEX + SET_END;
    private static final String Operators = SPACES + VALID_OPERATIONS + SPACES;


    public static final String OPERATION_REGEX = SPACES + ValidOperationTypes + Operators + ValidOperationTypes;
    public static Pattern VAR_MATH_OP = Pattern.compile(OPERATION_REGEX);

    //The following are the group numbers of the appropriate elements after applying the VAR_MATH_OP pattern in matcher
    public static final int OPERATOR_GROUP = 12;
    public static final int FIRST_ELEMENT = 1;
    public static final int SECOND_ELEMENT = 13;

    private static String forbiddenWords = null;



    /**
     * Given a string, check if it doesn't match a forbidden word by sjava.
     * @param string String to check.
     * @return true iff string matches exactly a forbidden word.
     */
    public static boolean isForbiddenWord(String string) {
        if (forbiddenWords != null) {
            return (string.matches(forbiddenWords));
        }
        forbiddenWords = VariableEnum.GetRegexSet(true);
        forbiddenWords = forbiddenWords.substring(0,forbiddenWords.length()-1);
        forbiddenWords += "|if|while|return|true|false)";
        return (string.matches(forbiddenWords));
    }


}
