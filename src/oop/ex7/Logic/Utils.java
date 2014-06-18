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
 * Created by Or Keren on 15/06/14.
 */
public class Utils {

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

    public static boolean DoubleTryParse(String value){
        try{
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException ex){
            return false;
        }
    }

    public static boolean StringTryParse(String value){
        try {
            value = value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\""));
            return true;
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }

    }

    public static boolean CharTryParse(String value){
        try{
            value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));
            return value.length() <= 1;
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }
    }

    public static boolean BooleanTryParse(String value){
        return !(!value.equals("false") && !value.equals("true"));

    }

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

    public static LinkedHashMap<String, Expression> mergeExpressions(LinkedHashMap<String, Expression> main,
                                                                     LinkedHashMap<String, Expression> secondary){
        LinkedHashMap<String, Expression> tempMap = new LinkedHashMap<String, Expression>();
        tempMap.putAll(main);
        tempMap.putAll(secondary);
        return tempMap;
    }

    public static boolean ValidArrayDeclaration(String string) throws InvalidArrayMembersDeclaration{
        if (string.equals("")) {
            return true;
        }
        if (string.trim().endsWith(",")) {
            throw new InvalidArrayMembersDeclaration();
        }
        return true;
    }

    public static Matcher validateVariableName(String variable) throws UnknownCodeLineException, InvalidNameException {
        String value;
        Matcher matcher = ExpressionTypeEnum.MEMBER_DECLARATION_PATTERN.matcher(variable);
        if(matcher.lookingAt()){ //this is a member declaration

            value = matcher.group(3);
        }
        else{
            matcher = ExpressionTypeEnum.ARRAY_DECLARATION_PATTERN.matcher(variable);
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

    public static String getArgsInBrackets(String line){
        String arguments = "";
        int indexOfBrackets = line.indexOf("(");
        if(indexOfBrackets != -1){ //This is a function call
            arguments = line.substring(indexOfBrackets+1, line.lastIndexOf(")"));
            return arguments;
        }
        indexOfBrackets = line.indexOf("{");
        if(indexOfBrackets != -1){ //This is an array declaration
            arguments = line.substring(indexOfBrackets+1, line.lastIndexOf("}"));
            return arguments;
        }
        indexOfBrackets = line.indexOf("[");
        if(indexOfBrackets != -1){
            arguments = line.substring(indexOfBrackets+1, line.lastIndexOf("]"));
            return arguments;
        }
        return arguments;
    }

    public static void checkValidIndexValue(String indexValue) throws InvalidArrayIndexException {
        if(!indexValue.matches(RegexConfig.OPERATION_REGEX)){ //check only if value is a single digit
            if(Utils.IntegerTryParse(indexValue) && Integer.parseInt(indexValue) < 0) //check if it is a non zero number
                throw new InvalidArrayIndexException();
        }
    }
}
