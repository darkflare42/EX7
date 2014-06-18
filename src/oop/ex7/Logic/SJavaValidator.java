package oop.ex7.Logic;

import oop.ex7.Logic.Exceptions.CompilationException;
import oop.ex7.Reader.SJavaReader;
import oop.ex7.Reader.IOException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class holds runs the validation algorithm on an sjava class
 * Created by Or Keren on 13/06/14.
 */
public class SJavaValidator {

    public static void validate(String filename){

        try{
            File file = new File(filename); //Throws FileNotFound
            SJavaReader reader = new SJavaReader(file); //throws filenotfound
            SyntaxCompiler.compile(reader); //Throws CompilationException
            System.out.println(0);
        }
        catch (FileNotFoundException ex){
            System.out.println(2);
        }
        catch (CompilationException ex){
            System.out.println(1);
            System.err.println(ex.getMessage());
        }
        catch (IOException ex){
            System.out.println(2);
            System.err.println(ex.getMessage());
        }

    }
}
