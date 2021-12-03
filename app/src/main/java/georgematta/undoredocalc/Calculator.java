package georgematta.undoredocalc;

public class Calculator {

    private double num;
    private String operator;

    private double result = 0;

    // Getters
    public double getNum(){
        return this.num;
    }
    public double getResult(){
        return this.result;
    }
    public String getOperator(){
        return this.operator;
    }

    // User chosen variables
    public void setNum(double num){
        this.num = num;
    }
    public void setOperator(String op){
        this.operator = op;
    }

    // Apply the operation onto the result and the number
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

    // Check if we have an operator
    public boolean validOperator(){
        return operator != null;
    }

    // Find the opposite operator
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
