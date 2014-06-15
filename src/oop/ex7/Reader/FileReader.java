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

    /*
    public FileReader(String filename) throws FileNotFoundException{
        File f = new File(filename);
        scanner =  new Scanner(f);
        m_filename = filename;
    }
    */


    public FileReader(String methodBlock){
        scanner = new Scanner(methodBlock);
        m_currLine = "";
        m_filename = "";
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public boolean hasNext(Pattern pattern) {
        return scanner.hasNext(pattern);
    }

    public boolean hasNext (String pattern) {
        return scanner.hasNext(pattern);
    }

    //TODO: check for int a\n=5
    public String next() {
        /*
        Pattern pattern = Pattern.compile(".*");
        scanner.useDelimiter("[;|{]");
        return scanner.next(pattern).trim();
        */
        String temp;
        do{
            temp = scanner.nextLine().replace("=", " = ").replaceAll("(/\\*.*\\*/|//.*$)", " ").replaceAll("\\s+", " ").trim();
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

    public String next(Pattern pattern) {
        return scanner.next(pattern);
    }

    public String next (String pattern) {
        return scanner.next(pattern);
    }
}
