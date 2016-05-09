package backtracking;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Thie is the main class for a safe solver with a path.  It runs the
 * backtracking algorithm and displays the full path of the solution,
 * step by step, if one exists.
 *
 * @author Sean Strout @ RIT CS
 */
public class SafeSolverPath {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Usage: java SafeSolverPath safe.in debug");
        } else {
            // construct the initial configuration from the file
            Configuration init = new SafeConfig(args[0]);

            // create the backtracker with the debug flag
            boolean debug = args[1].equals("true");
            Backtracker bt = new Backtracker(debug);

            // start the clock
            double start = System.currentTimeMillis();

            // attempt to solve the puzzle
            List<Configuration> path = bt.solveWithPath(init);

            // compute the elapsed time
            System.out.println("Elapsed time: " +
                    (System.currentTimeMillis() - start)/1000.0 + " seconds.");

            // indicate whether there was a solution, or not
            if (path != null) {
                int step = 0;
                // loop through the configs from start to end and display them
                for (Configuration config : path) {
                    System.out.println("Step " + step + ":");
                    System.out.println(config);
                    ++step;
                }
            } else {
                System.out.println("No solution!");
            }        }
    }
}
