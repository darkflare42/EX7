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
 */
public class SyntaxCompiler {

    private static LinkedHashMap<String, Expression> m_MemberMap; //Holds all the global members
    private static LinkedHashMap<String, Expression> m_MethodMap; //Holds all the methods

    /**
     * This method passes over the file and checks for any compilation errors
     * @param reader An SjavaReader which has been instantiated for a specific s-java file
     * @throws CompilationException If there was a compilation error
     * @throws IOException If there was some problem reading the file
     */
    public static void compile(SJavaReader reader) throws CompilationException, IOException{
        m_MemberMap = new LinkedHashMap<String, Expression>();
        m_MethodMap = new LinkedHashMap<String, Expression>();

        //First stage - read all global declarations - members and methods
        compileMethodDeclaration(reader);  //Methods first so that we can check member assignment during this phase
        reader.reset();  //Move "pointer" of file to the start
        compileGlobalMemberDeclaration(reader); //Call for member declaration compilation


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
     * @throws VariableTypeException
     * @throws MethodBadArgsException
     * @throws ExistingMethodNameException
     * @throws UnknownCodeLineException
     * @throws ExistingVariableName
     * @throws InvalidNameException
     * @throws InvalidMemberDeclaration
     */
    private static void compileMethodDeclaration(SJavaReader reader) throws ExistingMethodNameException,
            ExistingVariableName, VariableTypeException, UnknownCodeLineException, InvalidNameException,
            MethodBadArgsException, InvalidMemberDeclaration, AssignMismatchException, OperationMismatchException,
            VariableUninitializedException, UnknownMethodCallException, InvalidArrayMembersDeclaration, OperationTypeException {

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
     * @throws OperationTypeException
     * @throws AssignMismatchException
     * @throws VariableTypeException
     * @throws OperationMismatchException
     * @throws InvalidNameException
     * @throws InvalidMemberDeclaration
     * @throws InvalidArrayMembersDeclaration
     * @throws VariableUninitializedException
     * @throws UnknownCodeLineException
     * @throws ExistingVariableName
     * @throws UnknownMethodCallException
     */
    private static void compileGlobalMemberDeclaration(SJavaReader reader) throws OperationTypeException,
            AssignMismatchException, VariableTypeException, OperationMismatchException, InvalidNameException,
            InvalidMemberDeclaration, InvalidArrayMembersDeclaration, VariableUninitializedException,
            UnknownCodeLineException, ExistingVariableName, UnknownMethodCallException {
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
     * @throws AssignMismatchException
     * @throws OperationMismatchException
     * @throws VariableUninitializedException
     * @throws UnknownMethodCallException
     * @throws InvalidArrayMembersDeclaration
     * @throws OperationTypeException
     */
    private static void validateMethodDeclaration(String line) throws VariableTypeException, MethodBadArgsException,
            ExistingMethodNameException, ExistingVariableName, InvalidNameException, UnknownCodeLineException,
            InvalidMemberDeclaration, AssignMismatchException, OperationMismatchException, VariableUninitializedException,
            UnknownMethodCallException, InvalidArrayMembersDeclaration, OperationTypeException {

        String[] methodDeclaration = line.split(" ", 2); //split type and method name+params
        String methodName = Utils.stripName(methodDeclaration[1]).trim();

        //Run validity check on the name first!
        if(!Utils.checkValidVariableName(methodName))
            throw new InvalidNameException();


        String methodArgs = Utils.getArgsInBrackets(methodDeclaration[1]);
        String methodReturnType = Utils.stripName(methodDeclaration[0]);
        boolean isReturnArray = (methodDeclaration[0].contains(""+RegexConfig.SQUARE_BRACKETS_START));
        String[] arguments = methodArgs.split(""+RegexConfig.COMMA_SEPERATOR_CHAR);
        LinkedHashMap<String, Expression> params = new LinkedHashMap<>();

        //Run a validation check on each of the parameters
        if(!methodArgs.equals("")){ //If there are no parameters defined, no need to check
            for(String arg: arguments){
                arg = arg.trim();
                Variable vr = validateMemberDeclaration(arg+";", params, false);
                vr.setIsInitialized(true);
            }
        }

        if(m_MethodMap.containsKey(methodName)) //If there is another global member with this name - exception
            throw new ExistingMethodNameException();
        m_MethodMap.put(methodName, new Method(methodReturnType, methodName, params, isReturnArray));

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
    private static Variable validateMemberDeclaration(String line, LinkedHashMap<String, Expression> variableMap,
                                                  boolean isGlobal)
            throws InvalidMemberDeclaration, VariableTypeException,
            ExistingVariableName, AssignMismatchException, UnknownMethodCallException, VariableUninitializedException,
            InvalidArrayMembersDeclaration, OperationMismatchException, OperationTypeException, InvalidNameException,
            UnknownCodeLineException  {

        //Local variable declarations
        Matcher matcher = Utils.validateVariableDeclaration(line);
        String name = matcher.group(ExpressionTypeEnum.NAME_GROUP);
        String[] splitDeclaration = line.split(" "); //split type and expression
        String type = Utils.stripName(splitDeclaration[0]);
        boolean isArray = splitDeclaration[0].contains(""+RegexConfig.SQUARE_BRACKETS_START);
        boolean isInitialized = line.contains(""+RegexConfig.EQUALS_CHAR);

        if(!Utils.checkValidVariableName(name))
            throw new InvalidNameException();

        if(!checkMemberExists(name, variableMap, isGlobal))
            throw new ExistingVariableName();

        if(isInitialized){ //If the member is initialized we need to check the assignment value
            if(isArray){ //this is an array

                //check types of values
                String arrayValues = Utils.getArgsInBrackets(line).trim();
                String[] splitArrayValues = arrayValues.split(RegexConfig.COMMA_SEPERATOR_CHAR);
                if(!arrayValues.equals("")){ //non empty init of array
                    validateArrayInitialization(splitArrayValues,  new Variable(type, name, true), variableMap);
                }
                Utils.validArrayDeclaration(arrayValues); //Validate the array declaration
            }
            else{ //This isn't an array
                String value = matcher.group(ExpressionTypeEnum.VALUE_GROUP).replaceAll(""+RegexConfig.EQUALS_CHAR,
                        "").trim(); //Get the value that is assigned
                validateValueExpression(value, variableMap, new Variable(type, name, true));
            }
        }
        //Add the new variable to the variableMap
        Variable vr = new Variable(type, name, isInitialized, isArray);
        vr.setGlobal(isGlobal);
        variableMap.put(name,vr);
        return vr;
    }


    /**
     * This function validates a method block - it goes over every line in the method block and calls the appropriate
     * sub method which validates said line
     * @param reader The SJavaReader that reads through the current file
     * @param method The current method that is being validated
     * @throws UnknownCodeLineException
     * @throws AssignMismatchException
     * @throws InvalidMemberDeclaration
     * @throws ExistingVariableName
     * @throws UnknownMethodCallException
     * @throws VariableTypeException
     * @throws VariableUninitializedException
     * @throws UnknownVariableException
     * @throws OperationTypeException
     * @throws OperationMismatchException
     * @throws MethodBadArgsCountException
     * @throws MethodTypeMismatchException
     * @throws ConditionUnknownExpressionException
     * @throws ConditionExpressionNotBooleanException
     * @throws ConditionArrayCallMismatch
     * @throws InvalidArrayIndexException
     * @throws InvalidArrayMembersDeclaration
     * @throws InvalidNameException
     */
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

    /**
     * This function validates a return statement
     * @param line The line with the return statement
     * @param method The current method we are validating
     * @throws AssignMismatchException
     * @throws OperationTypeException
     * @throws VariableUninitializedException
     * @throws OperationMismatchException
     * @throws VariableTypeException
     * @throws InvalidArrayMembersDeclaration
     */
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

            String value = line.substring(line.indexOf("n")+1, line.length()-1).trim(); //Get the value that is returned

            //Check the returned value against the return type of the function
            VariableEnum valueType =  validateValueExpression(value, method.getAllExpressions(), method);

            //If we return an array - we check the values, otherwise the check is done within validateValueExpression
            if(valueType == VariableEnum.ARRAY_TYPE){ //validate array values
                String arguments = Utils.getArgsInBrackets(value);
                if(arguments.equals("")){ //We have an empty array initialization - valid
                    return;
                }
                String[] splitArguments = arguments.split(RegexConfig.COMMA_SEPERATOR_CHAR);
                validateArrayInitialization(splitArguments, method, method.getAllExpressions());
            }
        }


    }

    /**
     * This method validates a boolean condition (an if or while block)
     * @param line The line that holds the boolean condition
     * @param method The current method that is being validated
     * @throws ConditionExpressionNotBooleanException
     * @throws ConditionUnknownExpressionException
     * @throws VariableUninitializedException
     * @throws ConditionArrayCallMismatch
     */
    private static void validateBoolCondition(String line, Method method) throws ConditionExpressionNotBooleanException,
            ConditionUnknownExpressionException, VariableUninitializedException, ConditionArrayCallMismatch {

        String condition = Utils.getArgsInBrackets(line);
        Condition.isValid(condition, method.getAllExpressions());
    }

    /**
     * This function validates a method call
     * @param line The line that holds the method call
     * @param method The current method that is being validated
     * @throws MethodBadArgsCountException
     * @throws MethodTypeMismatchException
     * @throws UnknownMethodCallException
     */
    private static void validateMethodCall(String line, Method method) throws MethodBadArgsCountException,
            MethodTypeMismatchException, UnknownMethodCallException {
        String methodName = Utils.stripName(line);
        if(!method.getAllExpressions().containsKey(methodName)){ //Check if we call a method that doesn't exist
            throw new UnknownMethodCallException();
        }
        String params = Utils.getArgsInBrackets(line);
        VariableEnum[] paramTypes = getParameterTypes(params, method.getAllExpressions());
        ((Method)method.getAllExpressions().get(methodName)).ValidateHeader(paramTypes); //Validate the method call
    }


    /**
     * This function validates an assignment into some variable
     * @param line The line that holds the assignment
     * @param method The current method that is being validated
     * @throws UnknownVariableException
     * @throws VariableTypeException
     * @throws AssignMismatchException
     * @throws VariableUninitializedException
     * @throws OperationTypeException
     * @throws OperationMismatchException
     * @throws InvalidArrayIndexException
     */
    private static void validateAssignment(String line, Method method) throws UnknownVariableException,
            VariableTypeException, AssignMismatchException, VariableUninitializedException, OperationTypeException,
            OperationMismatchException, InvalidArrayIndexException  {

        String[] splitLine = line.split(""+RegexConfig.EQUALS_CHAR); //get variable, and operation string
        String name = splitLine[0].trim(); //get the name of the variable initialized
        String value = splitLine[1].substring(0, splitLine[1].length()-1).replaceAll(" ", "");

        //Check if it is an array
        if(name.contains(""+RegexConfig.SQUARE_BRACKETS_START)){ //This is an array

            String index = Utils.getArgsInBrackets(name); //Gets the index value
            name = Utils.stripName(name); //Gets the name of the array

            //First, validate the index value (a[index])
            //Create a faux INT variable that we assign it to
            validateValueExpression(index, method.getAllExpressions(),
                    new Variable(VariableEnum.INT, "arrIndex", true));

            if(!Utils.checkValidIndexValue(index)){
                    throw new InvalidArrayIndexException();
            }

            Variable array = (Variable)getExpression(name, method.getAllExpressions());
            Variable arrayElementVar = new Variable(array.getType(), "", array.isInitialized());

            //Validate the value inserted into the element of the array
            validateValueExpression(value, method.getAllExpressions(), arrayElementVar);
            return; //All is well
        }
        else{
            //Check if the member does not exist
            if(!m_MemberMap.containsKey(name) && !method.getAllExpressions().containsKey(name))
                throw new UnknownVariableException();
        }
        validateValueExpression(value, method.getAllExpressions(),
                getExpression(name, method.getAllExpressions()));
    }

    /**
     * This function validates any expression that is assigned or returned
     * @param valueExpression - without ';'
     * @param methodMembers The members that the method "sees"
     * @param insertInto The expression that we are assigning into (a variable, or a method)
     * @return The Enum that represents the type that the valueExpression is equal to
     * @throws OperationMismatchException
     * @throws OperationTypeException
     * @throws VariableUninitializedException
     * @throws VariableTypeException
     * @throws AssignMismatchException
     */
    private static VariableEnum validateValueExpression(String valueExpression, LinkedHashMap<String,
            Expression> methodMembers, Expression insertInto)
            throws OperationMismatchException,
            OperationTypeException, VariableUninitializedException, VariableTypeException
            , AssignMismatchException {

        Matcher varOperation = RegexConfig.VAR_MATH_OP.matcher(valueExpression);
        if(varOperation.lookingAt()){ //This means we have a math operation
            //group12 is the operation char
            String opType = varOperation.group(RegexConfig.OPERATOR_GROUP);
            //group1 is the left operator
            String op1 = varOperation.group(RegexConfig.FIRST_ELEMENT);
            //group13 is the right operator
            String op2 = varOperation.group(RegexConfig.SECOND_ELEMENT);

            op1 = Utils.stripName(op1);
            op2 = Utils.stripName(op2);

            VariableEnum operationResultType =  getOperationType(op1, op2, opType, methodMembers);
            insertInto.Assign(operationResultType);
            return operationResultType;
        }
        else{ //normal assignment - either function call, member call, value or array
            //check if it is an array

            if(valueExpression.contains(""+RegexConfig.BLOCK_START_CHAR)){ //Return array type and send it "higher up"
                if(!insertInto.isArray()) // we are declaring an array inside a non array variable
                    throw new AssignMismatchException();
                return VariableEnum.ARRAY_TYPE;
            }
            else{ //We are assigning a member, function or a direct value

                Expression ex = getExpression(valueExpression, methodMembers);
                if(ex == null){ //This means we assign a direct value
                    insertInto.Assign(Utils.getValueEnum(valueExpression.trim()));
                    return Utils.getValueEnum(valueExpression.trim());
                }
                if(valueExpression.contains(""+RegexConfig.SQUARE_BRACKETS_START)){ //sending an element of an array
                    ex = new Variable(ex.getType(), "", true); //create a variable that represents the array element
                }
                insertInto.Assign(ex);
                return ex.getType();

            }
        }
    }


    /**
     * This function retrieves the Expression (Variable or Method) from within the members and methods that the method
     * "sees"
     * @param name The name of the expression we are looking for
     * @param methodMembers The expression map that the method "sees"
     * @return The expression
     */
    private static Expression getExpression(String name, LinkedHashMap<String, Expression> methodMembers){
        name = Utils.stripName(name);
        Expression ex = getGlobalExpression(name); //First check for global members
        if(ex == null && !methodMembers.isEmpty()) //If it isn't a global expression look inside the method
            ex = methodMembers.get(name);
        return ex;

    }

    /**
     * This method retrieves the Expression from within the global members or methods
     * @param name The name of the expression we are looking for
     * @return The expression
     */
    private static Expression getGlobalExpression(String name){
        Expression ex = m_MemberMap.get(name);
        if(ex == null)
            ex = m_MethodMap.get(name);
        return ex;
    }

    /**
     * This function receives a line that holds the parameters and returns an array of VariableEnum which represent
     * the types of the parameters
     * @param parameterLine The line that holds the parameters
     * @param methodMembers The members that the method "sees"
     * @return An array of VariableEnum types that represent the types of all the parameters
     */
    private static VariableEnum[] getParameterTypes(String parameterLine,
                                                    LinkedHashMap<String, Expression> methodMembers){
        String[] params = parameterLine.split(RegexConfig.COMMA_SEPERATOR_CHAR);
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

    /**
     * This function validates an array initialization, it receives paramters, the type of the array and known
     * expressions and makes sure that each parameter can be inserted into the type of the array
     * @param params The parameters that we want to insert into the array
     * @param arrayType The type of the array
     * @param expressions The known expressions that the array "sees"
     * @throws AssignMismatchException
     * @throws OperationTypeException
     * @throws VariableUninitializedException
     * @throws OperationMismatchException
     * @throws VariableTypeException
     * @throws InvalidArrayMembersDeclaration
     */
    private static void validateArrayInitialization(String[] params, Expression arrayType,
                                                    LinkedHashMap<String, Expression> expressions)
            throws AssignMismatchException, OperationTypeException, VariableUninitializedException,
            OperationMismatchException, VariableTypeException , InvalidArrayMembersDeclaration {
        Variable arrayMember = new Variable(arrayType.getType(),"", true); //Create a faux variable member which
                                                                           // is of the type of the array
        for(String value: params){
            value = value.trim();
            validateValueExpression(value, expressions, arrayMember); //validate the value of the param against the type
                                                                      //of the array
        }
    }

    /**
     * This method checks if a member exists within the variableMap, this allows us to check for (and allow) duplicates
     * @param memberName The name of the member we are looking for
     * @param variableMap The known expressions that the method sees
     * @param isGlobal Global member or a local member
     * @return true if it exists and false if it doesn't
     */
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

    /**
     * This function receives two elements and a mathematical operation (-+*\) and returns the VariableEnum type of the
     * result of the operation
     * @param firstElement The first element in the operation
     * @param secondElement The second element in the operation
     * @param operator The actual operator
     * @param knownExpressions The known expressions
     * @return The Type of the result of the operation
     * @throws OperationMismatchException
     * @throws OperationTypeException
     * @throws VariableUninitializedException
     */
    private static VariableEnum getOperationType(String firstElement, String secondElement, String operator,
                                                 LinkedHashMap<String, Expression> knownExpressions) throws
            OperationMismatchException, OperationTypeException, VariableUninitializedException {
        Expression firstExpression = getExpression(firstElement, knownExpressions);
        Expression secondExpression = getExpression(secondElement, knownExpressions);
        VariableEnum operationResultType = VariableEnum.VOID;

        if(firstExpression != null && secondExpression != null){  //both expressions
            operationResultType =  Operation.Operate(firstExpression, operator, secondExpression);
        }
        if(firstExpression == null && secondExpression == null){ //both actual values
            operationResultType =  Operation.Operate(Utils.getValueEnum(firstElement), operator,
                    Utils.getValueEnum(secondElement));
        }
        if(firstExpression == null && secondExpression != null){ //one actual value the other an expression
            operationResultType = Operation.Operate(Utils.getValueEnum(firstElement), operator, secondExpression);
        }
        if(firstExpression != null && secondExpression == null){ //one actual value, the other an expression
            operationResultType = Operation.Operate(Utils.getValueEnum(secondElement), operator, firstExpression);
        }
        return  operationResultType;

    }


}
