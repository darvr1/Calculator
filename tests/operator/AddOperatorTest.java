package tests.operator;

import calculator.evaluator.Operand;

import calculator.operators.AddOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Addition Test")
public class AddOperatorTest {

    @Test
    public void simpleAdditionTest(){
        Operand operandOne =  new Operand(6);
        Operand operandTwo =  new Operand(11);
        AddOperator addOperator =  new AddOperator();
        Operand result = new Operand(addOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue(), 17);
    }

    @Test
    public void simpleAdditionTestReverseOperands(){
        Operand operandOne =  new Operand(11);
        Operand operandTwo =  new Operand(6);
        AddOperator addOperator =  new AddOperator();
        Operand result = new Operand(addOperator.execute(operandOne,operandTwo).getValue());
        //assertEquals(17, result.getValue());
        Assertions.assertEquals(result.getValue(), 17);
    }

    @Test
    public void simpleAdditionTestWithNegativeOperand(){
        Operand operandOne =  new Operand(6);
        Operand operandTwo =  new Operand(-11);
        AddOperator addOperator =  new AddOperator();
        Operand result = new Operand(addOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue(), -5);
    }

    @Test
    public void simpleAdditionTestWithNegativeOperandsReversed(){
        Operand operandOne =  new Operand(-11);
        Operand operandTwo =  new Operand(6);
        AddOperator addOperator =  new AddOperator();
        Operand result = new Operand(addOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(result.getValue(),-5);
    }

    @Test
    public void additionPriorityTest(){
        Assertions.assertEquals((new AddOperator().priority()), 1);
    }
}
