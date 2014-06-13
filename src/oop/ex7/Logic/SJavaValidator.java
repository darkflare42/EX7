package oop.ex7.Logic;

import oop.ex7.Logic.Exceptions.CompilationException;
import oop.ex7.Reader.FileReader;
import oop.ex7.Reader.IOException;

import java.io.FileNotFoundException;

/**
 * This class holds runs the validation algorithm on an sjava class
 * Created by Or Keren on 13/06/14.
 */
public class SJavaValidator {

    public static void validate(String filename){

        try{
            FileReader reader = new FileReader(filename); //throws filenotfound
            SyntaxCompiler.compile(reader);
            RunTimeCompiler.compile(); //TODO: implement
        }
        catch (FileNotFoundException ex){

        }
        catch (CompilationException ex){

        }
        catch (IOException ex){

        }

    }
}
