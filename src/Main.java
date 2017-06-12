import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Main {

    private static final int POPULATION_SIZE = 50;
    private static final int OPERANDS_MUTATION_CHANCE = 6;
    static final String[] OPERATORS = {"+", "-", "*", "/"};
    private static final int GROUP_CHANCE = 3;
    static int FINAL_VALUE;
    public static Random random = new Random();

    public static void main(String args[]) throws IOException {

        long startTime = System.nanoTime();

        for (int p = 2; p < 101; p++) {

            int generation = 0;
            List<Node> nodes = new Vector<>();
            Node best;

            BufferedReader reader = null;
            InputStream in = Main.class.getResourceAsStream(File.separator + "tests" + File.separator + p + ".txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            List<String> numbers = new Vector<>();
            //read first 6 lines and adds them to numbers
            for (int i = 0; i < 6; i++) {
                line = reader.readLine();
                numbers.add(line);
            }
            //last line is the final result
            FINAL_VALUE = Integer.parseInt(reader.readLine());


            //generates initial population
            for (int i = 0; i < POPULATION_SIZE; i++) {
                nodes.add(createEquation(numbers));
            }

            //process of mutation
            while (true) {
                generation++;
                Collections.sort(nodes); //sorts node by costa value tests Node
                best = nodes.get(0);

                //if cost is 0 that means we found the FINAL_VALUE
                if (best.getCost() == 0.0) {
                    System.out.println("Found solution for input " + p + " tests generation: " + generation);
                    System.out.println(best.getEq() + " = " + best.getValue());
                    System.out.println("##########################################################");
                    break;
                }

                //decides which mutation to do, on operator or operand
                for (int i = 0; i < POPULATION_SIZE; i++) {
                    if (random.nextInt(11) > OPERANDS_MUTATION_CHANCE) {
                        nodes.get(i).mutationOperator();
                    } else {
                        nodes.get(i).mutationOperandum();
                    }
                }

                //replaces last 50 noces tests population with newly generated nodes
                for (int i = POPULATION_SIZE / 2; i < POPULATION_SIZE; i++) {
                    nodes.set(i, createEquation(numbers));
                }
            }
        }
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1000000000);
    }

    //creates randomly generated node, mathematical expression from given numbers
    private static Node createEquation(List<String> numbers) {
        //shuffles the order of numbers
        Collections.shuffle(numbers);
        StringBuilder sb = new StringBuilder();
        List<String> usedNumbers = new Vector<>();
        List<String> operatorsUsed = new Vector<>();
        //number of operands tests the expression
        int size = 2 + random.nextInt(5);
        //number of opened (
        int opened = 0;
        //should ( be generated
        boolean open = false;
        //should ) be generated
        boolean close = false;

        for (String number : numbers) {
            //decides if ( should be added to the expression
            if (random.nextInt(10) > GROUP_CHANCE) {
                open = true;
            }
            //adds operand to the list that will be passed to the node
            usedNumbers.add(number);
            //adds ) if all cases are satisfied, there is alread an open (, expression has more than 2 operands, current number of ( is > 0
            if (usedNumbers.size() > 2 && opened > 0 && random.nextInt(10) > GROUP_CHANCE) {
                close = true;
            }
            //chooses random operator for expression
            String operatorUsed = OPERATORS[random.nextInt(OPERATORS.length)];
            //adds operator to the list of operators that will be passed to the node
            operatorsUsed.add(operatorUsed);
            if (open) {
                sb.append("( " + number + " " + operatorUsed + " ");
                opened++;
                open = false;
            } else if (close) {
                sb.append(number + " )" + " " + operatorUsed + " ");
                opened--;
                close = false;
            } else {
                sb.append(number + " " + operatorUsed + " ");
            }
            if (size-- == 0) {
                break;
            }
        }
        String eq = sb.toString();
        //after the final string is generated we are left with (space + operator + space) on the end that need to be removed
        eq = eq.substring(0, eq.length() - 3);
        //closes any opened ) that were left
        for (int i = 0; i < opened; i++) {
            eq += " )";
        }
        return new Node(eq, usedNumbers, operatorsUsed);
    }
}
