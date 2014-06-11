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
        variables = SetVariables(args);
    }

    private ArrayList<Variable> SetVariables(String args) throws VariableTypeException, MethodBadArgsException{
        String[] arguments = args.split(",");
        String[] currentArgument;
        String argument;
        ArrayList<Variable> newVariables= new ArrayList<Variable>();
        for (String arg: arguments) {
            argument = arg.trim();
            if (argument.matches(VariableEnum.TYPES+"\\s+([a-zA-Z_]+)([\\w]*)")) {
                currentArgument = argument.split(" ");
                newVariables.add(new Variable(currentArgument[0], currentArgument[1]));
            } else {
                throw new MethodBadArgsException();
            }
        }
        return newVariables;
    }

    public void AddVariable (Variable variable) {
        if (!variables.contains(variable)) {
            variables.add(variable);
        }
    }

    public String getName () {
        return name;
    }

    public VariableEnum getType () {
        return type;
    }
}
