package tests.operator;

import calculator.evaluator.Operand;
import calculator.operators.MultiplyOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



@DisplayName("Multiplication Test")
public class MultiplyOperatorTest {

    @Test
    public void simpleMultiplicationTest(){
        Operand operandOne =  new Operand(6);
        Operand operandTwo =  new Operand(11);
        MultiplyOperator multiplyOperator =  new MultiplyOperator();
        Operand result = new Operand(multiplyOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue(), 66);
    }

    @Test
    public void simpleMultiplicationTestReversedOperands(){
        Operand operandOne =  new Operand(11);
        Operand operandTwo =  new Operand(6);
        MultiplyOperator multiplyOperator =  new MultiplyOperator();
        Operand result = new Operand(multiplyOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue(), 66);
    }

    @Test
    public void simpleMultiplicationTestNegativeOperand(){
        Operand operandOne =  new Operand(-25);
        Operand operandTwo =  new Operand(5);
        MultiplyOperator multiplyOperator =  new MultiplyOperator();
        Operand result = new Operand(multiplyOperator.execute(operandTwo,operandOne).getValue());
        Assertions.assertEquals(result.getValue(), -125);
    }

    @Test
    public void simpleMultiplicationTestNegativeOperandReversed(){
        Operand operandOne =  new Operand(25);
        Operand operandTwo =  new Operand(-5);
        MultiplyOperator multiplyOperator =  new MultiplyOperator();
        Operand result = new Operand(multiplyOperator.execute(operandTwo,operandOne).getValue());
        Assertions.assertEquals(result.getValue(),-125);
    }

    @Test
    public void multiplicationPriorityTest(){
        Assertions.assertEquals((new MultiplyOperator().priority()), 2);
    }
}
