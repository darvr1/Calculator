package tests.operator;


import calculator.operators.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


public class OperatorTest {

    @ParameterizedTest
    @CsvSource({
            "+, true",
            "-, true",
            "*, true",
            "^, true",
            "/, true",
            "156, false",
            "156., false",
            "X, false",
            "**, false"
    })
    void operatorCheckTest(String operatorToken, Boolean expectedResult ) {
        Assertions.assertEquals(Operator.check(operatorToken),expectedResult);
    }

    private static Stream<Arguments> getOperatorArgumentStream() {
        return Stream.of(
                 Arguments.of("+", AddOperator.class),
                 Arguments.of("-", SubtractOperator.class),
                 Arguments.of("/", DivideOperator.class),
                 Arguments.of("*", MultiplyOperator.class),
                 Arguments.of("^", PowerOperator.class)
               );
    }
    @ParameterizedTest
    @MethodSource("getOperatorArgumentStream")
    void getOperatorReturnTypeTest(String operatorToken, Class<Operator> expectedReturnOperatorType) {
        Assertions.assertEquals(Operator.getOperator(operatorToken).getClass(),expectedReturnOperatorType);
    }
}
