package oop.ex7.Expressions;

import java.util.regex.Pattern;

/**
 * Created by Or Keren on 10/06/14.
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

    public static final char BLOCK_START_CHAR = '{';  //Has to be constant expression
    public static final char BLOCK_END_CHAR = '}';
    public static final char END_OF_LINE_CHAR = ';';

    public static final String NAME="(_?[a-zA-Z][_\\w]*)";
    //*( ?\s*?=\s*?\w.*)?;$ - new
    //( ?=\w.*)?;$"


    public static final String MEMBER_DECLARATION_REGEX = VariableEnum.Types(false)+" "+NAME+"( ?\\s*?=?\\s*?\"?-?\\w?.*\"?)?;$";
    public static final String METHOD_DECLARATION_REGEX = VariableEnum.Types(true) +"(\\[\\])? [a-zA-Z][_\\w]* ?\\(.*\\) ?\\" +
            BLOCK_START_CHAR;
    //public static final String ARRAY_DECLARATION_REGEX = VariableEnum.Types(false) + " *[\\w\\<\\>\\[\\]]+\\s+" +NAME+
    //        "*( ?=.*)?\\{(.*?)\\};";
    public static final String ARRAY_DECLARATION_REGEX = "(int|String|char|boolean|double) *[\\[\\]]+ (_?[a-zA-Z][_\\w])*(\\s?=\\s?\\{\\s?.*\\})?;";
            //"int|double|String|boolean|char) *[\w\<\>\[\]]+\s+_?[a-zA-Z][_\w]*( ?=.*)?;";
    public static final String RETURN_REGEX = "return.*;";
    public static final String METHOD_CALL_REGEX = NAME + "\\(.*\\);";
    public static final String OPERATION_REGEX =  "\\w.*=.*;";
    public static final String BLOCK_TYPES = "(if|while)";
    public static final String BLOCK_REGEX = BLOCK_TYPES + " ?\\(.*\\) ?\\"+BLOCK_START_CHAR;
    public static final String ARRAY_TYPE_REGEX = "(int|String|char|boolean|double)\\[\\]";
    public static final String COMMENT_TYPE_REGEX = "(//.*)";

    public static final Pattern MEMBER_DECLARATION_PATTERN = Pattern.compile(MEMBER_DECLARATION_REGEX);
    public static final Pattern ARRAY_DECLARATION_PATTERN = Pattern.compile(ARRAY_DECLARATION_REGEX);





    // array regex - (int|double|String|boolean|char) *[\w\<\>\[\]]+\s+(\w+)
    // array regex including brackets :(int|double|String|boolean|char) *[\w\<\>\[\]]+\s+(\w+) ( ?=.*)?;

    //regex for everything - (int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+)
    //regex for methods - (int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+) *\([^\)]*\) *(\{?|[^;])
    //regex for all members  -(int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+) *( ?=.*)?;
    //public static final String METHOD_DEC = "(int|double|String|boolean|char) " +
    //        "*[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])"; //Remove last part if we don't want {





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
