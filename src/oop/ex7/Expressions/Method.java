package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.MethodBadArgsCountException;
import oop.ex7.Expressions.Exceptions.MethodBadArgsException;
import oop.ex7.Expressions.Exceptions.MethodTypeMismatchException;
import oop.ex7.Expressions.Exceptions.VariableTypeException;
import oop.ex7.Logic.Exceptions.ExistingVariableName;

import java.util.LinkedHashMap;

/**
 * Class to represent a method Expression.
 * Contains members for its' name, the type it returns, if the return type is an array all the Expressions it is
 * aware of, and all the Expressions that were declared when the method is created.
 */
public class Method implements Expression {
    private VariableEnum type;
    private String name;
    private LinkedHashMap<String, Expression> allExpressions;
    private LinkedHashMap<String, Expression> headerExpressions;
    private boolean m_isArray = false;


    /**
     * Standard constructor - Creates a method object be defining a return type, a name and the argumnts declared in it's header.
     * @param returnType String of the type the method returns, "void" if none.
     * @param methodName String of the name of the method.
     * @param args String of the members declared in the method declaration.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     * @throws ExistingVariableName args is declaring a member with a name of an already existing member.
     */
    public Method (String returnType, String methodName, String args) throws VariableTypeException,
            MethodBadArgsException, ExistingVariableName {
        type = VariableEnum.toEnum(returnType);
        name = methodName.trim();
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
     * Copy constructor.
     * @param method Method to copy.
     */
    public Method(Method method){
        type = method.getType();
        name = method.getName();
        allExpressions = new LinkedHashMap<>(method.getAllExpressions());
        headerExpressions = new LinkedHashMap<>(method.getParams());
    }

    /**
     * Overload constructor to determine if if the Method returns an array of it's return type.
     * @param returnType String of the type the method returns, "void" if none.
     * @param methodName String of the name of the method.
     * @param args String of the members declared in the method declaration.
     * @param isReturnArray boolean if the method returns an array of the returnType.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     * @throws ExistingVariableName args is declaring a member with a name of an already existing member.

     */
    public Method(String returnType, String methodName, String args, boolean isReturnArray)
            throws VariableTypeException, MethodBadArgsException, ExistingVariableName {
        this(returnType, methodName, args);
        m_isArray = true;
    }

    /**
     * Set the collection of Variables from a string that represents Variables initializations.
     * @param args String of of Variable declarations inside the parentheses in the method header.
     * @return LinkedHashMap of all the header declared Variables.
     * @throws VariableTypeException args contains a Variable declaration with an invalid Variable type.
     * @throws MethodBadArgsException args has an invalid structure.
     * @throws ExistingVariableName args is declaring a member with a name of an already existing member.
     */
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
            boolean isArray = false;
            argument = arg.replace("\\s+", " ").trim();
            argument = argument.replace(" []", "[] "); // TODO TESTER 137 best fix in the world
            currentArgument = argument.split(" ");  //TODO this can be a lot cleaner but im getting lost in ExpressionTypeEnum
                                                    //TODO instead of splitting by 'space', which is a bad idea, should look into matching patterns
                                                    //TODO and dividing into groups of the match.
                                                    //TODO this breaks 424, at least.
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
        }
        return newVariables;
    }

    // TODO might be redundant.
    public void AddVariable (Variable variable) {
        if (!allExpressions.containsValue(variable)) {
            allExpressions.put(variable.getName(), variable);
        }
    }

    /**
     * Given an array of VariableEnums, checks its' validity, in order, against the headerExpressions LinkedHashMap.
     * @param headerTypes an array of VariableEnums.
     * @return true if the number of types and the types all match.
     * @throws MethodBadArgsCountException headerTypes has an invalid amount of values.
     * @throws MethodTypeMismatchException headerTypes has a value that mismatches headerExpressions.
     */
    public boolean ValidateHeader (VariableEnum[] headerTypes) throws MethodBadArgsCountException, MethodTypeMismatchException {
        if(headerTypes[0] == VariableEnum.VOID && headerExpressions.size() == 0) //no params
            return true;
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
     * Returns the method's name.
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Return the method's return type.
     * @return type
     */
    public VariableEnum getType () {
        return type;
    }

    /**
     * Return if the method's is initialized. Always true since a declarated method can be called.
     * @return true
     */
    public boolean isInitialized () {
        return true;
    }

    /**
     * Merge the current inner expressions with the global expressions.
     * @param globalExpressions LinkedHashMap expressions to merge.
     */
    public void mergeAllExpressions(LinkedHashMap<String, Expression> globalExpressions){
        allExpressions.putAll(globalExpressions);
    }

    /**
     * Return all the expressions the method recognizes.
     * @return allExpressions
     */
    public LinkedHashMap<String, Expression> getAllExpressions() {
        return allExpressions;
    }

    /**
     * Return the Variables declared in the method's header.
     * @return headerExpressions.
     */
    public LinkedHashMap<String, Expression> getParams(){
        return headerExpressions;
    }

    /**
     * Return a boolean if the method returns an array.
     * @return m_isArray
     */
    public boolean isArray(){
        return m_isArray;
    }

    /**
     * Return a boolean if the method can be accessed globally. Always true.
     * @return true
     */
    public boolean isGlobal(){return true;}
}
