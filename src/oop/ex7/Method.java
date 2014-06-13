package oop.ex7;

import java.util.ArrayList;

/**
 * Created by Oded on 11/6/2014.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private ArrayList<Expression> expressions;


    public Method (String returnType, String methodName, String args) throws VariableTypeException, MethodBadArgsException{
        // May be redundant due to always needing reference to the expressions outside of this method's scope.
        if (returnType.equals("null")) {
            type = null;
        } else {
            type = VariableEnum.toEnum(returnType);
        }
        name = methodName.trim() + "()"; // Save a method name with parentheses to deal with variable and methods having
                                         // possibly the same name.
        expressions = SetVariables(args);
    }

    public Method (String returnType, String methodName, String args, ArrayList<Expression> outsideExpressions) throws VariableTypeException, MethodBadArgsException{
        this(returnType, methodName, args);
        expressions.addAll(outsideExpressions);
    }

    private ArrayList<Expression> SetVariables(String args) throws VariableTypeException, MethodBadArgsException{
        String[] arguments = args.split(",");
        String[] currentArgument;
        String argument;
        ArrayList<Expression> newVariables= new ArrayList<Expression>();
        for (String arg: arguments) {
            argument = arg.trim();
            if (argument.matches(VariableEnum.Types()+"\\s+([a-zA-Z_]+)([\\w]*)")) {
                currentArgument = argument.split(" ");
                newVariables.add(new Variable(currentArgument[0], currentArgument[1], true));
            } else {
                throw new MethodBadArgsException();
            }
        }
        return newVariables;
    }

    public void AddVariable (Variable variable) {
        if (!expressions.contains(variable)) {
            expressions.add(variable);
        }
    }

    public String getName () {
        return name;
    }

    public VariableEnum getType () {
        return type;
    }

    public ArrayList<Expression> getExpressions() {
        return expressions;
    }
}
