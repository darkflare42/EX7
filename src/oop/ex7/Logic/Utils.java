package oop.ex7.Logic;

import oop.ex7.Expressions.Expression;
import oop.ex7.Expressions.VariableEnum;
import sun.security.krb5.Config;

import java.util.LinkedHashMap;

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
}
