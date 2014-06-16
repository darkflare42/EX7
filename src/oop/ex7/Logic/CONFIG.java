package oop.ex7.Logic;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 13/06/14.
 */
public class CONFIG {

    public static String METHOD_CALL = "(_?[a-zA-Z\\d]+) \\s*\\((.*)\\)"; //TODO: Change.


    public static final String operations = "(\\+|\\-|\\*|\\/)";
    public static final String zeroSpaceOrMore = "\\s*";
    public static final String semicolon = "[;]";
    public static final String startSet = "(";
    public static final String endSet = ")";
    public static final String orReg = "|";

    public static final String variableReg = "(([a-zA-Z_]+)([\\w]*))";
    public static final String methodReg = "(([a-zA-Z_]+)([\\w]*)[(].*[)])";
    public static final String doubleReg = "(\\d+\\.\\d+)";
    public static final String intReg = "(\\d+)";
    public static final String variableArrayReg = "(([a-zA-Z_]+)([\\w]*))\\s*(\\[" + startSet + intReg + orReg + variableReg + endSet +"])";
    public static final String VALUE_REGEX = "[-]?\\d*(\\.\\d+)?";
    //TODO: Insert variable saved names.



    public static final String ValidOperationTypes = startSet + variableReg + orReg + methodReg + orReg + doubleReg + orReg + intReg + orReg + variableArrayReg + endSet;
    public static final String Operators = zeroSpaceOrMore + operations + zeroSpaceOrMore;
    public static final String EndOfLine = zeroSpaceOrMore + semicolon;

    public static final String finalReg = zeroSpaceOrMore + ValidOperationTypes + Operators + ValidOperationTypes + EndOfLine;
    public static Pattern VAR_MATH_OP = Pattern.compile(finalReg);
}
