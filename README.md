# CSC 413 Project One - Calculator

## Student Information

### Student Name  : Name here

### Student ID    : ID here

### Student Email : Email here

## Requirements

- Implement Operand Class
- Implement Operator Abtsraction
- Implement Operator Sub classes
    - Implement AddOperator class
    - Implement Subractperator class
    - Implement DivideOperator class
    - Implement MultiplyOperator class
    - Implement PowerOperator class
- Implement the Evaluator Class
- Implement the EvluatorUI Class
- Write Project One documentation
    - Follow the guidelines in the Documentation Guidelines.docx file
    - Write your documentation in the A1DocBase.docx file.
    - Convert the docx to a PDF and rename it to the following:
        `lastname_firstname.pdf`
- Commit all code to repository by the given due date posted on Canvas. Sample commads below


## Compiling Project from command line

### Windows
```
javac -d target .\calculator\evaluator\Operand.java
javac -d target .\calculator\operators\*.java
javac -d target .\calculator\evaluator\*.java
```

### Linux/MacOS
```
javac -d target ./calculator/evaluator/Operand.java
javac -d target ./calculator/operators/*.java
javac -d target ./calculator/evaluator/*.java
```

## Run Project One

### Windows/Linux/MacOS
```
java -cp target calculator.evaluator.Evaluator
```

## Running Unit Tests for Project One

### Compile Test

### Windows
```
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\operator\*.java
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\operand\*.java
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\*.java

```

### Linux/MacOS
```
javac -d target --class-path "./:./lib/junit-platform-console-standalone-1.9.3.jar" ./tests/operator/*.java
javac -d target --class-path "./:./lib/junit-platform-console-standalone-1.9.3.jar" ./tests/operand/*.java
javac -d target --class-path "./:./lib/junit-platform-console-standalone-1.9.3.jar" ./tests/*.java
```

## Run Unit Test

### Windows
```
java -jar .\lib\junit-platform-console-standalone-1.9.3.jar -cp target --scan-classpath
```

### Linux
```
java -jar ./lib/junit-platform-console-standalone-1.9.3.jar -cp target --scan-classpath
```

## Submit Assignment
- Save all work
- Add all files to staging
    ```
    git add .
    ```
- Commit work to main branch
    ```
    git commit -m "message goes here"
    ```
- Push work to repository
    ```
    git push origin main
    ```
