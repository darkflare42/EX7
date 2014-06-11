package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Array {
    VariableEnum type;
    String name;

    public Array (String arrType, String arrName) throws VariableTypeException {
        type = VariableEnum.toEnum(arrType);
        name = arrName;
    }

}
