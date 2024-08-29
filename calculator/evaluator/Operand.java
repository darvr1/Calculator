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
            throw new InvalidTokenException("Operands can only be an integer.");
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
