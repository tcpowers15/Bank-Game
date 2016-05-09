package backtracking;

import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * Thie is the main class for a safe solver.  It runs the backtracking
 * algorithm and displays the solution, if one exists.
 *
 * @author Sean Strout @ RIT CS
 */
public class SafeSolver {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Usage: java SafeSolver safe.in debug");
        } else {
            // construct the initial configuration from the file
            Configuration init = new SafeConfig(args[0]);

            // create the backtracker with the debug flag
            boolean debug = args[1].equals("true");
            Backtracker bt = new Backtracker(debug);

            // start the clock
            double start = System.currentTimeMillis();

            // attempt to solve the puzzle
            Optional<Configuration> sol = bt.solve(init);

            // compute the elapsed time
            System.out.println("Elapsed time: " +
                    (System.currentTimeMillis() - start)/1000.0 + " seconds.");

            // indicate whether there was a solution, or not
            if (sol.isPresent()) {
                System.out.println("Solution:\n" + sol.get());
            } else {
                System.out.println("No solution!");
            }        }
    }
}
