

# Grading Report for P1



## Student Name: 

Dale.Rivera


## Commit Count: 

21



## Git Diff Since Base Commit: 


<details>
    <summary>Git Diff</summary>

~~~bash
diff --git a/.gitignore b/.gitignore
new file mode 100644
index 0000000..977d04a
--- /dev/null
+++ b/.gitignore
@@ -0,0 +1,4 @@
+.idea
+calculator-darvr1.iml
+target/
+out/
\ No newline at end of file
diff --git a/README.md b/README.md
index 9d31d25..754c3e0 100644
--- a/README.md
+++ b/README.md
@@ -2,11 +2,11 @@
 
 ## Student Information
 
-### Student Name  : Name here
+### Student Name  : Dale Rivera
 
-### Student ID    : ID here
+### Student ID    : 922792075
 
-### Student Email : Email here
+### Student Email : drivera@sfsu.edu
 
 ## Requirements
 
diff --git a/calculator/evaluator/Evaluator.java b/calculator/evaluator/Evaluator.java
index 3ce44bc..beac4b1 100644
--- a/calculator/evaluator/Evaluator.java
+++ b/calculator/evaluator/Evaluator.java
@@ -2,6 +2,7 @@ package calculator.evaluator;
 
 import calculator.operators.*;
 
+import java.util.EmptyStackException;
 import java.util.Stack;
 import java.util.StringTokenizer;
 
@@ -19,7 +20,7 @@ public class Evaluator {
   public int evaluateExpression(String expression ) throws InvalidTokenException {
     String expressionToken;
     StringTokenizer expressionTokenizer;
-    String delimiters = " +/*-^";
+    String delimiters = " +/*-^()";
 
     // 3 arguments tells the tokenizer to return delimiters as tokens
     expressionTokenizer = new StringTokenizer( expression, delimiters, true );
@@ -35,31 +36,62 @@ public class Evaluator {
             throw new InvalidTokenException(expressionToken);
           }
 
+          Operator newOperator = Operator.getOperator( expressionToken );
 
-          // TODO fix this line of code.
-          Operator newOperator = new Operator();
-
-         
-            while (operatorStack.peek().priority() >= newOperator.priority() ) {
-              Operator operatorFromStack = operatorStack.pop();
-              Operand operandTwo = operandStack.pop();
-              Operand operandOne = operandStack.pop();
-              Operand result = operatorFromStack.execute( operandOne, operandTwo );
-              operandStack.push( result );
+          if (newOperator instanceof OpeningParenthesis) {
+            operatorStack.push(newOperator);
+          } else if (newOperator instanceof ClosingParenthesis) {
+            evaluateParenthesis();
+          } else { // Other operators
+            while (!operatorStack.isEmpty() && operatorStack.peek().priority() >= newOperator.priority()) {
+              compute();
             }
-
-            operatorStack.push( newOperator );
-          
+            operatorStack.push(newOperator);
+          }
         }
       }
     }
 
-
     /*
      * once no more tokens need to be scanned from StringTokenizer,
      * we need to evaluate the remaining sub-expressions.
      */
