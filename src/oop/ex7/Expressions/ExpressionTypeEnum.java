package oop.ex7.Expressions;

import oop.ex7.Logic.RegexConfig;

import java.util.regex.Pattern;

/**
 * Enum to contain all valid line types that can be compiled.
 */
public enum ExpressionTypeEnum {
    MEM_DECLARATION,
    METHOD_DECLARATION,
    METHOD_CALL,
    RETURN,
    BLOCK_START,
    BLOCK_END,
    ASSIGNMENT,
    COMMENT,
    UNKNOWN;

    //TODO: Add string values and change throughout code
    //TODO: FIXES
    //TODO consider merging this file with CONFIG

    public static final char BLOCK_START_CHAR = '{';  //Has to be constant expression
    public static final char BLOCK_END_CHAR = '}';
    public static final char END_OF_LINE_CHAR = ';';


    public static final String MEMBER_DECLARATION_REGEX = VariableEnum.Types(false)+" "+ RegexConfig.VALID_NAME+"( ?\\s*?=?\\s*?\"?-?\\w?.*\"?)?;$";
    public static final String METHOD_DECLARATION_REGEX = VariableEnum.Types(true) +"(\\[\\])? [a-zA-Z][_\\w]* ?\\(.*\\) ?\\" +
            BLOCK_START_CHAR;
    public static final String ARRAY_DECLARATION_REGEX = VariableEnum.Types(false) + " *\\[ *\\] *(_?[a-zA-Z][_\\w]*)(\\s?=\\s?\\{\\s?.*\\})?;";
    public static final String RETURN_REGEX = "return.*;";
    public static final String METHOD_CALL_REGEX = RegexConfig.VALID_NAME + "\\(.*\\);";
    public static final String OPERATION_REGEX =  "\\w.*=.*;";
    public static final String BLOCK_TYPES = "(if|while)";
    public static final String BLOCK_REGEX = BLOCK_TYPES + " ?\\(.*\\) ?\\"+BLOCK_START_CHAR;
    public static final String ARRAY_TYPE_REGEX = VariableEnum.Types(false) + "\\[\\]";
    public static final String COMMENT_TYPE_REGEX = "(//.*)";

    public static final Pattern MEMBER_DECLARATION_PATTERN = Pattern.compile(MEMBER_DECLARATION_REGEX);
    public static final Pattern ARRAY_DECLARATION_PATTERN = Pattern.compile(ARRAY_DECLARATION_REGEX);


    public static ExpressionTypeEnum checkType(String line){
        if(line.startsWith("//"))
            return COMMENT;
        switch(line.charAt(line.length()-1)){ //Check the last character in the line
            case BLOCK_START_CHAR: //Method declaration/while, if block
                if(line.matches(METHOD_DECLARATION_REGEX))
                    return METHOD_DECLARATION;
                else if(line.matches(BLOCK_REGEX))
                    return BLOCK_START;
                else{
                    return UNKNOWN;
                }

            case BLOCK_END_CHAR: //End of method/block
                return BLOCK_END;

            case END_OF_LINE_CHAR: //member declaration or unknown
                if(line.matches(MEMBER_DECLARATION_REGEX) || line.matches(ARRAY_DECLARATION_REGEX))
                    return MEM_DECLARATION;
                else if(line.matches(METHOD_CALL_REGEX))
                    return METHOD_CALL;
                else if(line.matches(OPERATION_REGEX))
                    return ASSIGNMENT;
                else if(line.matches(RETURN_REGEX))
                    return RETURN;

            default: //this may be a comment
                if(line.matches(COMMENT_TYPE_REGEX))
                    return COMMENT;
                return UNKNOWN;
        }


    }


}
