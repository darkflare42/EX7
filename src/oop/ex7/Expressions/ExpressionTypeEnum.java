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

    //TODO: Add string values and change throughout code. oded: VAS DE FOOKEN

    public static final Pattern MEMBER_DECLARATION_PATTERN = Pattern.compile(RegexConfig.MEMBER_DECLARATION_REGEX);
    public static final Pattern ARRAY_DECLARATION_PATTERN = Pattern.compile(RegexConfig.ARRAY_DECLARATION_REGEX);


    public static ExpressionTypeEnum checkType(String line){
        if(line.startsWith("//"))
            return COMMENT;
        switch(line.charAt(line.length()-1)){ //Check the last character in the line
            case RegexConfig.BLOCK_START_CHAR: //Method declaration/while, if block
                if(line.matches(RegexConfig.METHOD_DECLARATION_REGEX))
                    return METHOD_DECLARATION;
                else if(line.matches(RegexConfig.BLOCK_CALL_REGEX))
                    return BLOCK_START;
                else{
                    return UNKNOWN;
                }

            case RegexConfig.BLOCK_END_CHAR: //End of method/block
                return BLOCK_END;

            case RegexConfig.END_OF_LINE: //member declaration or unknown
                if(line.matches(RegexConfig.MEMBER_DECLARATION_REGEX) || line.matches(RegexConfig.ARRAY_DECLARATION_REGEX))
                    return MEM_DECLARATION;
                else if(line.matches(RegexConfig.METHOD_CALL_REGEX))
                    return METHOD_CALL;
                else if(line.matches(RegexConfig.ASSIGNMENT_REGEX))
                    return ASSIGNMENT;
                else if(line.matches(RegexConfig.RETURN_REGEX))
                    return RETURN;

            default: //this may be a comment
                if(line.matches(RegexConfig.COMMENT_TYPE_REGEX))
                    return COMMENT;
                return UNKNOWN;
        }


    }


}
