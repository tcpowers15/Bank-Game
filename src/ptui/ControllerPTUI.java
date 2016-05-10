package ptui;

import model.LasersModel;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author Brian Powers & Trevor Powers
 */
public class ControllerPTUI {
    /**
     * The UI's connection to the model
     */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     *
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     *
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {
        // TODO;
        Scanner in = new Scanner(System.in);
        boolean playing = true;

        if (inputFile != null) {
            while (playing) {
                System.out.print("> ");
                String input = in.nextLine();


                String command = input.substring(0, 1);
                String[] tokens = input.split(" ");
                if (tokens.length == 0) {
                    int g = 0;
                } else if (command.equals("a")) {
                    if (tokens.length == 3) {
                        int row = Integer.parseInt(tokens[1]);
                        int col = Integer.parseInt(tokens[2]);
                        this.model.add(row, col);
                    } else {
                        System.out.println("Incorrect coordinates");
                    }
                } else if (command.equals("d")) {
                    System.out.println(this.model);
                } else if (command.equals("h")) {
                    System.out.println("a|add r c: Add laser to (r,c)");
                    System.out.println("d|display: Display safe");
                    System.out.println("h|help: Print this help message");
                    System.out.println("q|quit: Exit program");
                    System.out.println("r|remove r c: Remove laser from (r,c)");
                    System.out.println("v|verify: Verify safe correctness");
                } else if (command.equals("q")) {
                    System.exit(0);
                } else if (command.equals("r")) {
                    if (tokens.length == 3) {
                        int row = Integer.parseInt(tokens[1]);
                        int col = Integer.parseInt(tokens[2]);
                        this.model.remove(row, col);
                    } else {
                        System.out.println("Incorrect coordinates");
                    }
                } else if (command.equals("v")) {
                    this.model.verify();
                } else {
                    System.out.println("Unrecognized command: " + input);
                }
            }
        }
    }
}
