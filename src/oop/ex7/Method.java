package oop.ex7;

import java.awt.*;
import java.util.LinkedHashMap;

/**
 * Class to represent a method Expression.
 * Contains members for its' name, the type it returns, all the Expressions it is aware of, and all the Expressions
 * that were declared when the method is created.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private LinkedHashMap<String, Expression> allExpressions;
    private LinkedHashMap<String, Expression> headerExpressions;


    /**
     * Standard constructor.
     * @param returnType VariableEnum that the method returns.
     * @param methodName Method name.
     * @param args String of of Variable declarations inside the parentheses in the method header.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     */
    public Method (String returnType, String methodName, String args) throws VariableTypeException, MethodBadArgsException{
        // May be redundant due to always needing reference to the allExpressions outside of this method's scope.
        if (returnType.equals("void")) {
            type = null;
        } else {
            type = VariableEnum.toEnum(returnType);
        }
        name = methodName.trim() + "()"; // Save a method name with parentheses to deal with variables and methods having
                                         // possibly the same name.
        headerExpressions = SetVariables(args);
        allExpressions = new LinkedHashMap<String, Expression>(headerExpressions);

    }

    /**
     * A constructor that also receives a LinkedHashMap of all the expressions that it can refer to.
     * @param returnType VariableEnum that the method returns.
     * @param methodName Method name.
     * @param args String of of Variable declarations inside the parentheses in the method header.
     * @param outsideExpressions LinkedHashMap that the method can refer to in addition to declared variables.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     */
    public Method (String returnType, String methodName, String args, LinkedHashMap<String,Expression> outsideExpressions) throws VariableTypeException, MethodBadArgsException{
        this(returnType, methodName, args);
        allExpressions.putAll(outsideExpressions);
    }

    /**
     * Set the collection of Variables from a string that represents Variables initializations.
     * @param args String of of Variable declarations inside the parentheses in the method header.
     * @return LinkedHashMap of all the header declared Variables.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     */
    private LinkedHashMap<String, Expression> SetVariables(String args) throws VariableTypeException, MethodBadArgsException{
        String[] arguments = args.split(",");
        String[] currentArgument;
        String argument;
        LinkedHashMap<String,Expression> newVariables= new LinkedHashMap<String, Expression>();
        for (String arg: arguments) {
            argument = arg.trim();
            if (argument.matches(VariableEnum.Types()+"\\s+([a-zA-Z_]+)([\\w]*)")) {
                currentArgument = argument.split(" ");
                newVariables.put(currentArgument[1], new Variable(currentArgument[0], currentArgument[1], true));
            } else {
                throw new MethodBadArgsException();
            }
        }
        return newVariables;
    }

    /**
     * Add an expression to the LinkedHashMap collection of the method.
     * @param expression
     */
    public void AddVariable (Expression expression) {
        if (!allExpressions.containsValue(expression)) {
            allExpressions.put(expression.getName(), expression);
        }
    }

    /**
     * Given an array of VariableEnums, checks its' validity, in order, against the headerExpressions LinkedHashMap
     * @param headerTypes an array of VariableEnums
     * @return true if the number of types and the types all match.
     * @throws MethodBadArgsCountException headerTypes has an invalid amount of values.
     * @throws MethodTypeMismatchException headerTypes has a value that mismatches headerExpressions.
     */
    public boolean ValidateHeader (VariableEnum[] headerTypes) throws MethodBadArgsCountException, MethodTypeMismatchException{
        if (headerExpressions.size() != headerTypes.length) {
            throw new MethodBadArgsCountException();
        }
        int i = 0;
        for (Expression innerExpression : headerExpressions.values()) {
            if (innerExpression.getType() != headerTypes[i]) {
                throw new MethodTypeMismatchException();
            }
            i++;
        }
        return true;
    }

    /**
     * Returns the Method's name.
     * @return Method's name.
     */
    public String getName () {
        return name;
    }

    /**
     * Returns the Method's VariableType.
     * @return type.
     */
    public VariableEnum getType () {
        return type;
    }

    /**
     *
     * To comply with the Expression interface, returns true, as a method can't be uninitialized.
     * @return boolean true.
     */
    // TODO might be a little bit ridiculous.
    public boolean isInitialized () {
        return true;
    }

    /**
     * Returns a LinkedHashMap of all the Expressions the method can refer to.
     * @return allExpressions.
     */
    public LinkedHashMap<String, Expression> getAllExpressions() {
        return allExpressions;
    }

    /**
     * Returns a LinkedHashMap of the Expressions declared in it's header.
     * @return headerExpressions.
     */
    public LinkedHashMap<String, Expression> getHeaderExpressions () {
        return headerExpressions;
    }
}
