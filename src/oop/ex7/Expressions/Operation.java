package oop.ex7.Expressions;

import oop.ex7.Expressions.Exceptions.OperationMismatchException;
import oop.ex7.Expressions.Exceptions.OperationTypeException;
import oop.ex7.Expressions.Exceptions.VariableUninitializedException;

/**
 * Class to handle an operation of 2 Expressions.
 */
public class Operation {

    /**
     * Returns the Variable Type result of an operation on 2 given VariableEnums and an operation.
     * @param var1 VariableEnum of the LEFT Expressions in the operation.
     * @param op String of the operation.
     * @param var2 VariableEnum of the RIGHT Expressions in the operation.
     * @return VariableEnum of the result of the operation.
     * @throws OperationTypeException if op is not a string of a valid operation.
     * @throws OperationMismatchException if the operation was attempted on 2 Expressions that can't have the operation performed on them.
     */
    public static VariableEnum Operate(VariableEnum var1, String op, VariableEnum var2) throws OperationTypeException, OperationMismatchException {
        return ReturnType(var1, OperationEnum.toEnum(op), var2);
    }

    /**
     * Returns the Variable Type result of an operation on a VariableEnum and an Expression and an operation.
     * @param var1 Expression LEFT to the operation.
     * @param op String of the operation.
     * @param var2 Expression RIGHT to the operation.
     * @return VariableEnum of the result of the operation.
     * @throws OperationTypeException if op is not a string of a valid operation.
     * @throws OperationMismatchException if the operation was attempted on 2 Expressions that can't have the operation performed on them.
     * @throws VariableUninitializedException if var2 is not initialized.
     */
    public static VariableEnum Operate(VariableEnum var1, String op, Expression var2) throws OperationTypeException, OperationMismatchException, VariableUninitializedException {
        if (var2.isInitialized()) {
            return Operate(var1, op, var2.getType());
        }
        throw new VariableUninitializedException();
    }

    /**
     * Returns the Variable Type result of an operation on 2 Expressions and an operation.
     * @param var1 VariableEnum of the LEFT Expressions in the operation.
     * @param op String of the operation.
     * @param var2 VariableEnum of the RIGHT Expressions in the operation.
     * @return VariableEnum of the result of the operation.
     * @throws OperationTypeException if op is not a string of a valid operation.
     * @throws OperationMismatchException if the operation was attempted on 2 Expressions that can't have the operation performed on them.
     * @throws VariableUninitializedException if var1 or var2 are not initialized.
     */
    public static VariableEnum Operate(Expression var1, String op, Expression var2) throws OperationTypeException, OperationMismatchException, VariableUninitializedException {
        if (var1.isInitialized() && var2.isInitialized()) {
            return Operate(var1.getType(), op, var2.getType());
        }
        throw new VariableUninitializedException();
    }

    /**
     * Static method to compute the VariableEnum that will be returned by an operation.
     * @param var1 VariableEnum of the LEFT Expressions in the operation.
     * @param op String of the operation.
     * @param var2 VariableEnum of the RIGHT Expressions in the operation.
     * @return VariableEnum of the result of the operation.
     * @throws OperationMismatchException if the operation was attempted on 2 Expressions that can't have the operation performed on them.
     */
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
