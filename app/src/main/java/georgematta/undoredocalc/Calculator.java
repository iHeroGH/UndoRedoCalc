package georgematta.undoredocalc;

/**
 * This is the Calculator class, which consists of doubles for the new input and the total result
 * and a String for the operator
 *
 * This is the most basic class in the project, since it is purely used to make calculations and
 * store very basic information
 *
 * There is also a static method to return the opposite operator based on a given operator
 *
 * @author George Matta
 * @version 1.0
 */
public class Calculator {

    /**
     * A double denoting the input number (as the user inputs new numbers, this variable changes
     */
    private double num;
    /**
     * A String denoting the operator the user has selected (+-/*)
     */
    private String operator;

    /**
     * A double denoting the final result. This variable is never changed by a setter method, it
     * changes as the calculate() method is called
     */
    private double result = 0;

    // GETTERS

    /**
     * A method to return the num variable
     *
     * @return A double. This Calculator's num
     */
    public double getNum(){
        return this.num;
    }

    /**
     * A method to return the total result
     *
     * @return A double. This Calculator's result
     */
    public double getResult(){
        return this.result;
    }

    /**
     * A method to return the operator
     *
     * @return A Sring. This Calculator's operator
     */
    public String getOperator(){
        return this.operator;
    }

    // USER-SET VARS

    /**
     * A variable to change this Calculator's num
     *
     * @param num The double denoting what number we want to change the num variable to
     */
    public void setNum(double num){
        this.num = num;
    }

    /**
     * A variable to change this Calculator's operator
     *
     * @param op The String denoting what operator we want to change this operator to
     */
    public void setOperator(String op){
        this.operator = op;
    }
    /**
     * A method to reset the operator to null
     */
    public void resetOperator(){
        setOperator(null);
    }

    // Apply the operation onto the result and the number

    /**
     * A method to apply the set operation to the result
     *
     * Runs num (operator) result, and sets result to that new number
     */
    public void calculate(){
        // The operator can be +-*/
        switch (operator){
            case "+":
                result += num;
                break;
            case "-":
                result -= num;
                break;
            case "*":
                result *= num;
                break;
            case "/":
                result /= num;
                break;
            default:
                result = result;
                break;
        }
    }

    /**
     * A method to check if we have an operator (if operator is not null)
     * @return A boolean denoting the validity of the operator
     */
    public boolean validOperator(){
        return operator != null;
    }

    /**
     * A static method to return the inverse operator based on a given operator
     * For + it would be - and vice versa
     * For * it would be / and vice versa
     *
     * @param operator The input operator String
     * @return The inverse operator String
     */
    public static String oppositeOperator(String operator){
        switch (operator){
            case "+":
                return "-";
            case "-":
                return "+";
            case "*":
                return "/";
            case "/":
                return "*";
            default:
                return "";
        }
    }

}
