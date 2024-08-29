package tests.operator;

import calculator.evaluator.Operand;
import calculator.operators.DivideOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Division Test")
public class DivideOperatorTest {

    @Test
    public void simpleDivisionTest(){
        Operand operandOne =  new Operand(6);
        Operand operandTwo =  new Operand(11);
        DivideOperator divideOperator =  new DivideOperator();
        Operand result = new Operand(divideOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue() , 0);
    }

    @Test
    public void simpleDivisionTestReversedOperands(){
        Operand operandOne =  new Operand(11);
        Operand operandTwo =  new Operand(6);
        DivideOperator divideOperator =  new DivideOperator();
        Operand result = new Operand(divideOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals( result.getValue(), 1);
    }

    @Test
    public void simpleDivisionTestEvenlyDivisible() {
        Operand operandOne =  new Operand(25);
        Operand operandTwo =  new Operand(5);
        DivideOperator divideOperator =  new DivideOperator();
        Operand result = new Operand(divideOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue() , 5);
    }

    @Test
    public void simpleDivisionTestNegativeResult (){
        Operand operandOne =  new Operand(25);
        Operand operandTwo =  new Operand(-5);
        DivideOperator divideOperator =  new DivideOperator();
        Operand result = new Operand(divideOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals( result.getValue() ,-5);
    }

    @Test
    public void divisionPriorityTest(){
        Assertions.assertEquals((new DivideOperator().priority()),2);
    }
}
