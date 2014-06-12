package oop.ex7;

/**
 * Created by Oded on 10/6/2014.
 */
public class Operation {
    private VariableEnum varLeft;
    private VariableEnum varRight;
    private OperationEnum operation;

    /**
     * Operation constructor. Receives the VariableTypes of 2 operands and a string of an operator.
     * If the caller would have sent strings of the names of the variables, then it would also have
     * to send the database of all initialized values and it would have to cross-check for initialzied values, as such
     * the caller is the one that should handle that.
     * @param var1
     * @param var2
     * @param op
     * @throws OperationTypeException
     */
    public Operation(VariableEnum var1, String op, VariableEnum var2) throws OperationTypeException {
        varLeft = var1;
        varRight = var2;
        operation = OperationEnum.toEnum(op);
    }

    public Operation(VariableEnum var1, String op, Variable var2) throws OperationTypeException, OperationUninitializedVariableException {
        varLeft = var1;
        if (var2.isInitialized()) {
            varRight = var2.getType();
        } else {
            throw new OperationUninitializedVariableException ();
        }
        operation = OperationEnum.toEnum(op);
    }

    public Operation(Variable var1, String op, Variable var2) throws OperationTypeException, OperationUninitializedVariableException {
        if (var1.isInitialized() && var2.isInitialized()) {
            varLeft = var1.getType();
            varRight = var2.getType();
        } else {
            throw new OperationUninitializedVariableException ();
        }
        operation = OperationEnum.toEnum(op);
    }

    public VariableEnum ReturnType () throws OperationMismatchException {
        if (varLeft == varRight) {
            switch (varRight) {
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
        } else if ((varLeft == VariableEnum.INT || varRight == VariableEnum.INT)
                   && (varLeft == VariableEnum.DOUBLE || varRight == VariableEnum.DOUBLE)) {
            return VariableEnum.DOUBLE;
        }

        throw new OperationMismatchException();
    }
}
