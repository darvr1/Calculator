package tests.operator;

import calculator.evaluator.Operand;
import calculator.operators.SubtractOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;



@DisplayName("Subtraction Test")
public class SubtractOperatorTest {

    @Test
    public void simpleSubtractionTest(){
        Operand operatorOne =  new Operand(6);
        Operand operatorTwo =  new Operand(11);
        SubtractOperator so =  new SubtractOperator();
        Operand result = new Operand(so.execute(operatorOne,operatorTwo).getValue());
        Assertions.assertEquals(result.getValue(), -5);
    }

    @Test
    public void simpleSubtractionTestReversedOperands(){
        Operand operatorOne =  new Operand(11);
        Operand operatorTwo =  new Operand(6);
        SubtractOperator so =  new SubtractOperator();
        Operand result = new Operand(so.execute(operatorOne,operatorTwo).getValue());
        Assertions.assertEquals( result.getValue(), 5);
    }

    @Test
    public void simpleSubtractionTestNegativeOperands(){
        Operand operatorOne =  new Operand(-11);
        Operand operatorTwo =  new Operand(6);
        SubtractOperator so =  new SubtractOperator();
        Operand result = new Operand(so.execute(operatorOne,operatorTwo).getValue());
        Assertions.assertEquals(result.getValue(), -17);
    }

    @Test
    public void simpleSubtractionTestNegativeOperandsReversed(){
        Operand operandOne =  new Operand(11);
        Operand operatorTwo =  new Operand(-6);
        SubtractOperator so =  new SubtractOperator();
        Operand result = new Operand(so.execute(operandOne,operatorTwo).getValue());
        Assertions.assertEquals( result.getValue(), 17);
    }

    @Test
    public void subtractionPriorityTest(){
        Assertions.assertEquals((new SubtractOperator().priority()), 1);
    }
}
