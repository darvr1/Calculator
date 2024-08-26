package tests;

import calculator.evaluator.Evaluator;
import calculator.evaluator.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

@DisplayName("Evaluator Test")
public class EvaluatorTest {

    private Evaluator evaluator;

    @BeforeEach
    void setup() {
        evaluator = new Evaluator();
    }

    @AfterEach
    void tearDown() { evaluator = null; }

    @Test
    void basicExpressionSingleOperatorTest(){
        String testExpression = "1+2";
        int expectedResult = 3;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }

    }

    @Test
    void basicExpressionDivisionNumeratorLessThanDenominatorTest(){
        String testExpression = "1/2";
        int expectedResult = 0;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void basicExpressionMultipleOperatorTest(){
        String testExpression = "1+2*3";
        int expectedResult = 7;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }


    @Test
    void basicExpressionParensOnLeftTest(){
        String testExpression = "(1+2)*3";
        int expectedResult = 9;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void basicExpressionParensOnRightTest(){
        String testExpression = "1+(2*3)";
        int expectedResult = 7;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void mediumExpressionParensAndOperatorsTest(){
        String testExpression = "2-(3/10)+2-5";
        int expectedResult = -1;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void mediumExpressionParensAsSubExpressionTest(){
        String testExpression = "(6-12*2)/3";
        int expectedResult = -6;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }


    @Test
    void mediumExpressionPowerTest(){
        String testExpression = "3^2";
        int expectedResult = 9;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void mediumExpressionPowerWithMultipleOperators(){
        String testExpression = "3^2/2";
        int expectedResult = 4;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void mediumExpressionPowerWithParensTest(){
        String testExpression = "3^2/2 +(4+5)";
        int expectedResult = 13;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void mediumExpressionWithMultipleParensTest(){
        String testExpression = "3^2 + (2^4) +(4+5)";
        int expectedResult = 34;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void difficultExpressionMixtureOfOperatorsTest(){
        String testExpression = "3+2-9+8*2 + (3+9-8/2)";
        int expectedResult = 20;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }
    }

    @Test
    void veryDifficultExpressionMixtureOfOperatorsNestedParensTest() {
        String testExpression = "2+3-5*((2-3)*2-5*2+3*(2-3-5-5*6)+4/2)*2-9";
        int expectedResult = 1176;
        int actualResult;
        try{
            actualResult = this.evaluator.evaluateExpression(testExpression);
            Assertions.assertEquals(actualResult, expectedResult);
        }catch (Exception ex){
            Assertions.fail();
        }

    }

    @Test
    void invalidOperatorExpressionProducesInvalidTokenExceptionTest() {
        String testExpression = "2+3c*3";
        //no expected result since we are looking for an exception here.
        Assertions.assertThrows(InvalidTokenException.class, () -> {
            this.evaluator.evaluateExpression(testExpression);
        });
    }

}
