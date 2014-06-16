package oop.ex7.Logic;

import oop.ex7.Expressions.*;
import oop.ex7.Expressions.Exceptions.*;
import oop.ex7.Logic.Exceptions.*;
import oop.ex7.Reader.FileReader;
import oop.ex7.Reader.IOException;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;


/**
 * This class passes over the s-java class, line by line and checks for valid method and member declarations only
 * Created by Or Keren on 13/06/14.
 */
public class SyntaxCompiler {

    private static LinkedHashMap<String, Expression> m_MemberMap;
    private static LinkedHashMap<String, Expression> m_MethodMap;

    /**
     * This method passes over the file and checks for compilation errors
     * @param reader
     * @throws CompilationException
     * @throws IOException
     */
    public static void compile(FileReader reader) throws CompilationException, IOException{
        m_MemberMap = new LinkedHashMap<String, Expression>();
        m_MethodMap = new LinkedHashMap<String, Expression>();
        
        compileMethods(reader);  //Call for methods first so that we can check variable assignment during this phase
        reader.reset();  //Move "pointer" to start of file
        compileMembers(reader); //Call for member compilation


        //If we have declared methods - run a deep check
        if(!m_MethodMap.isEmpty()){
            reader.reset();

            reader.moveToFirstMethodDeclaration();
            LinkedHashMap<String, Expression> globalValues = Utils.mergeExpressions(m_MemberMap, m_MethodMap);
            for(String methodName: m_MethodMap.keySet()){
                ((Method)m_MethodMap.get(methodName)).mergeAllExpressions(globalValues);
                 validateMethodBlock(reader, methodName, (Method) m_MethodMap.get(methodName));
                reader.moveToEndOfMethod();
                if(reader.hasNext())reader.next();
            }
        }

    }

