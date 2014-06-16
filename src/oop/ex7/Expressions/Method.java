package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.MethodBadArgsCountException;
import oop.ex7.Expressions.Exceptions.MethodBadArgsException;
import oop.ex7.Expressions.Exceptions.MethodTypeMismatchException;
import oop.ex7.Expressions.Exceptions.VariableTypeException;
import oop.ex7.Logic.Exceptions.ExistingVariableName;

import java.util.LinkedHashMap;

/**
 * Created by Oded on 11/6/2014.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private LinkedHashMap<String, Expression> allExpressions;
    private LinkedHashMap<String, Expression> headerExpressions;
    private boolean m_isArray = false;


    /**
     *
     * @param returnType VariableType that the method will return. If void, should be "void"
     * @param methodName
     * @param args
     * @throws oop.ex7.Expressions.Exceptions.VariableTypeException
     * @throws oop.ex7.Expressions.Exceptions.MethodBadArgsException
     */
    public Method (String returnType, String methodName, String args) throws VariableTypeException,
            MethodBadArgsException, ExistingVariableName {
        // May be redundant due to always needing reference to the allExpressions outside of this method's scope.
        type = VariableEnum.toEnum(returnType);

        //OR - modified (removed '()' )
        name = methodName.trim(); // Save a method name with parentheses to deal with variable and methods having
                                         // possibly the same name.
        if(!args.equals("")){
            headerExpressions = SetVariables(args);
            allExpressions = new LinkedHashMap<String, Expression>(headerExpressions);
        }
        else{
            headerExpressions = new LinkedHashMap<String, Expression>();
            allExpressions = new LinkedHashMap<String, Expression>();
        }

    }

    /**
     * Copy ctor
     * @param method
     */
    public Method(Method method){
        type = method.getType();
        name = method.getName();
        allExpressions = method.getAllExpressions();
        headerExpressions = method.getParams();
    }

    public Method (String returnType, String methodName, String args, LinkedHashMap<String ,
            Expression> outsideExpressions) throws VariableTypeException, MethodBadArgsException, ExistingVariableName {
        this(returnType, methodName, args);
        allExpressions.putAll(outsideExpressions);
    }

    public Method(String returnType, String methodName, String args, boolean isReturnArray)
            throws VariableTypeException, MethodBadArgsException, ExistingVariableName {
        this(returnType, methodName, args);
        m_isArray = true;
    }

    private LinkedHashMap<String, Expression> SetVariables(String args) throws VariableTypeException,
            MethodBadArgsException, ExistingVariableName {
        if (args.trim().endsWith(",")) {
            throw new MethodBadArgsException();
        }
        String[] arguments = args.split(",");
        String[] currentArgument;
        String argument;
        LinkedHashMap<String,Expression> newVariables= new LinkedHashMap<String, Expression>();
        for (String arg: arguments) {
            argument = arg.trim();
            currentArgument = argument.split(" ");
            String variableName = currentArgument[1];
            if(ExpressionTypeEnum.checkType(argument + ";") != ExpressionTypeEnum.MEM_DECLARATION){
                throw new MethodBadArgsException();
            }
            if(newVariables.containsKey(variableName)){ //
                throw new ExistingVariableName();
            }
            //check if it is an array
            if(currentArgument[0].matches(ExpressionTypeEnum.ARRAY_TYPE_REGEX)){ //this is an array
                String type = currentArgument[0].substring(0, currentArgument[0].indexOf("["));
                newVariables.put(variableName, new Variable(type, variableName, true, true));
            }
            else{ //normal declaration
                newVariables.put(variableName, new Variable(currentArgument[0], variableName, true));
            }
            /*
            if (argument.matches(VariableEnum.Types(false)+"\\s+([a-zA-Z_]+)([\\w]*)")) {
                currentArgument = argument.split(" ");
                if(newVariables.containsKey(currentArgument[1])) //Member with the same name already exists
                    throw new ExistingVariableName();
                newVariables.put(currentArgument[1], new Variable(currentArgument[0], currentArgument[1], true));
            } else {
                throw new MethodBadArgsException();
            }
            */
        }
        return newVariables;
    }

    public void AddVariable (Variable variable) {
        if (!allExpressions.containsValue(variable)) {
            allExpressions.put(variable.getName(), variable);
        }
    }

    public boolean ValidateHeader (VariableEnum[] headerTypes) throws MethodBadArgsCountException, MethodTypeMismatchException {
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

    public LinkedHashMap<String, Expression> getParams(){
        return headerExpressions;
    }

    public boolean isArray(){
        return m_isArray;
    }
}
