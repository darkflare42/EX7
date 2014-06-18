package oop.ex7.Reader;

import oop.ex7.Expressions.ExpressionTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class to handle reading from a file.
 * Wrapper class to Java's scanner.
 */
public class SJavaReader {
    private Scanner scanner;
    private String m_filename;
    private String m_currLine;

    /**
     * Standard constructor.
     * @param file File to read from.
     * @throws FileNotFoundException if given File is not found.
     */
    public SJavaReader(File file) throws FileNotFoundException{
        scanner = new Scanner(file);
        m_filename = file.getAbsolutePath();
    }

    /**
     * Block constructor - Create a FileReader for a specific block (e.g. of a method), instead of a whole file.
     * @param methodBlock String of the method block. Assumes, lines are separated by '\n'.
     */
    public SJavaReader(String methodBlock){
        scanner = new Scanner(methodBlock);
        m_currLine = "";
        m_filename = "";
    }

    /**
     * Check if the file has another line.
     * @return true iff there is another line in the file.
     */
    public boolean hasNext() {
        return scanner.hasNext();
    }

    /**
     * Update the current line to the next line in the File.
     * @return String of the next line in the file.
     */
    public String next() {
        String temp;
        do{
            //temp = scanner.nextLine().replace("=", " = ").replaceAll("(//.*$)", " ").replaceAll("\\s+", " ").trim();
            temp = scanner.nextLine().replace("=", " = ").replaceAll("\\s+", " ").trim();
            temp = temp.replaceAll("\\s(?=[\\[\\]\\(])", ""); //replace all spaces before and after parentheses
            temp = temp.replaceAll("\\](?=[\\w.*])", "] "); //add zero after []{char}
        }while(temp.length() < 1);
        m_currLine = temp;
        return temp;
    }

    /**
     * Reset the scanner pointer to the beginning of the file.
     * @throws IOException if the File can't be read.
     */
    public void reset() throws IOException{
        try{
            scanner = new Scanner(new File(m_filename));
        }
        catch (FileNotFoundException ex){
            throw new IOException();
        }
    }

    /**
     * Move the scanner's pointer to the next line right after the last '}' of a method block.
     * If the current scanner's pointer does not point to a beginning of a block, does nothing.
     */
    public void moveToEndOfMethod(){
        int counter = 0;
        do{
            if(m_currLine.contains("{"))
                counter++;
            if(m_currLine.contains("}"))
                counter--;
            if(counter > 0){
                next();
            }
        }while(counter > 0 && hasNext());
    }

    /**
     * Move the scanner's pointer to the first method declaration in the file.
     * If there's none, pointer will be at the end of the file.
     */
    public void moveToFirstMethodDeclaration(){
        while(ExpressionTypeEnum.checkType(m_currLine) != ExpressionTypeEnum.METHOD_DECLARATION && hasNext()){
            next();
        }
    }

    /**
     * Return the entire method block as a single String, lines separated by '\n'.
     * Assumes the pointer points at the method's opening curly bracket.
     * @return String of the method's block.
     */
    public SJavaReader getMethodBlock(){
        next(); //Read start of method block
        String methodBlock = "";
        int counter = 1;
        do{
            if(m_currLine.contains("{"))
                counter++;
            if(m_currLine.contains("}"))
                counter--;
            if(counter > 0){
                methodBlock += m_currLine +"\n"; //Insert the actual code of the method + a newline break
                next();
            }
        }while(counter > 0 && hasNext());
        return (new SJavaReader(methodBlock));
    }
}
