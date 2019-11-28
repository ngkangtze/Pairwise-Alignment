import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.stream.Stream;

public class PairwiseAlignment {

    public static void main(String[] args)throws Exception {
        String currentDirectory = System.getProperty("user.dir");
        String schemePath =  currentDirectory + "/res/scheme.txt";
        String sequencesPath =  currentDirectory + "/res/sequences.txt";
        String fileContent;

        // Reading input from scheme.txt into 2D int array scheme
        File file = new File(schemePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int[][] scheme = new int[5][5];
        String s;
        br.readLine();
        for (int i = 0; i < 5; i++) {
            s = br.readLine();
            scheme[i] = Stream.of(Arrays.copyOfRange(s.split(" "), 1, 6))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            // System.out.println(Arrays.toString(scheme[i]));
        }

        // Reading input from sequences into sequences to be processed.
        String x, y;
        file = new File(sequencesPath);
        br = new BufferedReader(new FileReader(file));
        br.readLine();
        x = br.readLine();
        y = br.readLine();
        // System.out.println(x + "\n" + y);

        // Producing output
        fileContent = pairwiseAlignment(x, y, scheme);
        BufferedWriter writer = new BufferedWriter(new FileWriter(currentDirectory + "/res/output.txt"));
        writer.write(fileContent);
        writer.close();
    }

    /**
     * Converts a string sequence into an integer array.
     * Each integer in the array represents a different symbol (A, G, C, T).
     * @param s Sequence to be converted into integer array.
     * @return An array of integers representing each symbol in the array.
     */
    private static int[] sequenceToNumArray(String s) {
        int[] numArray = new int[s.length()];
        String[] strArray = s.split("");
        Symbol[] symbols = Symbol.values();
        for (int i = 0; i < s.length(); i++) {
            for (Symbol symbol : symbols) {
                if (strArray[i].equals(symbol.toString())) {
                    numArray[i] = symbol.ordinal();
                }
            }
        }
        return numArray;
    }

    /**
     * The main method of the program. Uses dynamic programming to generate the maximum scoring
     * pairwise sequence alignment of two sequences.
     * @param x First sequence to be aligned.
     * @param y Second sequence to be aligned.
     * @param scheme Scoring matrix to determine the maximum scoring matching.
     * @return Result of the program to be saved into a text file.
     */
    private static String pairwiseAlignment(String x, String y, int[][] scheme) {
        int xLength = x.length();
        int yLength = y.length();
        int[][] dp = new int[xLength + 1][yLength + 1];
        char[][] trace = new char[xLength + 1][yLength + 1];
        int[] xToValues = sequenceToNumArray(x);
        int[] yToValues = sequenceToNumArray(y);

        // Initialise all starting values to the sum of the gap penalties.
        dp[0][0] = scheme[4][4];
        for (int i = 1; i < xLength + 1; i++) {
            dp[i][0] = dp[i - 1][0] + scheme[xToValues[i - 1]][4];
            trace[i][0] = 'T';
        }
        for (int i = 1; i < yLength + 1; i++) {
            dp[0][i] = dp[0][i - 1] + scheme[4][yToValues[i - 1]];
            trace[0][i] = 'L';
        }

        // Using the recursive algorithm to obtain the score for each entry in the dp matrix.
        // Also keeps track of which previous entry the current entry came from.
        for (int i = 1; i < xLength + 1; i++) {
            for (int j = 1; j < yLength + 1; j++) {
                // Taking the value from the diagonal;
                // Match the i - 1th character from x and the j - 1th character from y.
                int diagonalValue = dp[i - 1][j - 1] + scheme[xToValues[i - 1]][yToValues[j - 1]];

                // Taking the value from the left; j - 1th character from y doesnt match anything.
                int leftValue = dp[i][j - 1] + scheme[yToValues[j - 1]][4];

                // Taking the value from the top; i - 1th character from x doesnt match anything.
                int topValue = dp[i - 1][j] + scheme[4][xToValues[i - 1]];

                dp[i][j] = Math.max(diagonalValue, Math.max(leftValue, topValue));

                if (diagonalValue >= leftValue && diagonalValue >= topValue) {
                    trace[i][j] = 'D';
                } else if (leftValue >= topValue) {
                    trace[i][j] = 'L';
                } else {
                    trace[i][j] = 'T';
                }
            }
        }

        // Arrays.stream(dp).map(Arrays::toString).forEach(System.out::println);
        // Arrays.stream(trace).map(Arrays::toString).forEach(System.out::println);

        // Based on the path taken, generate the new x and y strings.
        String tracePath = "";
        char curr;
        int i = xLength;
        int j = yLength;
        String xMod = "";
        String yMod = "";
        while (i != 0 || j != 0) {
            curr = trace[i][j];
            tracePath = curr + tracePath;
            if (curr == 'D') {
                xMod = x.charAt(i - 1) + xMod;
                yMod = y.charAt(j - 1) + yMod;
                i -= 1;
                j -= 1;
            } else if (curr == 'L') {
                xMod = '_' + xMod;
                yMod = y.charAt(j - 1) + yMod;
                j -= 1;
            } else {
                xMod = x.charAt(i - 1) + xMod;
                yMod = '_' + yMod;
                i -= 1;
            }
        }
        // System.out.println(xMod);
        // System.out.println(yMod);

        String fileContent = ">alignment\n";
        fileContent += xMod + "\n";
        fileContent += yMod + "\n";
        fileContent += ">score\n";
        fileContent += dp[xLength][yLength];
        // System.out.println(fileContent);

        return fileContent;
    }
}
