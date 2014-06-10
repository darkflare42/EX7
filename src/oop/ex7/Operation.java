package oop.ex7;

import com.sun.xml.internal.bind.v2.TODO;

/**
 * Created by Oded on 10/6/2014.
 */
public class Operation {
    private VariableType varLeft;
    private VariableType varRight;
    private OperationType operation;

    /**
     * Operation constructor. Receives the VariableTypes of 2 operands and a string of an operator.
     * If the caller would have sent strings of the names of the variables, then it would also have
     * to send the database of all initialized values and it would have to cross-check for initialzied values, as such
     * the caller is the one that should handle that.
     * @param var1
     * @param var2
     * @param op
     * @throws OperatorTypeException
     */
    public Operation(VariableType var1, VariableType var2, String op) throws OperatorTypeException {
        varLeft = var1;
        varRight = var2;
        operation = FindOperation(op);
    }

    private OperationType FindOperation (String op) throws OperatorTypeException {
        if (op.equals("+")) {
            return OperationType.ADD;
        } else if (op.equals("-")) {
            return OperationType.SUBTRACT;
        } else if (op.equals("*")) {
            return OperationType.MULTIPLY;
        } else if (op.equals("/")) {
            return OperationType.DIVIDE;
        } else {
            throw new OperatorTypeException();
        }
    }

    public VariableType ReturnType () throws OperationMismatchException {
        double a = 1.2; //DEBUG
        int b = 1;//DEBUG
        double c = a + b;//DEBUG
        //TODO finish this. add support for all crossing variables, eg double + int
        if (varLeft == varRight) {
            VariableType type =
        } else {
            throw new OperationMismatchException();
        }
    }
}
