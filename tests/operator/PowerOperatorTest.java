package tests.operator;


import calculator.evaluator.Operand;
import calculator.operators.PowerOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



@DisplayName("Power Test")
public class PowerOperatorTest {

    @Test
    public void simplePowerTest(){
        Operand operandOne =  new Operand(2);
        Operand operandTwo =  new Operand(3);
        PowerOperator powerOperator =  new PowerOperator();
        Operand res = new Operand(powerOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(res.getValue(), 8);
    }

    @Test
    public void simplePowerTestNegativeBase(){
        Operand operandOne =  new Operand(-2);
        Operand operandTwo =  new Operand(8);
        PowerOperator powerOperator =  new PowerOperator();
        Operand res = new Operand(powerOperator.execute(operandOne,operandTwo).getValue());
        Assertions.assertEquals(res.getValue(), 256);
    }


    @Test
    public void powerPriorityTest(){
        Assertions.assertEquals(new PowerOperator().priority(), 3);
    }
}
