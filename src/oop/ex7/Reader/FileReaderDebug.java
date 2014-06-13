package oop.ex7.Reader;

import oop.ex7.Expressions.ExpressionTypeEnum;

import java.io.File;

/**
 * Created by Oded on 10/6/2014.
 */
public class FileReaderDebug {
    public static void main(String[] args) {

        //File file = new File("..\\tests\\test510.sjava");
        File file = new File(FileReaderDebug.class.getResource("/oop/ex7/tests/test001.sjava").getFile());
        String string = "^[0-9]+;$";
        ExpressionTypeEnum expressionType;
        String test  = ExpressionTypeEnum.MEMBER_DECLARATION_REGEX;
        try {
            FileReader fileReader = new FileReader(file);
            while (fileReader.hasNext()) {
                //System.in.read();
                expressionType = ExpressionTypeEnum.checkType(fileReader.next());
                //System.out.println(fileReader.next());
            }
        }
        catch (Exception e) {
            return;
        }
    }
}
