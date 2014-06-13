package oop.ex7.Expressions;

import oop.ex7.VariableEnum;

/**
 * Created by Or Keren on 10/06/14.
 */
public enum ExpressionTypeEnum {
    MEM_DECLARATION,
    METHOD_DECLARATION,
    BLOCK_START{
        public String toString(){
            return "{";
        }
    },
    BLOCK_END,
    UNKNOWN;

    public static final char BLOCK_START_CHAR = '{';  //Has to be constant expression
    public static final char BLOCK_END_CHAR = '}';
    public static final char END_OF_LINE_CHAR = ';';

    public static final String NAME="_?[a-zA-Z][_\\w]*";
    public static final String MEMBER_DECLARATION_REGEX = VariableEnum.Types()+" "+NAME+"( ?=\\w.*)?;$";
    public static final String METHOD_DECLARATION_REGEX = VariableEnum.TypesInclVoid() +" [a-zA-Z][_\\w]* ?\\(.*\\) ?\\" +
            BLOCK_START_CHAR;



    // array regex - (int|double|String|boolean|char) *[\w\<\>\[\]]+\s+(\w+)
    // array regex including brackets :(int|double|String|boolean|char) *[\w\<\>\[\]]+\s+(\w+) ( ?=.*)?;

    //regex for everything - (int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+)
    //regex for methods - (int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+) *\([^\)]*\) *(\{?|[^;])
    //regex for all members  -(int|double|String|boolean|char)*[\w\<\>\[\]]+\s+(\w+) *( ?=.*)?;
    //public static final String METHOD_DEC = "(int|double|String|boolean|char) " +
    //        "*[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])"; //Remove last part if we don't want {





    public static ExpressionTypeEnum checkType(String line){
        switch(line.charAt(line.length()-1)){ //Check the last character in the line
            case BLOCK_START_CHAR: //Method declaration/block
                if(line.matches(METHOD_DECLARATION_REGEX))
                    return METHOD_DECLARATION;

            case BLOCK_END_CHAR: //End of method/block
                return BLOCK_END;

            case END_OF_LINE_CHAR: //member declaration or unknown
                if(line.matches(MEMBER_DECLARATION_REGEX)) //TODO: Add array
                    return MEM_DECLARATION;

            default:
                return UNKNOWN;
        }


    }


}
