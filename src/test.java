import oop.ex7.Reader.FileReaderDebug;

import java.io.File;

/**
 * Created by Or Keren on 15/06/14.
 */
public class test {
    public static void main(String[] args) {
        File file = new File(FileReaderDebug.class.getResource("/tests/test001.sjava").getFile());

    }
}
