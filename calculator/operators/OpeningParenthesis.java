package calculator.operators;

import calculator.evaluator.Operand;

public class OpeningParenthesis extends Operator {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        // Cannot do operations
        return null;
    }
}
