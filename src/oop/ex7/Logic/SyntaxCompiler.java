package oop.ex7.Logic;

import oop.ex7.Expressions.*;
import oop.ex7.Expressions.Exceptions.*;
import oop.ex7.Logic.Exceptions.*;
import oop.ex7.Reader.SJavaReader;
import oop.ex7.Reader.IOException;

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
     * This method passes over the file and checks for any compilation errors
     * @param reader An SjavaReader which has been instantiated for a specific s-java file
     * @throws CompilationException
     * @throws IOException
     */
    public static void compile(SJavaReader reader) throws CompilationException, IOException{
        m_MemberMap = new LinkedHashMap<String, Expression>();
        m_MethodMap = new LinkedHashMap<String, Expression>();

        //First stage - read all global declarations - members and methods
        compileMethodDeclaration(reader);  //Methods first so that we can check member assignment during this phase
        reader.reset();  //Move "pointer" of file to the start
        compileMemberDeclaration(reader); //Call for member declaration compilation


        //If we have declared methods - run a deep check, otherwise we finished
        if(!m_MethodMap.isEmpty()){

            reader.reset();  //Reset the location of the reader
            reader.moveToFirstMethodDeclaration();

            //Create a hashmap of all the global values
            LinkedHashMap<String, Expression> globalValues = Utils.mergeExpressions(m_MemberMap, m_MethodMap);


            for(String methodName: m_MethodMap.keySet()){
                //First merge all method members with global members
                ((Method)m_MethodMap.get(methodName)).mergeAllExpressions(globalValues);

                //Validate the method block
                validateMethodBlock(reader, (Method) m_MethodMap.get(methodName));
                reader.moveToEndOfMethod();

                if(reader.hasNext())reader.next();
            }
        }

    }

    /**
     * This method runs over all the file and checks method declarations only for compilation errors
     * @param reader The SJavaReader that reads through the current file
     */
    private static void compileMethodDeclaration(SJavaReader reader) throws VariableTypeException, MethodBadArgsException,
            ExistingMethodNameException, UnknownCodeLineException, ExistingVariableName, InvalidNameException, InvalidMemberDeclaration {

        String currLine;
        while(reader.hasNext()){
            currLine = reader.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case METHOD_DECLARATION:
                    validateMethodDeclaration(currLine);
                    reader.moveToEndOfMethod(); //Jump to the end of the method block (we run a deep check later)
                    break;
                case UNKNOWN: //We reached an unknown code line
                    throw new UnknownCodeLineException();
            }
        }
    }

    /**
     * This method runs over all the file and checks member declaration only for compilation errors
     * @param reader The SJavaReader that reads through the current file
     * @throws CompilationException
     */
    private static void compileMemberDeclaration(SJavaReader reader) throws CompilationException{
        String currLine;
        while(reader.hasNext()){
            currLine = reader.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case MEM_DECLARATION:
                    validateMemberDeclaration(currLine, m_MemberMap, true);
                    break;
                case METHOD_DECLARATION: //If its a method declaration - jump to the end of the method
                    reader.moveToEndOfMethod();
                    break;
                case COMMENT: //If its a valid comment line - continue on in the file
                    continue;
                default: //A line which is not compliant with code regulations
                    throw new UnknownCodeLineException();
            }
        }
    }

    /**
     * This method validates a method declaration
     * @param line The actual declaration line from the sjava file
     * @throws VariableTypeException
     * @throws MethodBadArgsException
     * @throws ExistingMethodNameException
     * @throws ExistingVariableName
     * @throws InvalidNameException
     * @throws UnknownCodeLineException
     * @throws InvalidMemberDeclaration
     */
    private static void validateMethodDeclaration(String line) throws VariableTypeException, MethodBadArgsException,
            ExistingMethodNameException, ExistingVariableName, InvalidNameException, UnknownCodeLineException,
            InvalidMemberDeclaration {

        String[] methodDeclaration = line.split(" ", 2); //split type and method name+params
        String methodName = Utils.stripName(methodDeclaration[1]).trim();

        //Run validity check on the name first!
        if(!RegexConfig.checkValidVariableName(methodName))
            throw new InvalidNameException();

        String methodArgs = Utils.getArgsInBrackets(methodDeclaration[1]);
        String methodReturnType = Utils.stripName(methodDeclaration[0]);
        boolean isReturnArray = (methodDeclaration[0].contains("["));


        if(m_MethodMap.containsKey(methodName)) //If there is another global member with this name - exception
            throw new ExistingMethodNameException();
        m_MethodMap.put(methodName, new Method(methodReturnType, methodName, methodArgs, isReturnArray));

    }

    /**
     * This method validates a member declaration, whether it is done "globally" or it is done locally within a block
     * @param line The actual declaration line from the sjava file
     * @param variableMap The variable map that is currently available to the block
     * @param isGlobal If we are declaring a global variable or not
     * @throws InvalidMemberDeclaration
     * @throws VariableTypeException
     * @throws ExistingVariableName
     * @throws AssignMismatchException
     * @throws UnknownMethodCallException
     * @throws VariableUninitializedException
     * @throws InvalidArrayMembersDeclaration
     * @throws OperationMismatchException
     * @throws OperationTypeException
     * @throws InvalidNameException
     * @throws UnknownCodeLineException
     */
    private static void validateMemberDeclaration(String line, LinkedHashMap<String, Expression> variableMap,
                                                  boolean isGlobal)
            throws InvalidMemberDeclaration, VariableTypeException,
            ExistingVariableName, AssignMismatchException, UnknownMethodCallException, VariableUninitializedException,
            InvalidArrayMembersDeclaration, OperationMismatchException, OperationTypeException, InvalidNameException,
            UnknownCodeLineException  {

        //Local variable declarations
        Matcher matcher = Utils.validateVariableName(line);
        String name = matcher.group(2);
        String[] splitDeclaration = line.split(" "); //split type and expression
        String type = Utils.stripName(splitDeclaration[0]);
        boolean isArray = splitDeclaration[0].contains("[");
        boolean isInitialized = line.contains("=");

        if(!checkMemberExists(name, variableMap, isGlobal))
            throw new ExistingVariableName();

        if(isInitialized){ //If the member is initialized we need to check the assignment value
            if(isArray){ //this is an array

                //check types of values
                String arrayValues = Utils.getArgsInBrackets(line).trim();
                String[] splitArrayValues = arrayValues.split(",");
                if(!arrayValues.equals("")){ //non empty init of array
                    validateArrayInitialization(splitArrayValues,  new Variable(type, name, true), variableMap);
                }
                Utils.ValidArrayDeclaration(arrayValues); //Validate the array declaration
            }
            else{ //This isn't an array
                String value = matcher.group(3).replaceAll("=", "").trim(); //Get the value that is assigned
                validateValueExpression(value, variableMap, new Variable(type, name, true));
            }
        }
        //Add the new variable to the variableMap
        Variable vr = new Variable(type, name, isInitialized, isArray);
        vr.setGlobal(isGlobal);
        variableMap.put(name,vr);
    }

    
    private static void validateMethodBlock(SJavaReader reader, Method method) throws
            UnknownCodeLineException, AssignMismatchException, InvalidMemberDeclaration, ExistingVariableName,
            UnknownMethodCallException, VariableTypeException, VariableUninitializedException, UnknownVariableException,
            OperationTypeException, OperationMismatchException, MethodBadArgsCountException,
            MethodTypeMismatchException,ConditionUnknownExpressionException, ConditionExpressionNotBooleanException,
            ConditionArrayCallMismatch, InvalidArrayIndexException, InvalidArrayMembersDeclaration,
            InvalidNameException  {

        SJavaReader methodCode = reader.getMethodBlock();
        String currLine;
        while(methodCode.hasNext()){
            currLine = methodCode.next(); //Read the next line of the function
            switch (ExpressionTypeEnum.checkType(currLine)){ //Determine the line type
                case BLOCK_START: //If-while block
                    validateBoolCondition(currLine, method);
                    //Recursively check the inner if-while block
                    validateMethodBlock(methodCode, new Method(method));
                    break;
                case METHOD_CALL: //a single method call (no assignment)
                    validateMethodCall(currLine, method);
                    break;
                case RETURN: //The method's return statement
                    validateReturnStatement(currLine, method);
                    break;
                case MEM_DECLARATION: //check member declaration with and without initialization isGlobal - false
                                      //Because it is a local variable declaration
                    validateMemberDeclaration(currLine, method.getAllExpressions(), false);
                    break;
                case ASSIGNMENT: //Any assignment line (a = b+c)
                    validateAssignment(currLine, method);
                    break;
                case UNKNOWN: //Unknown code line
                    throw new UnknownCodeLineException();
            }
        }
    }

    private static void validateReturnStatement(String line, Method method) throws AssignMismatchException,
            OperationTypeException, VariableUninitializedException, OperationMismatchException, VariableTypeException,
            InvalidArrayMembersDeclaration {

        String[] splitReturn = line.split(" ", 2);
        if(splitReturn.length != 2){ //we have a return, with no value returned
            if(method.getType() != VariableEnum.VOID)
                throw new AssignMismatchException();
        }
        else if(method.getType() == VariableEnum.VOID) //Return with value but method is void
            throw new AssignMismatchException();

        else{ //Check the return type of the function and the type of the value that is returned

            //TODO: See if we can split this using regex
            String value = line.substring(line.indexOf("n")+1, line.length()-1).trim(); //Get the value that is returned

            //Check the returned value against the return type of the function
            VariableEnum valueType =  validateValueExpression(value, method.getAllExpressions(), method);

            //If we return an array - we check the values, otherwise the check is done within validateValueExpression
            if(valueType == VariableEnum.ARRAY_TYPE){ //validate array values
                String arguments = Utils.getArgsInBrackets(value);
                if(arguments.equals("")){ //We have an empty array initialization - valid
                    return;
                }
                String[] splitArguments = arguments.split(",");
                validateArrayInitialization(splitArguments, method, method.getAllExpressions());
            }
        }


    }

    private static void validateBoolCondition(String line, Method method) throws ConditionExpressionNotBooleanException,
            ConditionUnknownExpressionException, VariableUninitializedException, ConditionArrayCallMismatch {

        String condition = Utils.getArgsInBrackets(line);
        Condition.isValid(condition, method.getAllExpressions());
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


    /**
     * Several options - normal assignment of a member
     * mathematical operation between members/methods/values
     * @param line
     * @param method
     * @throws UnknownVariableException
     * @throws VariableTypeException
     * @throws AssignMismatchException
     * @throws VariableUninitializedException
     * @throws OperationTypeException
     * @throws OperationMismatchException
     */
    private static void validateAssignment(String line, Method method) throws UnknownVariableException,
            VariableTypeException, AssignMismatchException, VariableUninitializedException, OperationTypeException,
            OperationMismatchException, InvalidArrayIndexException  {
        String[] splitLine = line.split("="); //get variable, and operation string
        String name = splitLine[0].trim(); //get the name of the variable initialized
        String value = splitLine[1].substring(0, splitLine[1].length()-1).replaceAll(" ", "");
        //Check if it is an array
        int indexOfBracket = name.indexOf("[");
        if(indexOfBracket != -1){ //An array
            String index = name.substring(indexOfBracket+1, name.length()-1);
            name = name.substring(0, indexOfBracket);
            //TODO: ARRAY CHECK
            VariableEnum indexType = validateValueExpression(index, method.getAllExpressions(),
                    new Variable("int", "arrIndex", true));



            if(indexType != VariableEnum.INT) //Index is not a type value
                throw new AssignMismatchException();
            else{ //check if it is a single non zero value
                if(!index.matches(RegexConfig.OPERATION_REGEX)){ //check only if value is a single digit
                    if(Utils.IntegerTryParse(index) && Integer.parseInt(index) < 0) //check if it is a non zero number
                        throw new InvalidArrayIndexException();
                }
            }
            //TODO: Call validateValueExpression
            Variable array = (Variable)getExpression(name, method.getAllExpressions());
            Variable arrayElementVar = new Variable(array.getType().toString(),
                    "", array.isInitialized());
            validateValueExpression(value, method.getAllExpressions(), arrayElementVar);
            return;
        }
        else{
            if(!m_MemberMap.containsKey(name) && !method.getAllExpressions().containsKey(name))  //if the member is not defined
                throw new UnknownVariableException();
        }

        validateValueExpression(value, method.getAllExpressions(),
                getExpression(name, method.getAllExpressions()));
    }

    /**
     * This function validates any expression that is assigned or returned
     * @param valueExpression - without ';'
     * @return
     */
    private static VariableEnum validateValueExpression(String valueExpression, LinkedHashMap<String,
            Expression> methodMembers, Expression insertInto)
            throws OperationMismatchException,
            OperationTypeException, VariableUninitializedException, VariableTypeException
            , AssignMismatchException {
        Matcher varOperation = RegexConfig.VAR_MATH_OP.matcher(valueExpression);
        if(varOperation.lookingAt()){ //This means we have a math operation
            // TODO oded: i just realized how shitty this is because of the super complex pattern. maybe it can be simplified?
            //group12 is the operation char
            String opType = varOperation.group(12);
            //group1 is the left operator
            String op1 = varOperation.group(1);
            //group13 is the right operator
            String op2 = varOperation.group(13);

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
            insertInto.Assign(operationResultType);
            return operationResultType;
        }
        else{ //normal assignment - either function call, member call, value or array
            //check if it is an array
            int indexOfBrackets = valueExpression.indexOf("{");

            if(indexOfBrackets != -1){ //Return array type and deal with it "higher up"
                if(!insertInto.isArray()) // we are declaring an array inside a non array variable
                    throw new AssignMismatchException();
                return VariableEnum.ARRAY_TYPE;
            }
            else{
                Expression ex = getExpression(valueExpression, methodMembers);
                if(ex == null){
                    insertInto.Assign(Utils.getValueEnum(valueExpression.trim()));
                    return Utils.getValueEnum(valueExpression.trim());
                } //TODO: Some of these checks are done inside variable.Assign
                else if(!ex.isInitialized()){
                    insertInto.Assign(VariableEnum.VOID); //Uninitialized member
                        return VariableEnum.VOID;
                }
                else if(valueExpression.contains("[")){ //we are sending an element of an array
                    Variable vr = new Variable(ex.getType().toString(), "", true);
                    insertInto.Assign(vr.getType());
                    return vr.getType();
                }
                else{
                    insertInto.Assign(ex.getType());
                    return ex.getType();
                }

            }
        }
    }


    private static Expression getExpression(String name, LinkedHashMap<String, Expression> methodMembers){
        name = Utils.stripName(name);
        Expression ex = getGlobalExpression(name);
        if(ex == null && !methodMembers.isEmpty())
            ex = methodMembers.get(name);
        return ex;
    }

    private static Expression getGlobalExpression(String name){
        Expression ex = m_MemberMap.get(name);
        if(ex == null)
            ex = m_MethodMap.get(name);
        return ex;
    }

    private static VariableEnum[] getParameterTypes(String line, LinkedHashMap<String, Expression> methodMembers){
        String[] params = line.split(",");
        VariableEnum[] types = new VariableEnum[params.length];
        int index = 0;
        for(String param: params){
            param = param.trim();
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


    private static void validateArrayInitialization(String[] params, Expression arrayType,
                                                    LinkedHashMap<String, Expression> expressions)
            throws AssignMismatchException, OperationTypeException, VariableUninitializedException,
            OperationMismatchException, VariableTypeException , InvalidArrayMembersDeclaration {
        Variable arrayMember = new Variable(arrayType.getType(),"", true); //Create a faux variable member which
                                                                           // is of the type of the array
        for(String value: params){
            value = value.trim();
            validateValueExpression(value, expressions, arrayMember); //validate the value of the param agains the type
                                                                      //of the array
        }
    }

    private static boolean checkMemberExists(String memberName, LinkedHashMap<String, Expression> variableMap,
                                        boolean isGlobal){
        if(isGlobal){ //we are defining a global variable
            if(variableMap.containsKey(memberName))
                return false;
        }
        else{ //we are defining a local variable
            if(variableMap.containsKey(memberName) && !variableMap.get(memberName).isGlobal())
                return false;

        }
        return true;

    }




}
