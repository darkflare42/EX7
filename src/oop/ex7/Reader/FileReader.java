package oop.ex7.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Oded on 10/6/2014.
 */
public class FileReader {
    Scanner scanner;

    public FileReader (File file) throws FileNotFoundException{
        scanner = new Scanner(file);
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

    public String next(Pattern pattern) {
        return scanner.next(pattern);
    }

    public String next (String pattern) {
        return scanner.next(pattern);
    }
}