-    return 0;
+    while (!operatorStack.isEmpty()) {
+      // Check for leftover opening parenthesis
+      if (operatorStack.peek() instanceof OpeningParenthesis) {
+        throw new InvalidTokenException("Unbalanced parenthesis");
+      }
+      compute();
+    }
+
+    return operandStack.pop().getValue();
+  }
+
+  /**
+   * Computes the result of applying the operator on the top two operands from the stack.
+   * Pops the operator and operands, computes, then the result gets pushed onto the operand stack.
+   */
+  private void compute() {
+    Operator operatorFromStack = operatorStack.pop();
+    Operand operandTwo = operandStack.pop();
+    Operand operandOne = operandStack.pop();
+    Operand result = operatorFromStack.execute( operandOne, operandTwo );
+    operandStack.push( result );
+  }
+
+  /**
+   * Evaluates the expression inside the parenthesis.
+   * @throws InvalidTokenException If there is no opening parenthesis found.
+   */
+  private void evaluateParenthesis() throws InvalidTokenException {
+    try {
+      while (!(operatorStack.peek() instanceof OpeningParenthesis)) {
+        compute();
+      }
+    } catch (EmptyStackException e) {
+      throw new InvalidTokenException("Unbalanced parenthesis");
+    }
+    operatorStack.pop(); // Pop opening parenthesis
   }
 
   public static void main(String[] args) throws InvalidTokenException {
diff --git a/calculator/evaluator/EvaluatorUI.java b/calculator/evaluator/EvaluatorUI.java
index 0f391f6..10ea989 100644
--- a/calculator/evaluator/EvaluatorUI.java
+++ b/calculator/evaluator/EvaluatorUI.java
@@ -5,11 +5,13 @@ import javax.swing.*;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
+import java.util.EmptyStackException;
 
 public class EvaluatorUI extends JFrame implements ActionListener {
 
      private JTextField expressionTextField = new JTextField();
      private JPanel buttonPanel = new JPanel();
+     private final Evaluator calculator = new Evaluator();
 
      // total of 20 buttons on the calculator,
      // numbered from left to right, top to bottom
@@ -66,7 +68,31 @@ public class EvaluatorUI extends JFrame implements ActionListener {
       *                          button is pressed.
       */
      public void actionPerformed(ActionEvent actionEventObject) {
+         String expression;
+         String buttonInput = actionEventObject.getActionCommand();
 
-
+         switch (buttonInput) {
+             case "=":
+                 expression = expressionTextField.getText();
+                 try {
+                     String result = String.valueOf(calculator.evaluateExpression(expression));
+                     expressionTextField.setText(result);
+                 } catch (InvalidTokenException e) {
+                     System.err.println("Invalid token in expression: " + expression);
+                 }
+                 break;
+             case "CE":
+                 String a = expressionTextField.getText();
+                 // Remove the last character
+                 if (!a.isEmpty()) expressionTextField.setText(a.substring(0, a.length() - 1));
+                 break;
+             case "C":
+                 expressionTextField.setText("");
+                 break;
+             default:
+                 expression = expressionTextField.getText();
+                 // Concatenate input with current expression
+                 expressionTextField.setText(expression + buttonInput);
+         }
      }
- }
+ }
\ No newline at end of file
diff --git a/calculator/evaluator/Operand.java b/calculator/evaluator/Operand.java
index 7813d0c..a305641 100644
--- a/calculator/evaluator/Operand.java
+++ b/calculator/evaluator/Operand.java
@@ -5,25 +5,31 @@ package calculator.evaluator;
  * in a valid mathematical expression.
  */
 public class Operand {
+    private final int number;
+
     /**
      * construct operand from string token.
      */
-    public Operand(String token) {
-
+    public Operand(String token) throws InvalidTokenException {
+        if (check(token)) {
+            number = Integer.parseInt(token);
+        } else {
+            throw new InvalidTokenException(token);
+        }
     }
 
     /**
      * construct operand from integer
      */
     public Operand(int value) {
-
+        this.number = value;
     }
 
     /**
      * return value of operand
      */
     public int getValue() {
-        return 0;
+        return this.number;
     }
 
     /**
@@ -31,6 +37,11 @@ public class Operand {
      * operand.
      */
     public static boolean check(String token) {
-        return false;
+        try {
+            Integer.parseInt(token);
+            return true;
+        } catch (NumberFormatException e) {
+            return false;
+        }
     }
 }
diff --git a/calculator/operators/AddOperator.java b/calculator/operators/AddOperator.java
new file mode 100644
index 0000000..80f3fad
--- /dev/null
+++ b/calculator/operators/AddOperator.java
@@ -0,0 +1,15 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class AddOperator extends Operator {
+    @Override
+    public int priority() {
+        return 1;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        return new Operand(operandOne.getValue() + operandTwo.getValue());
+    }
+}
diff --git a/calculator/operators/ClosingParenthesis.java b/calculator/operators/ClosingParenthesis.java
new file mode 100644
index 0000000..eccce23
--- /dev/null
+++ b/calculator/operators/ClosingParenthesis.java
@@ -0,0 +1,16 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class ClosingParenthesis extends Operator {
+    @Override
+    public int priority() {
+        return 0;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        // Cannot do operations
+        return null;
+    }
+}
diff --git a/calculator/operators/DivideOperator.java b/calculator/operators/DivideOperator.java
new file mode 100644
index 0000000..a2ea358
--- /dev/null
+++ b/calculator/operators/DivideOperator.java
@@ -0,0 +1,15 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class DivideOperator extends Operator {
+    @Override
+    public int priority() {
+        return 2;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        return new Operand(operandOne.getValue() / operandTwo.getValue());
+    }
+}
diff --git a/calculator/operators/MultiplyOperator.java b/calculator/operators/MultiplyOperator.java
new file mode 100644
index 0000000..1939343
--- /dev/null
+++ b/calculator/operators/MultiplyOperator.java
@@ -0,0 +1,15 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class MultiplyOperator extends Operator {
+    @Override
+    public int priority() {
+        return 2;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        return new Operand(operandOne.getValue() * operandTwo.getValue());
+    }
+}
\ No newline at end of file
diff --git a/calculator/operators/OpeningParenthesis.java b/calculator/operators/OpeningParenthesis.java
new file mode 100644
index 0000000..b7bc1fd
--- /dev/null
+++ b/calculator/operators/OpeningParenthesis.java
@@ -0,0 +1,16 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class OpeningParenthesis extends Operator {
+    @Override
+    public int priority() {
+        return 0;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        // Cannot do operations
+        return null;
+    }
+}
diff --git a/calculator/operators/Operator.java b/calculator/operators/Operator.java
index a64ffda..b7058ff 100644
--- a/calculator/operators/Operator.java
+++ b/calculator/operators/Operator.java
@@ -2,6 +2,9 @@ package calculator.operators;
 
 import calculator.evaluator.Operand;
 
+import java.util.HashMap;
+import java.util.Map;
+
 public abstract class Operator {
     // The Operator class should contain an instance of a HashMap
     // This map will use keys as the tokens we're interested in,
@@ -14,6 +17,16 @@ public abstract class Operator {
     // operators.put( "+", new AdditionOperator() );
     // operators.put( "-", new SubtractionOperator() );
 
+    private static final Map<String, Operator> operators = new HashMap<>();
+    static {
+         operators.put("+", new AddOperator());
+         operators.put("-", new SubtractOperator());
+         operators.put("*", new MultiplyOperator());
+         operators.put("/", new DivideOperator());
+         operators.put("^", new PowerOperator());
+         operators.put("(", new OpeningParenthesis());
+         operators.put(")", new ClosingParenthesis());
+    }
 
     /**
      * retrieve the priority of an Operator
@@ -38,7 +51,7 @@ public abstract class Operator {
      * @return reference to a Operator instance.
      */
     public static Operator getOperator(String token) {
-        return null;
+        return operators.get(token);
     }
 
     
@@ -49,6 +62,6 @@ public abstract class Operator {
      * Think about what happens if we add more operators.
      */
     public static boolean check(String token) {
-        return false;
+        return operators.containsKey(token);
     }
 }
diff --git a/calculator/operators/PowerOperator.java b/calculator/operators/PowerOperator.java
new file mode 100644
index 0000000..5fffdf0
--- /dev/null
+++ b/calculator/operators/PowerOperator.java
@@ -0,0 +1,15 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class PowerOperator extends Operator {
+    @Override
+    public int priority() {
+        return 3;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        return new Operand((int) Math.pow(operandOne.getValue(), operandTwo.getValue()));
+    }
+}
diff --git a/calculator/operators/SubtractOperator.java b/calculator/operators/SubtractOperator.java
new file mode 100644
index 0000000..e832ed6
--- /dev/null
+++ b/calculator/operators/SubtractOperator.java
@@ -0,0 +1,15 @@
+package calculator.operators;
+
+import calculator.evaluator.Operand;
+
+public class SubtractOperator extends Operator {
+    @Override
+    public int priority() {
+        return 1;
+    }
+
+    @Override
+    public Operand execute(Operand operandOne, Operand operandTwo) {
+        return new Operand(operandOne.getValue() - operandTwo.getValue());
+    }
+}
diff --git a/documentation/Rivera_Dale.pdf b/documentation/Rivera_Dale.pdf
new file mode 100644
index 0000000..ab90ceb
Binary files /dev/null and b/documentation/Rivera_Dale.pdf differ
diff --git a/documentation/UML.png b/documentation/UML.png
new file mode 100644
index 0000000..0603776
Binary files /dev/null and b/documentation/UML.png differ
diff --git a/documentation/lastname_firstname.docx b/documentation/lastname_firstname.docx
deleted file mode 100644
index 170f6c6..0000000
Binary files a/documentation/lastname_firstname.docx and /dev/null differ
diff --git a/tests/operator/AddOperatorTest.java b/tests/operator/AddOperatorTest.java
index 127c11e..5e106d9 100644
--- a/tests/operator/AddOperatorTest.java
+++ b/tests/operator/AddOperatorTest.java
@@ -2,6 +2,7 @@ package tests.operator;
 
 import calculator.evaluator.Operand;
 
+import calculator.operators.AddOperator;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
diff --git a/tests/operator/DivideOperatorTest.java b/tests/operator/DivideOperatorTest.java
index ce169a8..c2147f6 100644
--- a/tests/operator/DivideOperatorTest.java
+++ b/tests/operator/DivideOperatorTest.java
@@ -1,6 +1,7 @@
 package tests.operator;
 
 import calculator.evaluator.Operand;
+import calculator.operators.DivideOperator;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
diff --git a/tests/operator/MultiplyOperatorTest.java b/tests/operator/MultiplyOperatorTest.java
index 1187039..9e05b22 100644
--- a/tests/operator/MultiplyOperatorTest.java
+++ b/tests/operator/MultiplyOperatorTest.java
@@ -1,6 +1,7 @@
 package tests.operator;
 
 import calculator.evaluator.Operand;
+import calculator.operators.MultiplyOperator;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
diff --git a/tests/operator/PowerOperatorTest.java b/tests/operator/PowerOperatorTest.java
index 5711132..d4fbeeb 100644
--- a/tests/operator/PowerOperatorTest.java
+++ b/tests/operator/PowerOperatorTest.java
@@ -2,6 +2,7 @@ package tests.operator;
 
 
 import calculator.evaluator.Operand;
+import calculator.operators.PowerOperator;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
diff --git a/tests/operator/SubtractOperatorTest.java b/tests/operator/SubtractOperatorTest.java
index 7b35647..2d09ad3 100644
--- a/tests/operator/SubtractOperatorTest.java
+++ b/tests/operator/SubtractOperatorTest.java
@@ -1,6 +1,7 @@
 package tests.operator;
 
 import calculator.evaluator.Operand;
+import calculator.operators.SubtractOperator;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.Assertions;

~~~

</details>




## Compiling Source Code Results: 



~~~bash

~~~
    


## Compiling Unit Tests Results: 



~~~bash

~~~
    


## Code Review


<details>
    <summary>./calculator/evaluator/Operand.java</summary>

~~~java
package calculator.evaluator;

/**
 * Operand class used to represent an operand
 * in a valid mathematical expression.
 */
public class Operand {
    private final int number;

    /**
     * construct operand from string token.
     */
    public Operand(String token) throws InvalidTokenException {
        if (check(token)) {
            number = Integer.parseInt(token);
        } else {
            throw new InvalidTokenException(token);
        }
    }

    /**
     * construct operand from integer
     */
    public Operand(int value) {
        this.number = value;
    }

    /**
     * return value of operand
     */
    public int getValue() {
        return this.number;
    }

    /**
     * Check to see if given token is a valid
     * operand.
     */
    public static boolean check(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/ClosingParenthesis.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class ClosingParenthesis extends Operator {
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

~~~

</details>



<details>
    <summary>./calculator/operators/Operator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

import java.util.HashMap;
import java.util.Map;

public abstract class Operator {
    // The Operator class should contain an instance of a HashMap
    // This map will use keys as the tokens we're interested in,
    // and values will be instances of the Operators.
    // ALL subclasses of operator MUST be in their own file.
    // Example:
    // Where does this declaration go? What should its access level be?
    // Class or instance variable? Is this the right declaration?
    // HashMap operators = new HashMap();
    // operators.put( "+", new AdditionOperator() );
    // operators.put( "-", new SubtractionOperator() );

    private static final Map<String, Operator> operators = new HashMap<>();
    static {
         operators.put("+", new AddOperator());
         operators.put("-", new SubtractOperator());
         operators.put("*", new MultiplyOperator());
         operators.put("/", new DivideOperator());
         operators.put("^", new PowerOperator());
         operators.put("(", new OpeningParenthesis());
         operators.put(")", new ClosingParenthesis());
    }

    /**
     * retrieve the priority of an Operator
     * @return priority of an Operator as an int
     */
    public abstract int priority();

    /**
     * Abstract method to execute an operator given two operands.
     * @param operandOne first operand of operator
     * @param operandTwo second operand of operator
     * @return an operand of the result of the operation.
     */
    public abstract Operand execute(Operand operandOne, Operand operandTwo);

    /**
     * used to retrieve an operator from our HashMap.
     * This will act as out publicly facing function,
     * granting access to the Operator HashMap.
     *
     * @param token key of the operator we want to retrieve
     * @return reference to a Operator instance.
     */
    public static Operator getOperator(String token) {
        return operators.get(token);
    }

    
     /**
     * determines if a given token is a valid operator.
     * please do your best to avoid static checks
     * for example token.equals("+") and so on.
     * Think about what happens if we add more operators.
     */
    public static boolean check(String token) {
        return operators.containsKey(token);
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/AddOperator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class AddOperator extends Operator {
    @Override
    public int priority() {
        return 1;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        return new Operand(operandOne.getValue() + operandTwo.getValue());
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/OpeningParenthesis.java</summary>

~~~java
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

~~~

</details>



<details>
    <summary>./calculator/operators/PowerOperator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class PowerOperator extends Operator {
    @Override
    public int priority() {
        return 3;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        return new Operand((int) Math.pow(operandOne.getValue(), operandTwo.getValue()));
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/DivideOperator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class DivideOperator extends Operator {
    @Override
    public int priority() {
        return 2;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        return new Operand(operandOne.getValue() / operandTwo.getValue());
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/SubtractOperator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class SubtractOperator extends Operator {
    @Override
    public int priority() {
        return 1;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        return new Operand(operandOne.getValue() - operandTwo.getValue());
    }
}

~~~

</details>



<details>
    <summary>./calculator/operators/MultiplyOperator.java</summary>

~~~java
package calculator.operators;

import calculator.evaluator.Operand;

public class MultiplyOperator extends Operator {
    @Override
    public int priority() {
        return 2;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        return new Operand(operandOne.getValue() * operandTwo.getValue());
    }
}
~~~

</details>



<details>
    <summary>./calculator/evaluator/Evaluator.java</summary>

~~~java
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

~~~

</details>



<details>
    <summary>./calculator/evaluator/EvaluatorUI.java</summary>

~~~java
package calculator.evaluator;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;

public class EvaluatorUI extends JFrame implements ActionListener {

     private JTextField expressionTextField = new JTextField();
     private JPanel buttonPanel = new JPanel();
     private final Evaluator calculator = new Evaluator();

     // total of 20 buttons on the calculator,
     // numbered from left to right, top to bottom
     // bText[] array contains the text for corresponding buttons
     private static final String[] buttonText = {
         "7", "8", "9", "+",
         "4", "5", "6", "-",
         "1", "2", "3", "*",
         "(", "0", ")", "/",
         "C", "CE", "=", "^"
     };

     /**
      * C  is for clear, clears entire expression
      * CE is for clear expression, clears last entry up until the last operator.
      */

     public static void main(String argv[]) {
         new EvaluatorUI();
     }

     public EvaluatorUI() {
         setLayout(new BorderLayout());
         this.expressionTextField.setPreferredSize(new Dimension(600, 50));
         this.expressionTextField.setFont(new Font("Courier", Font.BOLD, 24));
         this.expressionTextField.setHorizontalAlignment(JTextField.CENTER);

         add(expressionTextField, BorderLayout.NORTH);
         expressionTextField.setEditable(false);

         add(buttonPanel, BorderLayout.CENTER);
         buttonPanel.setLayout(new GridLayout(5, 4));

         //create 20 buttons with corresponding text in bText[] array
         JButton jb;
         for (String s : EvaluatorUI.buttonText) {
             jb = new JButton(s);
             jb.setFont(new Font("Courier", Font.BOLD, 24));
             jb.addActionListener(this);
             this.buttonPanel.add(jb);
         }

         setTitle("Calculator");
         setSize(400, 400);
         setLocationByPlatform(true);
         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         setVisible(true);
     }

     /**
      * This function is called anytime a button is pressed
      * on our Calculator GUI.
      * @param actionEventObject Event object generated when a
      *                          button is pressed.
      */
     public void actionPerformed(ActionEvent actionEventObject) {
         String expression;
         String buttonInput = actionEventObject.getActionCommand();

         switch (buttonInput) {
             case "=":
                 expression = expressionTextField.getText();
                 try {
                     String result = String.valueOf(calculator.evaluateExpression(expression));
                     expressionTextField.setText(result);
                 } catch (InvalidTokenException e) {
                     System.err.println("Invalid token in expression: " + expression);
                 }
                 break;
             case "CE":
                 String a = expressionTextField.getText();
                 // Remove the last character
                 if (!a.isEmpty()) expressionTextField.setText(a.substring(0, a.length() - 1));
                 break;
             case "C":
                 expressionTextField.setText("");
                 break;
             default:
                 expression = expressionTextField.getText();
                 // Concatenate input with current expression
                 expressionTextField.setText(expression + buttonInput);
         }
     }
 }
~~~

</details>




## Unit Tests Results



~~~bash

Thanks for using JUnit! Support its development at https://junit.org/sponsoring

╷
├─ JUnit Jupiter ✔
│  ├─ Multiplication Test ✔
│  │  ├─ simpleMultiplicationTest() ✔
│  │  ├─ simpleMultiplicationTestNegativeOperand() ✔
│  │  ├─ multiplicationPriorityTest() ✔
│  │  ├─ simpleMultiplicationTestReversedOperands() ✔
│  │  └─ simpleMultiplicationTestNegativeOperandReversed() ✔
│  ├─ OperatorTest ✔
│  │  ├─ getOperatorReturnTypeTest(String, Class) ✔
│  │  │  ├─ [1] +, class calculator.operators.AddOperator ✔
│  │  │  ├─ [2] -, class calculator.operators.SubtractOperator ✔
│  │  │  ├─ [3] /, class calculator.operators.DivideOperator ✔
│  │  │  ├─ [4] *, class calculator.operators.MultiplyOperator ✔
│  │  │  └─ [5] ^, class calculator.operators.PowerOperator ✔
│  │  └─ operatorCheckTest(String, Boolean) ✔
│  │     ├─ [1] +, true ✔
│  │     ├─ [2] -, true ✔
│  │     ├─ [3] *, true ✔
│  │     ├─ [4] ^, true ✔
│  │     ├─ [5] /, true ✔
│  │     ├─ [6] 156, false ✔
│  │     ├─ [7] 156., false ✔
│  │     ├─ [8] X, false ✔
│  │     └─ [9] **, false ✔
│  ├─ Division Test ✔
│  │  ├─ simpleDivisionTestNegativeResult() ✔
│  │  ├─ simpleDivisionTest() ✔
│  │  ├─ divisionPriorityTest() ✔
│  │  ├─ simpleDivisionTestReversedOperands() ✔
│  │  └─ simpleDivisionTestEvenlyDivisible() ✔
│  ├─ Addition Test ✔
│  │  ├─ simpleAdditionTestWithNegativeOperand() ✔
│  │  ├─ simpleAdditionTestReverseOperands() ✔
│  │  ├─ simpleAdditionTest() ✔
│  │  ├─ additionPriorityTest() ✔
│  │  └─ simpleAdditionTestWithNegativeOperandsReversed() ✔
│  ├─ Power Test ✔
│  │  ├─ simplePowerTest() ✔
│  │  ├─ simplePowerTestNegativeBase() ✔
│  │  └─ powerPriorityTest() ✔
│  ├─ Evaluator Test ✔
│  │  ├─ mediumExpressionPowerWithParensTest() ✔
│  │  ├─ basicExpressionParensOnRightTest() ✔
│  │  ├─ invalidOperatorExpressionProducesInvalidTokenExceptionTest() ✔
│  │  ├─ basicExpressionDivisionNumeratorLessThanDenominatorTest() ✔
│  │  ├─ mediumExpressionParensAsSubExpressionTest() ✔
│  │  ├─ veryDifficultExpressionMixtureOfOperatorsNestedParensTest() ✔
│  │  ├─ mediumExpressionParensAndOperatorsTest() ✔
│  │  ├─ difficultExpressionMixtureOfOperatorsTest() ✔
│  │  ├─ mediumExpressionPowerTest() ✔
│  │  ├─ mediumExpressionPowerWithMultipleOperators() ✔
│  │  ├─ mediumExpressionWithMultipleParensTest() ✔
│  │  ├─ basicExpressionMultipleOperatorTest() ✔
│  │  ├─ basicExpressionSingleOperatorTest() ✔
│  │  └─ basicExpressionParensOnLeftTest() ✔
│  ├─ Subtraction Test ✔
│  │  ├─ simpleSubtractionTestNegativeOperands() ✔
│  │  ├─ simpleSubtractionTestNegativeOperandsReversed() ✔
│  │  ├─ simpleSubtractionTestReversedOperands() ✔
│  │  ├─ simpleSubtractionTest() ✔
│  │  └─ subtractionPriorityTest() ✔
│  └─ Operand Test ✔
│     ├─ getValueTest() ✔
│     ├─ operandCheckTest(String, boolean) ✔
│     │  ├─ [1] 13, true ✔
│     │  ├─ [2] c, false ✔
│     │  ├─ [3] *, false ✔
│     │  ├─ [4] 430., false ✔
│     │  ├─ [5] 430.456, false ✔
│     │  └─ [6] 343324fd, false ✔
│     └─ getValueTypeTest() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 102 ms
[        14 containers found      ]
[         0 containers skipped    ]
[        14 containers started    ]
[         0 containers aborted    ]
[        14 containers successful ]
[         0 containers failed     ]
[        59 tests found           ]
[         0 tests skipped         ]
[        59 tests started         ]
[         0 tests aborted         ]
[        59 tests successful      ]
[         0 tests failed          ]


~~~
    
