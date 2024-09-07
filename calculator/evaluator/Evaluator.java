package calculator.evaluator;

import calculator.operators.*;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {

  private final Stack<Operand> operandStack;
  private final Stack<Operator> operatorStack;


  public Evaluator() {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
  }

  public int evaluateExpression(String expression ) throws InvalidTokenException {
    String expressionToken;
    StringTokenizer expressionTokenizer;
    String delimiters = " +/*-^()";

    // 3 arguments tells the tokenizer to return delimiters as tokens
    expressionTokenizer = new StringTokenizer( expression, delimiters, true );

    while ( expressionTokenizer.hasMoreTokens() ) {
      // filter out spaces
      if ( !( expressionToken = expressionTokenizer.nextToken() ).equals( " " )) {
        // check if token is an operand
        if ( Operand.check( expressionToken )) {
          operandStack.push( new Operand( expressionToken ));
        } else {
          if ( ! Operator.check( expressionToken )) {
            throw new InvalidTokenException(expressionToken);
          }

          Operator newOperator = Operator.getOperator( expressionToken );

          if (newOperator instanceof OpeningParenthesis) {
            operatorStack.push(newOperator);
          } else if (newOperator instanceof ClosingParenthesis) {
            evaluateParenthesis();
          } else { // Other operators
            while (!operatorStack.isEmpty() && operatorStack.peek().priority() >= newOperator.priority()) {
              compute();
            }
            operatorStack.push(newOperator);
          }
        }
      }
    }

    /*
     * once no more tokens need to be scanned from StringTokenizer,
     * we need to evaluate the remaining sub-expressions.
     */
    while (!operatorStack.isEmpty()) {
      // Check for leftover opening parenthesis
      if (operatorStack.peek() instanceof OpeningParenthesis) {
        throw new InvalidTokenException("Unbalanced parenthesis");
      }
      compute();
    }

    return operandStack.pop().getValue();
  }

  /**
   * Computes the result of applying the operator on the top two operands from the stack.
   * Pops the operator and operands, computes, then the result gets pushed onto the operand stack.
   */
  private void compute() {
    Operator operatorFromStack = operatorStack.pop();
    Operand operandTwo = operandStack.pop();
    Operand operandOne = operandStack.pop();
    Operand result = operatorFromStack.execute( operandOne, operandTwo );
    operandStack.push( result );
  }

  /**
   * Evaluates the expression inside the parenthesis.
   * @throws InvalidTokenException If there is no opening parenthesis found.
   */
  private void evaluateParenthesis() throws InvalidTokenException {
    try {
      while (!(operatorStack.peek() instanceof OpeningParenthesis)) {
        compute();
      }
    } catch (EmptyStackException e) {
      throw new InvalidTokenException("Unbalanced parenthesis");
    }
    operatorStack.pop(); // Pop opening parenthesis
  }

  public static void main(String[] args) throws InvalidTokenException {
     if(args.length == 1){
      Evaluator e = new Evaluator();
      System.out.println(e.evaluateExpression(args[0]));
     }else{
      System.err.println("Invalid arguments or No expression given");
     }
  }

}
