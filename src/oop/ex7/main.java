package oop.ex7;

/**
 * Created by Or Keren on 09/06/14.
 */
public class main {
    public static void main(){
        String test;
        test = "hello";
        Test enumTest = Test.Hello;


        switch(enumTest){
            case World:
                break;
            case Hello:
                break;
        }


    }
}
