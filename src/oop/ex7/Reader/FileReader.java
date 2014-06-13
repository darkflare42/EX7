package oop.ex7.Reader;

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


    public FileReader (File file) throws FileNotFoundException{
        scanner = new Scanner(file);
        m_filename = file.getAbsolutePath();
    }

    public FileReader(String filename) throws FileNotFoundException{
        File f = new File(filename);
        scanner =  new Scanner(f);
        m_filename = filename;
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

    public String next(Pattern pattern) {
        return scanner.next(pattern);
    }

    public String next (String pattern) {
        return scanner.next(pattern);
    }
}
