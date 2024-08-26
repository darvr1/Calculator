javac -d target .\calculator\evaluator\Operand.java
javac -d target .\calculator\operators\*.java
javac -d target .\calculator\evaluator\*.java
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\operator\*.java
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\operand\*.java
javac -d target --class-path "./;.\lib\junit-platform-console-standalone-1.9.3.jar" .\tests\*.java

java -jar .\lib\junit-platform-console-standalone-1.9.3.jar -cp target --scan-classpath