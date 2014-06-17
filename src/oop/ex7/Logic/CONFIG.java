package oop.ex7.Logic;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class CONFIG {

    //TODO consider merging this file with ExpressionTypeEnum
    public static final String VALID_NAME = "(([a-zA-Z_]+)([\\w]*))";

    public static final String operations = "(\\+|\\-|\\*|\\/)";
    public static final String SPACES = "\\s*";
    public static final String semicolon = "[;]";
    public static final String START = "(";
    public static final String END = ")";
    public static final String OR = "|";
    public static final String minusOrNot = "[-]?";

    public static final String variableReg = minusOrNot + VALID_NAME;
    public static final String methodReg = minusOrNot + "(([a-zA-Z_]+)([\\w]*)[(].*[)])"; //TODO wanted to make this more dynamic by adding the VALID_NAME string before the () suffix.
    public static final String doubleReg = minusOrNot + "(\\d+\\.\\d+)";
    public static final String intReg = minusOrNot + "(\\d+)";
    public static final String variableArrayReg = minusOrNot + "(([a-zA-Z_]+)([\\w]*))\\s*(\\[" + START + intReg + OR + variableReg + END +"])";
    public static final String stringReg = minusOrNot + "[\"][\\w]+[\"]";

    public static final String VALUE_REGEX = "[-]?\\d*(\\.\\d+)?";
    //TODO: Insert variable saved names



    public static final String ValidOperationTypes = START + variableReg + OR + methodReg + OR + doubleReg + OR + intReg + OR + variableArrayReg + OR + stringReg + END;
    public static final String Operators = SPACES + operations + SPACES;
    public static final String EndOfLine = SPACES + semicolon;

    public static final String finalReg = SPACES + ValidOperationTypes + Operators + ValidOperationTypes; //+ EndOfLine;
    public static Pattern VAR_MATH_OP = Pattern.compile(finalReg);
}
