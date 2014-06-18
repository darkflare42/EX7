package oop.ex7.Logic;

import oop.ex7.Expressions.Exceptions.InvalidArrayIndexException;
import oop.ex7.Expressions.Expression;
import oop.ex7.Expressions.ExpressionTypeEnum;
import oop.ex7.Expressions.VariableEnum;
import oop.ex7.Logic.Exceptions.InvalidArrayMembersDeclaration;
import oop.ex7.Logic.Exceptions.InvalidNameException;
import oop.ex7.Logic.Exceptions.UnknownCodeLineException;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;

/**
 * This class holds several utility methods which are used throughout the code
 */
public class Utils {

    /**
     * Tries to parse the value to an int
     * @param value The value to try and parse
     * @return True if it parses, False if it doesn't
     */
    public static boolean IntegerTryParse(String value){
        try{
            if(!value.matches(RegexConfig.INT_CALL_REGEX))
                return false;
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ex){
            return false;
        }
    }

    /**
     * Tries to parse the value to a double
     * @param value The value to try and parse
     * @return True if it parses, False if it doesn't
     */
    public static boolean DoubleTryParse(String value){
        try{
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException ex){
            return false;
        }
    }

    /**
     * Tries to parse the value to an string
     * @param value The value to try and parse
     * @return True if it parses, False if it doesn't
     */
    public static boolean StringTryParse(String value){
        try {
            value = value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\""));
            return true;
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }

    }

    /**
     * Tries to parse the value to an char
     * @param value The value to try and parse
     * @return True if it parses, False if it doesn't
     */
    public static boolean CharTryParse(String value){
        try{
            value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));
            return value.length() <= 1;
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }
    }

    /**
     * Tries to parse the value to a boolean
     * @param value The value to try and parse
     * @return True if it parses, False if it doesn't
     */
    public static boolean BooleanTryParse(String value){
        return (value.matches(RegexConfig.BOOLEAN_VALUES));
    }

    /**
     * This funcion receives an actual value and tries to parse it to the supported types
     * @param value The actual value to parse
     * @return The enum of the type of the value
     */
    public static VariableEnum getValueEnum(String value){
        if(IntegerTryParse(value))
            return VariableEnum.INT;
        if(DoubleTryParse(value))
            return VariableEnum.DOUBLE;
        if(StringTryParse(value))
            return VariableEnum.STRING;
        if(CharTryParse(value))
            return VariableEnum.CHAR;
        if(BooleanTryParse(value))
            return VariableEnum.BOOLEAN;
        return VariableEnum.VOID;
    }

    /**
     * This function merges between two LinkedHashMaps
     * @param main The main linkedHashMap
     * @param secondary The secondary Linked HashMap
     * @return The merged linkedHashMap
     */
    public static LinkedHashMap<String, Expression> mergeExpressions(LinkedHashMap<String, Expression> main,
                                                                     LinkedHashMap<String, Expression> secondary){
        LinkedHashMap<String, Expression> tempMap = new LinkedHashMap<String, Expression>();
        tempMap.putAll(main);
        tempMap.putAll(secondary);
        return tempMap;
    }

    /**
     * This function "dumb" validates an array declaration - it checks if the string inside the {} is valid
     * @param paramString The string of the parameters
     * @return True if the param string is valid
     * @throws InvalidArrayMembersDeclaration if the params are not in a valid format
     */
    public static boolean validArrayDeclaration(String paramString) throws InvalidArrayMembersDeclaration{
        if (paramString.equals("")) {
            return true;
        }
        if (paramString.trim().endsWith(",")) {
            throw new InvalidArrayMembersDeclaration();
        }
        return true;
    }

    /**
     * This function validates a variable declaration - it checks if it is a valid array or member declaration
     * @param variableDeclaration The declaration line
     * @return A regex Matcher that represents the declaration of the variable
     * @throws UnknownCodeLineException If we encountered an invalid declaration
     * @throws InvalidNameException If the name of the variable is not valid (i.e it has a whitespace in it)
     */
    public static Matcher validateVariableDeclaration(String variableDeclaration) throws UnknownCodeLineException,
            InvalidNameException {
        String value;
        Matcher matcher = ExpressionTypeEnum.MEMBER_DECLARATION_PATTERN.matcher(variableDeclaration);
        if(matcher.lookingAt()){ //this is a member declaration

            value = matcher.group(3);
        }
        else{
            matcher = ExpressionTypeEnum.ARRAY_DECLARATION_PATTERN.matcher(variableDeclaration);
            if(matcher.lookingAt()){ //This is an array declaration

                value = matcher.group(3);
            }
            else{
                throw new UnknownCodeLineException();
            }
        }
        if(value == null) value = "";

        if(!value.equals("") && !value.contains("=")) throw new InvalidNameException();
        return matcher;
    }

    /**
     * Check the validity of a variable or a method name.
     * @param variableName String to check.
     * @return true iff the string is valid in sjava.
     */
    public static boolean checkValidVariableName(String variableName){
        return  (variableName.matches(RegexConfig.VALID_NAME) && !RegexConfig.isForbiddenWord(variableName));
    }

    /**
     * This function strips a name (removes brackets)
     * @param name The name of the member or function to strip
     * @return The stripped name
     */
    public static String stripName(String name){
        name = name.replace("-", "");
        int indexOfBracket = name.indexOf("(");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
            return name;
        }
        indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        return name;
    }

    /**
     * This function retrieves the values inside brackets {} () and [] are supported only
     * @param paramsWithBrackets The parameters inside their brackets
     * @return The params without the brackets
     */
    public static String getArgsInBrackets(String paramsWithBrackets){
        String arguments = "";
        int indexOfBrackets = paramsWithBrackets.indexOf("(");
        if(indexOfBrackets != -1){ //This is a function call
            arguments = paramsWithBrackets.substring(indexOfBrackets+1, paramsWithBrackets.lastIndexOf(")"));
            return arguments;
        }
        indexOfBrackets = paramsWithBrackets.indexOf("{");
        if(indexOfBrackets != -1){ //This is an array declaration
            arguments = paramsWithBrackets.substring(indexOfBrackets+1, paramsWithBrackets.lastIndexOf("}"));
            return arguments;
        }
        indexOfBrackets = paramsWithBrackets.indexOf("[");
        if(indexOfBrackets != -1){
            arguments = paramsWithBrackets.substring(indexOfBrackets+1, paramsWithBrackets.lastIndexOf("]"));
            return arguments;
        }
        return arguments;
    }

    /**
     * This function checks if an index value is a valid index value - it checks only math operations between direct
     * values and single direct values. If it is a single direct value it also checks if it is zero
     * @param indexValue
     * @return true if it is a valid index, false if it isn't
     */
    public static boolean checkValidIndexValue(String indexValue) throws InvalidArrayIndexException {
        if(!indexValue.matches(RegexConfig.OPERATION_REGEX)){ //check only if value is a single digit
            if(Utils.IntegerTryParse(indexValue) && Integer.parseInt(indexValue) < 0) //check if it is a non zero number
                return false;
        }
        return true;
    }
}
