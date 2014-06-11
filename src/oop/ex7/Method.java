package oop.ex7;

import java.util.ArrayList;

/**
 * Created by Oded on 11/6/2014.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private ArrayList<Variable> variables;


    public Method (String returnType, String methodName, String args) throws VariableTypeException, MethodBadArgsException{
        if (returnType.equals("null")) {
            type = null;
        } else {
            type = VariableEnum.toEnum(returnType);
        }
        name = methodName;
        SetVariables(args);
    }

    private void SetVariables(String args) throws VariableTypeException, MethodBadArgsException{
        args = args.trim();
        if (args.endsWith(",")) {
            throw new MethodBadArgsException();
        }
        String[] arguments = args.split(",");
        String[] currentArgument;
        String argument;
        for (String arg: arguments) {
            argument = arg.trim();
            if (argument.matches(VariableEnum.TYPES+"\\s+([a-zA-Z_]+)([\\w]*)")) {
                currentArgument = argument.split(" ");
                variables.add(new Variable(currentArgument[0], currentArgument[1]));
            } else {
                throw new MethodBadArgsException();
            }
        }
    }

    public String getName () {
        return name;
    }

    public VariableEnum getType () {
        return type;
    }
}
