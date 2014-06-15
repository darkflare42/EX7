package oop.ex7;

import java.util.LinkedHashMap;

/**
 * Created by Oded on 11/6/2014.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private LinkedHashMap<String, Expression> allExpressions;
    private LinkedHashMap<String, Expression> headerExpressions;


    /**
     *
     * @param returnType VariableType that the method will return. If void, should be "void"
     * @param methodName
     * @param args
     * @throws VariableTypeException
     * @throws MethodBadArgsException
     */
    public Method (String returnType, String methodName, String args) throws VariableTypeException, MethodBadArgsException{
        // May be redundant due to always needing reference to the allExpressions outside of this method's scope.
        if (returnType.equals("void")) {
            type = null;
        } else {
            type = VariableEnum.toEnum(returnType);
        }
        name = methodName.trim() + "()"; // Save a method name with parentheses to deal with variable and methods having
                                         // possibly the same name.
        headerExpressions = SetVariables(args);
        allExpressions = new LinkedHashMap<String, Expression>(headerExpressions);

    }

    public Method (String returnType, String methodName, String args, LinkedHashMap<String,Expression> outsideExpressions) throws VariableTypeException, MethodBadArgsException{
        this(returnType, methodName, args);
        allExpressions.putAll(outsideExpressions);
    }

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

    public void AddVariable (Variable variable) {
        if (!allExpressions.containsValue(variable)) {
            allExpressions.put(variable.getName(), variable);
        }
    }

    public boolean ValidateHeader (VariableEnum[] hearderTypes) throws MethodBadArgsCountException, MethodTypeMismatchException{
        if (headerExpressions.size() != hearderTypes.length) {
            throw new MethodBadArgsCountException();
        }
        int i = 0;
        for (Expression innerExpression : headerExpressions.values()) {
            if (innerExpression.getType() != hearderTypes[i]) {
                throw new MethodTypeMismatchException();
            }
            i++;
        }
        return true;
    }

    public String getName () {
        return name;
    }

    public VariableEnum getType () {
        return type;
    }

    public boolean isInitialized () {
        return true;
    }

    public LinkedHashMap<String, Expression> getAllExpressions() {
        return allExpressions;
    }
}
