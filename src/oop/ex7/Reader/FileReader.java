package oop.ex7.Reader;

import jdk.internal.jfr.events.FileReadEvent;
import oop.ex7.Expressions.ExpressionTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Oded on 10/6/2014.
 */
public class FileReader {
    private Scanner scanner;
    private String m_filename;
    private String m_currLine;


    public FileReader (File file) throws FileNotFoundException{
        scanner = new Scanner(file);
        m_filename = file.getAbsolutePath();
    }

    public FileReader(String methodBlock){
        scanner = new Scanner(methodBlock);
        m_currLine = "";
        m_filename = "";
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String next() {
        /*
        Pattern pattern = Pattern.compile(".*");
        scanner.useDelimiter("[;|{]");
        return scanner.next(pattern).trim();
        */
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

    public void reset() throws IOException{
        try{
            scanner = new Scanner(new File(m_filename));
        }
        catch (FileNotFoundException ex){
            throw new IOException();
        }
    }

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

    public void moveToFirstMethodDeclaration(){
        while(ExpressionTypeEnum.checkType(m_currLine) != ExpressionTypeEnum.METHOD_DECLARATION && hasNext()){
            next();
        }
    }

    public FileReader getMethodBlock(){
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
        return (new FileReader(methodBlock));
    }
}
