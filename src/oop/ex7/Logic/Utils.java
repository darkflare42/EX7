package oop.ex7.Logic;

import oop.ex7.Expressions.Expression;
import oop.ex7.Expressions.ExpressionTypeEnum;
import oop.ex7.Expressions.VariableEnum;
import oop.ex7.Logic.Exceptions.InvalidArrayMembersDeclaration;
import oop.ex7.Logic.Exceptions.InvalidNameException;
import oop.ex7.Logic.Exceptions.UnknownCodeLineException;
import sun.security.krb5.Config;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;

/**
 * Created by Or Keren on 15/06/14.
 */
public class Utils {

    public static boolean IntegerTryParse(String value){
        try{
            if(!value.matches(CONFIG.VALUE_REGEX))
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
            if(value.length() > 1){
                return false;
            }
            return true;
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }
    }

    public static boolean BooleanTryParse(String value){
        if(!value.equals("false") && !value.equals("true")){//TODO: Check
                    return false;
        }
        return true;

    }

    public static VariableEnum getValueEnum(String value){
        // TODO this functionality is available at the VariableEnum class, without the "void" support.
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

    public static boolean validateArrayTypes(String args, VariableEnum arrayType){
        if(args.equals(""))
            return true;
        String[] arguments = args.split(",");
        for(String argument: arguments){
            argument = argument.trim();
            VariableEnum argType = getValueEnum(argument);
            if(!VariableEnum.checkValidAssignment(arrayType, argType))
                return false;
        }
        return true;
    }

    //TODO shouldnt this be a static method in Variable? or in config?
    public static boolean checkValidVariableName(String variableName){

        if(!variableName.matches(CONFIG.VALID_NAME))
            return  false;
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
        }
        indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        return name;
    }
}
