package oop.ex7;

/**
 * Created by Oded on 14/6/2014.
 */
public class RegexBlat {
    //TODO delete this
    public static void main(String[] args) {
        //outdated since addition of arrays;
        String regexHell = "\\s*(([a-zA-Z_]+)([\\w]*))\\s*[=]\\s*((([a-zA-Z_]+)([\\w]*))|(([a-zA-Z_]+)([\\w]*)[(].*[)])|(\\d+\\.\\d+)|(\\d+))\\s*(\\+|\\-|\\*|\\/)\\s*((([a-zA-Z_]+)([\\w]*))|(([a-zA-Z_]+)([\\w]*)[(].*[)])|(\\d+\\.\\d+)|(\\d+))\\s*[;]";

        // regex for human beings:
        String operations = "(\\+|\\-|\\*|\\/)";
        String zeroSpaceOrMore = "\\s*";
        String equals = "[=]";
        String semicolon = "[;]";
        String startSet = "(";
        String endSet = ")";
        String orReg = "|";

        String variableReg = "(([a-zA-Z_]+)([\\w]*))";
        String methodReg = "(([a-zA-Z_]+)([\\w]*)[(].*[)])";
        String doubleReg = "(\\d+\\.\\d+)";
        String intReg = "(\\d+)";
//        String variableArrayReg = "(([a-zA-Z_]+)([\\w]*))\\s*(\\[" + startSet + intReg + orReg + variableReg + endSet +"])";
        String variableArrayReg = "(([a-zA-Z_]+)([\\w]*))\\s*(\\[((\\d+)|(([a-zA-Z_]+)([\\w]*)))])";




        String VariableEquals = zeroSpaceOrMore + startSet + variableReg + orReg + variableArrayReg + endSet + zeroSpaceOrMore + equals + zeroSpaceOrMore;
        String ValidOperationTypes = startSet + variableReg + orReg + methodReg + orReg + doubleReg + orReg + intReg + orReg + variableArrayReg + endSet;
        String Operators = zeroSpaceOrMore + operations + zeroSpaceOrMore;
        String EndOfLine = zeroSpaceOrMore + semicolon;

        String finalReg = VariableEquals + ValidOperationTypes + Operators + ValidOperationTypes + EndOfLine;

//        if (finalReg.equals(regexHell)) {
//            System.out.println("true");
//        } else {
//            System.out.println("false");
//        }
    }
}
