package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.*;
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
     * This constructor is called during the creation of a method (when a method declaration is encountered)
     * It sets all the relevant members according to the parameters
     * @param returnType The return type of the method
     * @param methodName The name of the method
     * @param params The parameters that the method receives
     * @param isReturnArray If this returns an array or not
     * @throws VariableTypeException
     */
    public Method(String returnType, String methodName, LinkedHashMap<String, Expression> params,
                  boolean isReturnArray) throws VariableTypeException {
        type = VariableEnum.toEnum(returnType);
        name = methodName.trim();
        headerExpressions = params;
        m_isArray = isReturnArray;
        allExpressions = new LinkedHashMap<>(params);

    }


    /**
     * Given an array of VariableEnums, checks its' validity, in order, against the headerExpressions LinkedHashMap.
     * @param headerTypes an array of VariableEnums.
     * @return true if the number of types and the types all match.
     * @throws MethodBadArgsCountException headerTypes has an invalid amount of values.
     * @throws MethodTypeMismatchException headerTypes has a value that mismatches headerExpressions.
     */
    public boolean ValidateHeader (VariableEnum[] headerTypes) throws MethodBadArgsCountException,
            MethodTypeMismatchException {
        if(headerTypes[0] == VariableEnum.VOID && headerExpressions.size() == 0) //no params
            return true;
        if (headerExpressions.size() != headerTypes.length) {
            throw new MethodBadArgsCountException();
        }
        int i = 0;
        for (Expression innerExpression : headerExpressions.values()) {
            if (i >= headerTypes.length || innerExpression.getType() != headerTypes[i]) {
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
    @Override
    public String getName () {
        return name;
    }

    /**
     * Return the method's return type.
     * @return type
     */
    @Override
    public VariableEnum getType () {
        return type;
    }

    /**
     * Return if the method's is initialized. Always true since a declarated method can be called.
     * @return true
     */
    @Override
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
     * Assign a value to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign VariableEnum to assign to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of
     * the Variable.
     */
    @Override
    public void Assign (VariableEnum assign) throws AssignMismatchException{
        if (type == VariableEnum.VOID) {
            throw new AssignMismatchException();
        }
        if(type == assign) {
            return;
        }
        if(type == VariableEnum.DOUBLE && assign == VariableEnum.INT) {
            return;
        }
        throw new AssignMismatchException();
    }

    /**
     * Assign the value of an expression to a Variable. Initializes the Variable (if it was not initialized).
     * @param assign Expression to assign its' value to the variable.
     * @throws oop.ex7.Expressions.Exceptions.AssignMismatchException if assign is a type that mismatches the type of
     * the Variable.
     * @throws VariableUninitializedException if assign is not an initialized Expression.
     */
    @Override
    public void Assign (Expression assign) throws AssignMismatchException, VariableUninitializedException {
        if (!assign.isInitialized()) {
            throw new VariableUninitializedException();
        }
        Assign(assign.getType());
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
    @Override
    public boolean isArray(){
        return m_isArray;
    }

    /**
     * Return a boolean if the method can be accessed globally. Always true.
     * @return true
     */
    @Override
    public boolean isGlobal(){return true;}
}
