package oop.ex7;

/**
 * Created by Oded on 14/6/2014.
 */
public class RegexBlat {
    //TODO delete this
    public static void main(String[] args) {
        String regexHell = "\\b(([a-zA-Z_]+)([\\w]*))\\s*[=]\\s*((([a-zA-Z_]+)([\\w]*))|(([a-zA-Z_]+)([\\w]*)[(].*[)])|(\\d+\\.\\d+)|(\\d+))\\s*(\\+|\\-|\\*|\\/)\\s*((([a-zA-Z_]+)([\\w]*))|(([a-zA-Z_]+)([\\w]*)[(].*[)])|(\\d+\\.\\d+)|(\\d+))\\s*[;]";

        // regex for human beings:
        String variableReg = "(([a-zA-Z_]+)([\\w]*))";
        String methodReg = "(([a-zA-Z_]+)([\\w]*)[(].*[)])";
        String doubleReg = "(\\d+\\.\\d+)";
        String intReg = "(\\d+)";
        String operations = "(\\+|\\-|\\*|\\/)";
        String zeroSpaceOrMore = "\\s*";
        String boundry = "\\b";
        String equals = "[=]";
        String semicolon = "[;]";
        String startSet = "(";
        String endSet = ")";
        String orReg = "|";
        String finalReg = boundry + variableReg + zeroSpaceOrMore + equals + zeroSpaceOrMore +
                startSet + variableReg + orReg + methodReg + orReg + doubleReg + orReg + intReg + endSet +
                zeroSpaceOrMore + operations + zeroSpaceOrMore +
                startSet + variableReg + orReg + methodReg + orReg + doubleReg + orReg + intReg + endSet +
                zeroSpaceOrMore + semicolon;

        if (finalReg.equals(regexHell)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