    /**
     * This method runs over all the file and checks methods only for compilation errors
     * @param reader The FileReader that reads through the current file
     */
    private static void compileMethods(FileReader reader) throws VariableTypeException, MethodBadArgsException,
            ExistingMethodNameException, UnkownCodeLineException, ExistingVariableName, InvalidNameException {
        String currLine;
        while(reader.hasNext()){
            currLine = reader.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case METHOD_DECLARATION:
                    validateMethodDeclaration(currLine);
                    reader.moveToEndOfMethod();
                    break;
                case UNKNOWN:
                    throw new UnkownCodeLineException();


            }
        }
    }

    /**
     * This method runs over all the file and checks members only for compilation errors
     * @param reader The FileReader that reads through the current file
     * @throws CompilationException
     */
    private static void compileMembers(FileReader reader) throws CompilationException{
        String currLine;
        while(reader.hasNext()){
            currLine = reader.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case MEM_DECLARATION:
                    validateMemberDeclaration(currLine, m_MemberMap, true);
                     //move to the end of the function block, we don't do a deep check now
                    break;
                case METHOD_DECLARATION:
                    reader.moveToEndOfMethod();
                    break;
                case COMMENT:
                    continue;
                default: //A line which is not compliant with code regulations (int[] a = 5;)
                    throw new UnkownCodeLineException();
            }
        }
    }

    //TODO: Implement
    private static void validateMethodDeclaration(String line) throws VariableTypeException, MethodBadArgsException,
            ExistingMethodNameException, ExistingVariableName, InvalidNameException {
        String[] methodDeclaration = line.split(" ", 2); //line.split(" "); //split type and method name+params
        //int foo(int b, int c);
        String methodName = methodDeclaration[1].substring(0, methodDeclaration[1].indexOf("(")).trim();
        if(!Utils.checkValidVariableName(methodName))
            throw new InvalidNameException();
        String methodArgs = methodDeclaration[1].substring(methodDeclaration[1].indexOf("(")+1,
                methodDeclaration[1].lastIndexOf(")"));
        String type = methodDeclaration[0];
        Method method;
        int indexOfBrackets = type.indexOf("[");

        if(indexOfBrackets == -1){ //The return type is not an array
            method = new Method(methodDeclaration[0], methodName, methodArgs);
        }
        else{
            type = type.substring(0, indexOfBrackets);
            method = new Method(type, methodName, methodArgs, true);
        }
        if(m_MethodMap.containsKey(methodName))
            throw new ExistingMethodNameException();
        m_MethodMap.put(methodName, method);

    }

    /**
     * Validates a member declaration
     * @param line The line of code which is a declaration of a variable
     * @throws InvalidMemberDeclaration
     * @throws VariableTypeException
     * @throws ExistingVariableName
     * @throws TypeMismatchException
     * @throws UnknownMethodCallException
     */
    private static void validateMemberDeclaration(String line, LinkedHashMap<String, Expression> variableMap, boolean isGlobal)
            throws InvalidMemberDeclaration, VariableTypeException,
            ExistingVariableName, TypeMismatchException, UnknownMethodCallException, VariableUninitializedException,
            InvalidArrayMembersDeclaration, OperationMismatchException, OperationTypeException, InvalidNameException,
            UnkownCodeLineException {


        //TODO: Check redundancy
        if(line.charAt(line.length()-1) != ';') //if the line doesn't end with a semicolon
            throw new InvalidMemberDeclaration();

        String name, value2;
        Matcher matcher = ExpressionTypeEnum.MEMBER_DECLARATION_PATTERN.matcher(line);
        if(matcher.lookingAt()){ //this is a member declaration
            name = matcher.group(2);
            value2 = matcher.group(3);
        }
        else{
            matcher = ExpressionTypeEnum.ARRAY_DECLARATION_PATTERN.matcher(line);
            if(matcher.lookingAt()){
                name = matcher.group(2);
                value2 = matcher.group(3);
            }
            else{
                throw new UnkownCodeLineException();
            }
        }

        String[] splitDeclaration = line.split(" "); //split type and expression

        //TODO: Check name against saved expressions

        int index = line.indexOf('=');
        if(value2 == null) value2 = "";

        if(!value2.equals("") && !value2.contains("=")) throw new InvalidNameException();
        if(index == -1){ //No initialization
            if(isGlobal){ //we are defining a global variable
                if(variableMap.containsKey(name))
                    throw new ExistingVariableName();
            }
            else{ //we are defining a local variable
                if(variableMap.containsKey(name) && !variableMap.get(name).isGlobal())
                    throw new ExistingVariableName();

            }
           if(splitDeclaration[0].matches(ExpressionTypeEnum.ARRAY_TYPE_REGEX)){ //This is an array
               String type = splitDeclaration[0].substring(0, splitDeclaration[0].indexOf("["));
               Variable vr = new Variable(type, name, false, true);
               vr.setGlobal(isGlobal);
               variableMap.put(name, vr);
           }
           else{
                Variable vr = new Variable(splitDeclaration[0], name, false);
                vr.setGlobal(isGlobal);
                variableMap.put(name, vr);
           }

        }
        else{ //Initialization of the member
            if(splitDeclaration[0].contains("[")){ //this is an array
                String type = splitDeclaration[0].substring(0, splitDeclaration[0].indexOf("["));
                boolean initialized = false;
                if(variableMap.containsKey(name))
                    throw new ExistingVariableName();

                //check types of values
                int indexOfCurly = line.indexOf("{");
                if(indexOfCurly == -1) initialized = false;
                int lastIndexOfCurly = line.lastIndexOf("}");
                String arrayValues = line.substring(indexOfCurly + 1, lastIndexOfCurly);
                String[] splitArrayValues = arrayValues.split(",");
                if((lastIndexOfCurly - indexOfCurly) > 1){
                    validateArrayInitialization(splitArrayValues, VariableEnum.toEnum(type), variableMap);
                }
                Utils.ValidArrayDeclaration(arrayValues);
                initialized = true;
                Variable vr = new Variable(type, name, initialized, true);
                variableMap.put(name, vr);
            }
            else{ //This isn't an array
                String value = line.substring(index+2, line.length()-1).replaceAll(" ", ""); //TODO: Check indexes
                String type = splitDeclaration[0];
                VariableEnum valueType =  validateValueExpression(value,
                        variableMap);

                if(!VariableEnum.checkValidAssignment(VariableEnum.toEnum(type), valueType))
                    throw new TypeMismatchException();
                if(isGlobal){ //we are defining a global variable
                    if(variableMap.containsKey(name))
                        throw new ExistingVariableName();
                }
                else{ //we are defining a local variable
                    if(variableMap.containsKey(name) && !variableMap.get(name).isGlobal())
                        throw new ExistingVariableName();

                }
                Variable vr = new Variable(type, name, true);
                vr.setGlobal(isGlobal);
                variableMap.put(name,vr );
            }
        }


    }


    private static void validateMethodBlock(FileReader reader, String methodName, Method method) throws
            UnkownCodeLineException, TypeMismatchException, InvalidMemberDeclaration, ExistingVariableName,
            UnknownMethodCallException, VariableTypeException, VariableUninitializedException, UnknownVariableException,
            OperationTypeException, OperationMismatchException, MethodBadArgsCountException, MethodTypeMismatchException,
            ConditionUnknownExpressionException, ConditionExpressionNotBooleanException, ConditionArrayCallMismatch,
            InvalidArrayIndexException, InvalidArrayMembersDeclaration, InvalidNameException {
        FileReader methodCode = reader.getMethodBlock();
        String currLine;
        while(methodCode.hasNext()){
            currLine = methodCode.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case BLOCK_START: //If-while block
                    validateBoolCondition(currLine, method);
                    //Recursively check the inner if-while block
                    validateMethodBlock(methodCode, methodName, new Method(method));
                    break;
                case METHOD_CALL: //a single method call (no assignment)
                    validateMethodCall(currLine, method);
                    break;
                case RETURN:
                    validateReturnStatement(currLine, method);
                    break;
                case MEM_DECLARATION: //check member declaration with and without initialization
                    validateMemberDeclaration(currLine, method.getAllExpressions(), false);
                    break;
                case ASSIGNMENT:
                    validateAssignment(currLine, method);
                    break;
                case UNKNOWN:
                    throw new UnkownCodeLineException();

            }
        }
    }

    private static void validateReturnStatement(String line, Method method) throws TypeMismatchException,
            OperationTypeException, VariableUninitializedException, OperationMismatchException, VariableTypeException {
        String[] splitReturn = line.split(" ", 2);
        if(splitReturn.length != 2){ //we have a return, with no value returned
            if(method.getType() != VariableEnum.VOID)
                throw new TypeMismatchException();
        }
        else if(method.getType() == VariableEnum.VOID) //Return with value but method is void
            throw new TypeMismatchException();
        else{
            String value = line.substring(line.indexOf("n")+1, line.length()-1).trim();
            VariableEnum valueType =  validateValueExpression(value, method.getAllExpressions());
            if(valueType == VariableEnum.ARRAY_TYPE){ //validate array values
                int indexOfBrackets = value.indexOf("{");
                String arguments = value.substring(indexOfBrackets+1, value.lastIndexOf("}"));
                if(arguments.equals("")){ //empty array is valid
                    return;
                }
                String[] splitArguments = arguments.split(",");
                for(String arg:splitArguments){
                    arg = arg.trim();
                    VariableEnum argType = validateValueExpression(arg, method.getAllExpressions());
                    if(!VariableEnum.checkValidAssignment(method.getType(), argType))
                        throw new TypeMismatchException();
                }
                /*
                if(!Utils.validateArrayTypes(arguments, method.getType()))
                    throw new TypeMismatchException();
                */
            }
            else{
                if(!VariableEnum.checkValidAssignment(method.getType(), valueType))
                    throw new TypeMismatchException();
            }
        }


    }

    private static void validateBoolCondition(String line, Method method) throws ConditionExpressionNotBooleanException,
            ConditionUnknownExpressionException, VariableUninitializedException, ConditionArrayCallMismatch {

        String condition = line.substring(line.indexOf("(")+1, line.lastIndexOf(")")); //TODO: Check indexes
        LinkedHashMap allExpressions = Utils.mergeExpressions(m_MemberMap, m_MethodMap);
        allExpressions = Utils.mergeExpressions(allExpressions, method.getAllExpressions());
        Condition.isValid(condition, allExpressions);
    }

    private static void validateMethodCall(String line, Method method) throws MethodBadArgsCountException,
            MethodTypeMismatchException, UnknownMethodCallException {
        String methodName = line.substring(0, line.indexOf("("));
        if(!method.getAllExpressions().containsKey(methodName)){
            throw new UnknownMethodCallException();
        }
        String params = line.substring(line.indexOf("(")+1, line.length()-2); //TODO: Check indexes
        VariableEnum[] paramTypes = getParameterTypes(params, method.getAllExpressions());
        ((Method)method.getAllExpressions().get(methodName)).ValidateHeader(paramTypes);
    }

    //TODO: Check if needs to deal with multiple math operations

    /**
     * Several options - normal assignment of a member
     * mathematical operation between members/methods/values
     * @param line
     * @param method
     * @throws UnknownVariableException
     * @throws VariableTypeException
     * @throws TypeMismatchException
     * @throws VariableUninitializedException
     * @throws OperationTypeException
     * @throws OperationMismatchException
     */
    private static void validateAssignment(String line, Method method) throws UnknownVariableException,
            VariableTypeException, TypeMismatchException, VariableUninitializedException, OperationTypeException,
            OperationMismatchException, InvalidArrayIndexException {
        String[] splitLine = line.split("="); //get variable, and operation string
        String name = splitLine[0].trim(); //get the name of the variable initialized
        String value = splitLine[1].substring(0, splitLine[1].length()-1).replaceAll(" ", "");
        //Check if it is an array
        int indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){ //An array
            String index = name.substring(indexOfBracket+1, name.length()-1);
            name = name.substring(0, indexOfBracket);
            VariableEnum indexType = validateValueExpression(index, method.getAllExpressions());
            if(indexType != VariableEnum.INT) //Index is not a type value
                throw new TypeMismatchException();
            else{ //check if it is a single non zero value
                if(!index.matches(CONFIG.finalReg)){ //check only if value is a single digit
                    if(Integer.parseInt(index) < 0)
                        throw new InvalidArrayIndexException();
                }
            }

        }
        else{
            if(!m_MemberMap.containsKey(name) && !method.getAllExpressions().containsKey(name))  //if the member is not defined
                throw new UnknownVariableException();
        }

        VariableEnum valueType =  validateValueExpression(value, method.getAllExpressions());

        VariableEnum.checkValidAssignment(getExpression(name, method.getAllExpressions()).getType(), valueType);
    }

    /**
     * This function validates any expression that is assigned or returned
     * @param valueExpression - without ';'
     * @return
     */
    private static VariableEnum validateValueExpression(String valueExpression, LinkedHashMap<String,
            Expression> methodMembers)
            throws OperationMismatchException,
            OperationTypeException, VariableUninitializedException, VariableTypeException {
        Matcher varOperation = CONFIG.VAR_MATH_OP.matcher(valueExpression);
        if(varOperation.lookingAt()){ //This means we have a math operation
            //group32 is the operation char
            String opType = varOperation.group(19); //TODO: Check
            //group14 is the left operator
            String op1 = varOperation.group(1);
            //group33 is the right operator
            String op2 = varOperation.group(20); 

            //Check if it is a method call
            int indexOfBrackets = op1.indexOf("(");
            if(indexOfBrackets != -1){
                op1 = op1.substring(0, indexOfBrackets);
            }
            indexOfBrackets = op2.indexOf("(");
            if(indexOfBrackets != -1){
                op2 = op2.substring(0, indexOfBrackets);
            }
            Expression op1Expression = getExpression(op1, methodMembers);
            Expression op2Expression = getExpression(op2, methodMembers);
            VariableEnum operationResultType = VariableEnum.VOID;

            if(op1Expression != null && op2Expression != null){  //both expressions
                operationResultType =  Operation.Operate(op1Expression, opType, op2Expression);
            }
            if(op1Expression == null && op2Expression == null){ //both actual values
                operationResultType =  Operation.Operate(Utils.getValueEnum(op1), opType, Utils.getValueEnum(op2));
            }
            if(op1Expression == null && op2Expression != null){
                operationResultType = Operation.Operate(Utils.getValueEnum(op1), opType, op2Expression);
            }
            if(op1Expression != null && op2Expression == null){
                operationResultType = Operation.Operate(Utils.getValueEnum(op2), opType, op1Expression);
            }
            return operationResultType;
        }
        else{ //normal assignment - either function call, member call, value or array
            //check if it is an array
            int indexOfBrackets = valueExpression.indexOf("{");
            if(indexOfBrackets != -1){ //Return array type and deal with it "higher up"
                return VariableEnum.ARRAY_TYPE;
            }
            else{
                Expression ex = getExpression(valueExpression, methodMembers);
                if(ex == null){
                    return Utils.getValueEnum(valueExpression.trim());
                }
                else{
                    if(!ex.isInitialized())
                        return VariableEnum.VOID;
                    return ex.getType();
                }
            }
        }
    }

    private static Expression getExpression(String name, LinkedHashMap<String, Expression> methodMembers){
        name = name.replace("-", "");
        int indexOfBracket = name.indexOf("(");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        Expression ex = getExpression(name);
        if(ex == null && !methodMembers.isEmpty())
            ex = methodMembers.get(name);
        return ex;
    }

    private static Expression getExpression(String name){
        name = name.replace("-", "");
        int indexOfBracket = name.indexOf("(");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){
            name = name.substring(0, indexOfBracket);
        }
        Expression ex = m_MemberMap.get(name);
        if(ex == null)
            ex = m_MethodMap.get(name);
        return ex;
    }

    private static VariableEnum getValueOfExpression(String expression){
        VariableEnum type;
        Expression ex = getExpression(expression);
        if(ex == null)
            type = Utils.getValueEnum(expression);
        else
            type = ex.getType();
        return type;
    }

    private static VariableEnum getValueOfExpression(String expression, LinkedHashMap<String, Expression> methodMembers){
        VariableEnum type;
        Expression ex = getExpression(expression, methodMembers);
        if(ex == null)
            type = Utils.getValueEnum(expression);
        else
            type = ex.getType();
        return type;
    }




    private static VariableEnum[] getParameterTypes(String line, LinkedHashMap<String, Expression> methodMembers){
        String[] params = line.split(",");
        VariableEnum[] types = new VariableEnum[params.length];
        int index = 0;
        for(String param: params){
            Expression ex = getExpression(param, methodMembers);
            if(ex!=null)
                types[index] = ex.getType();
            else{
                types[index] = Utils.getValueEnum(param);
            }
            index++;
        }
        return types;
    }


    //TODO: Expand to use expressionMap
    private static void validateArrayInitialization(String[] params, VariableEnum arrayType,
                                                    LinkedHashMap<String, Expression> expressions)
            throws TypeMismatchException, OperationTypeException, VariableUninitializedException,
            OperationMismatchException, VariableTypeException {
        VariableEnum paramType;
        for(String value: params){
            VariableEnum argumentType = validateValueExpression(value, expressions );
            if(!VariableEnum.checkValidAssignment(arrayType, argumentType))
                throw new TypeMismatchException();

        }
    }

}
