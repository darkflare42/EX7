package oop.ex7.Logic;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class CONFIG {

    //TODO consider merging this file with ExpressionTypeEnum
    public static final String VALID_NAME = "^[a-zA-Z0-9_]*$";

    public static final String operations = "(\\+|\\-|\\*|\\/)";
    public static final String zeroSpaceOrMore = "\\s*";
    public static final String semicolon = "[;]";
    public static final String startSet = "(";
    public static final String endSet = ")";
    public static final String OR = "|";
    public static final String minusOrNot = "[-]{0,1}";

    public static final String variableReg = minusOrNot + "(([a-zA-Z_]+)([\\w]*))";
    public static final String methodReg = minusOrNot + "(([a-zA-Z_]+)([\\w]*)[(].*[)])";
    public static final String doubleReg = minusOrNot + "(\\d+\\.\\d+)";
    public static final String intReg = minusOrNot + "(\\d+)";
    public static final String variableArrayReg = minusOrNot + "(([a-zA-Z_]+)([\\w]*))\\s*(\\[" + startSet + intReg + OR + variableReg + endSet +"])";
    public static final String stringReg = minusOrNot + "[\"][\\w]+[\"]";

    public static final String VALUE_REGEX = "[-]?\\d*(\\.\\d+)?";
    //TODO: Insert variable saved names



    public static final String ValidOperationTypes = startSet + variableReg + OR + methodReg + OR + doubleReg + OR + intReg + OR + variableArrayReg + OR + stringReg + endSet;
    public static final String Operators = zeroSpaceOrMore + operations + zeroSpaceOrMore;
    public static final String EndOfLine = zeroSpaceOrMore + semicolon;

    public static final String finalReg = zeroSpaceOrMore + ValidOperationTypes + Operators + ValidOperationTypes; //+ EndOfLine;
    public static Pattern VAR_MATH_OP = Pattern.compile(finalReg);
}
