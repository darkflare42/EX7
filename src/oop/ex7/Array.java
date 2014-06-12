package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Array implements Expression{
    private VariableEnum type;
    private String name;
    private boolean initialized = false;

    public Array (String arrType, String arrName) throws VariableTypeException {
        type = VariableEnum.toEnum(arrType);
        name = arrName;
    }

    public Array (String arrType, String arrName, boolean initialize) throws VariableTypeException {
        type = VariableEnum.toEnum(arrType);
        name = arrName;
        initialized = initialize;
    }

    public String getName() {
        return name;
    }

    public VariableEnum getType() {
        return type;
    }

    public boolean isInitialized () {
        return initialized;
    }
}
