package tests.operand;


import calculator.evaluator.Operand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Operand Test")
public class OperandTest {

    @Test
    public void getValueTest(){
        Operand operand = new Operand(6);
        /* note the use of is() here is purely done for readability.
        the is function does nothing in terms of functionality.
         */
        Assertions.assertEquals(operand.getValue(),6);
    }

    @Test
    public void getValueTypeTest(){
        Operand operand = new Operand(6);
        /* the isA() function is used to test types,
        we are saying is the return value from getValue() actually
        an Integer.
         */
        Assertions.assertTrue(Integer.class.isInstance(operand.getValue()));
    }
    
    @ParameterizedTest
    @CsvSource({
            "13, true",
            "c, false",
            "*, false",
            "430., false",
            "430.456, false",
            "343324fd, false"
    })
    public void operandCheckTest(String testValue, boolean expectedResult){
        Assertions.assertEquals(Operand.check(testValue),expectedResult);
    }
}
