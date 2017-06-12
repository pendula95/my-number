import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by lbulic on 6/12/17.
 */
public class Node implements Comparable<Node> {

    // String representation of the expression
    private String eq;
    // value of the expression
    private double value;
    // FINAL_VALUE - cost ABS
    private double cost;
    // array of operands tests expression
    private List<String> numbers;
    // array of operators tests expression
    private List<String> operators;

    public Node(String eq, List<String> numbers, List<String> operators) {
        this.eq = eq;
        this.value = EvaluateString.evaluate(eq);
        this.cost = Math.abs(Main.FINAL_VALUE - value);
        this.numbers = new Vector<>(numbers);
        this.operators = new Vector<>(operators);
    }

    public void mutationOperator() {
        //randomly selects one operator and randomly chooses new one to replace it
        Random random = new Random();
        int indexReplace = random.nextInt(operators.size());
        int indexNew = random.nextInt(Main.OPERATORS.length);
        //if the new operator is same as the old one, select one randomly again
        if(operators.get(indexReplace).equals(Main.OPERATORS[indexNew])) {
            if(indexNew > 0) {
                indexNew--;
            }else {
                indexNew = 1;
            }
        }
        operators.set(indexReplace, Main.OPERATORS[indexNew]);
        buildEq();
        calculateCost();
    }


    public void mutationOperandum() {
        Random random = new Random();
        //randomly swaps places of 2 operands tests expression
        int rnd1 = random.nextInt(numbers.size());
        String tmp1 = numbers.get(rnd1);
        int rnd2 = random.nextInt(numbers.size());
        if(rnd1 == rnd2) {
            if(rnd2 > 0) {
                rnd2--;
            }else{
                rnd2 = 1;
            }
        }
        String tmp2 = numbers.get(rnd2);
        numbers.set(rnd1, tmp2);
        numbers.set(rnd2, tmp1);
        buildEq();
        calculateCost();
    }

    //build mutade expression
    private void buildEq() {
        StringBuilder sb = new StringBuilder();
        Vector<String> numbersTmp = new Vector<>(numbers);
        Vector<String> operatorsTmp = new Vector<>(operators);
        for (int i = 0; i < eq.length(); i++) {
            //if ( or ) just copy it, no change
            if (eq.charAt(i) == '(' || eq.charAt(i) == ')') {
                sb.append(eq.charAt(i));
                //if operand than copy the operand from vector and remove it from the copy vector
            } else if (eq.charAt(i) == '+' || eq.charAt(i) == '-' || eq.charAt(i) == '*' || eq.charAt(i) == '/') {
                sb.append(operatorsTmp.get(0));
                operatorsTmp.remove(0);
                //if space than just copy it
            } else if (eq.charAt(i) == ' ') {
                sb.append(" ");
                //it is a operand copy it from the copy vector and remove it
            } else if (sb.length() == 0) {
                sb.append(numbersTmp.get(0));
                numbersTmp.remove(0);
                //we are going character by character through the expression, if number is not a single digit ignore other digits;
            } else if (!Character.isDigit(eq.charAt(i - 1))) {
                sb.append(numbersTmp.get(0));
                numbersTmp.remove(0);
            }
        }

        eq = sb.toString();
    }

    //resets value and cost of node
    private void calculateCost() {
        value = EvaluateString.evaluate(eq);
        cost = Math.abs(Main.FINAL_VALUE - value);
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.cost, o.getCost());
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
