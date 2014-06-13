package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Operation {

    public static VariableEnum Operate(VariableEnum var1, String op, VariableEnum var2) throws OperationTypeException, OperationMismatchException {
        return ReturnType(var1, OperationEnum.toEnum(op), var2);
    }

    public static VariableEnum Operate(VariableEnum var1, String op, Expression var2) throws OperationTypeException, OperationMismatchException, VariableUninitializedException {
        if (var2.isInitialized()) {
            return Operate(var1, op, var2.getType());
        }
        throw new VariableUninitializedException();
    }

    public static VariableEnum Operate(Expression var1, String op, Expression var2) throws OperationTypeException, OperationMismatchException, VariableUninitializedException {
        if (var1.isInitialized() && var2.isInitialized()) {
            return Operate(var1.getType(), op, var2.getType());
        }
        throw new VariableUninitializedException();
    }

    private static VariableEnum ReturnType (VariableEnum var1, OperationEnum op, VariableEnum var2) throws OperationMismatchException {
        if (var1 == var2) {
            switch (var1) {
                case INT:
                    return VariableEnum.INT;
                case DOUBLE:
                    return VariableEnum.DOUBLE;
                case BOOLEAN:
                    throw new OperationMismatchException();
                case CHAR:
                    throw new OperationMismatchException();
                case STRING:
                    throw new OperationMismatchException(); // Java supports String addition, but s-java does not.
//                    if (operation == OperationEnum.ADD) {
//                        return VariableEnum.STRING;
//                    } else {
//                        throw new OperationMismatchException();
//                    }
            }
        } else if ((var1 == VariableEnum.INT || var2 == VariableEnum.INT)
                   && (var1 == VariableEnum.DOUBLE || var2 == VariableEnum.DOUBLE)) {
            return VariableEnum.DOUBLE;
        }

        throw new OperationMismatchException();
    }
}
