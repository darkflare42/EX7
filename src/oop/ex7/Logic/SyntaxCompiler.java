package oop.ex7.Logic;

import oop.ex7.Expressions.ExpressionTypeEnum;
import oop.ex7.Logic.Exceptions.*;
import oop.ex7.Method;
import oop.ex7.Reader.FileReader;
import oop.ex7.Variable;
import oop.ex7.VariableEnum;
import oop.ex7.VariableTypeException;
import oop.ex7.Reader.IOException;

import java.util.LinkedHashMap;


/**
 * This class passes over the s-java class, line by line and checks for valid method and member declarations only
 * Created by Or Keren on 13/06/14.
 */
public class SyntaxCompiler {

    private static LinkedHashMap<String, Variable> m_MemberMap;
    private static LinkedHashMap<String, Method> m_MethodMap;

    /**
     * This method passes over the file and checks for compilation errors
     * @param reader
     * @throws CompilationException
     * @throws IOException
     */
    public static void compile(FileReader reader) throws CompilationException, IOException{
        m_MemberMap = new LinkedHashMap<String, Variable>();
        m_MethodMap = new LinkedHashMap<String, Method>();
        compileMethods(reader);  //Call for methods first so that we can check variable assignment during this phase
        reader.reset();  //Move "pointer" to start of file
        compileMembers(reader); //Call for member compilation

    }

    /**
     * This method runs over all the file and checks methods only for compilation errors
     * @param reader The FileReader that reads through the current file
     */
    private static void compileMethods(FileReader reader){
        String currLine;
        while(reader.hasNext()){
            currLine = reader.next();
            switch (ExpressionTypeEnum.checkType(currLine)){
                case METHOD_DECLARATION:
                    validateMethodDeclaration(currLine);
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
                    validateMemberDeclaration(currLine);
                    break;
            }
        }
    }

    //TODO: Implement
    private static void validateMethodDeclaration(String line){

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
    private static void validateMemberDeclaration(String line) throws InvalidMemberDeclaration, VariableTypeException,
    ExistingVariableName, TypeMismatchException, UnknownMethodCallException{
        if(line.contains(",")) //multiple declarations in one line
            throw new InvalidMemberDeclaration();

        //TODO: Check redundancy
        if(line.charAt(line.length()-1) != ';') //if the line doesn't end with a semicolon
            throw new InvalidMemberDeclaration();

        //TODO: Check redundancy
        String[] splitDeclaration = line.split(" ");
        if(!splitDeclaration[0].matches(VariableEnum.Types()))  //If first element in the string is not a TYPE
            throw new InvalidMemberDeclaration();
        //TODO: Check name against saved expressions

        int index = line.indexOf('=');
        if(index != -1){ //No initialization
            if(m_MemberMap.containsKey(splitDeclaration[1]))
                throw new ExistingVariableName();
             m_MemberMap.put(splitDeclaration[1], new Variable(splitDeclaration[0], splitDeclaration[1], false));
        }
        else{ //Initialization of the member
            String value = line.substring(index+1, line.length()-1); //TODO: Check indexes
            String type = splitDeclaration[0];
            if(value.matches(CONFIG.METHOD_CALL)){  //Check if it is initialized by a method call
                String methodName = value.substring(0, value.indexOf("(")); //get method name
                if(!m_MethodMap.containsKey(methodName))
                    throw new UnknownMethodCallException();
                if(VariableEnum.toEnum(type) != m_MethodMap.get(methodName).getType())
                    throw new TypeMismatchException();
            }
            else{ //A direct value is given
                parseVariableValue(value, type);
            }
            //Add the member to the member map
            if(m_MemberMap.containsKey(splitDeclaration[1]))
                throw new ExistingVariableName();
            m_MemberMap.put(splitDeclaration[1], new Variable(type, splitDeclaration[1], true));
        }


    }

    /**
     * This function parses the value of the variable to the type the variable is assigned as
     * @param value The value that is assigned to the variable
     * @param type The declaration type of the variable
     * @throws VariableTypeException
     * @throws TypeMismatchException
     */
    private static void parseVariableValue(String value, String type) throws VariableTypeException,
            TypeMismatchException{
        VariableEnum typeEnum = VariableEnum.toEnum(type);
        switch(typeEnum){  //Try to parse the value according to the type the variable is defined as
            case INT:
                try{
                    Integer.parseInt(value);
                }
                catch (NumberFormatException ex){
                    throw new TypeMismatchException();
                }
                break;
            case DOUBLE:
                try{
                    Double.parseDouble(value);
                }
                catch (NumberFormatException ex){
                    throw new TypeMismatchException();
                }
                break;
            case STRING:
                try{
                    value = value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\""));
                }
                catch (IndexOutOfBoundsException ex){
                    throw new TypeMismatchException();
                }
                break;
            case BOOLEAN:
                if(!value.equals("false") && !value.equals("true")) //TODO: Check
                    throw new TypeMismatchException();
                break;
            case CHAR:
                try{
                    value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));
                    if(value.length() > 1){
                        throw new TypeMismatchException();
                    }
                }
                catch (IndexOutOfBoundsException ex){
                    throw new TypeMismatchException();
                }

        }
    }
}
