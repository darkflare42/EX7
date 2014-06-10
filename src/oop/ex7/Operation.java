package oop.ex7;

import com.sun.xml.internal.bind.v2.TODO;

/**
 * Created by Oded on 10/6/2014.
 */
public class Operation {
    private Variable varLeft;
    private Variable varRight;
    private OperationType operation;

    public Operation(Variable var1, Variable var2, String op) throws OperatorTypeException {
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
        double a = 1.2;
        int b = 1;
        int c = a + b;
        //TODO finish this. add support for all crossing variables, eg double + int
        VariableType type = varLeft.getType();
        if (varLeft.getType() == varRight.getType()) {
            VariableType type =
        } else {
            throw new OperationMismatchException();
        }
    }
}
