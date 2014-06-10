package oop.ex7.Reader;

import java.io.File;

/**
 * Created by Oded on 10/6/2014.
 */
public class FileReaderDebug {
    public static void main(String[] args) {
        File file = new File("C:\\Dropbox\\Univercity\\Studies\\Semester_B\\OOP\\Homework\\ex7\\EX7\\tests\\test510.sjava");
        String string = "^[0-9]+;$";
        try {
            FileReader fileReader = new FileReader(file);
            while (fileReader.hasNext()) {
//                System.in.read();
                System.out.println(fileReader.next());
            }
        }
        catch (Exception e) {
            return;
        }
    }
}
