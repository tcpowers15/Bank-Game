import javafx.application.Application;

import java.io.FileNotFoundException;

import gui.LasersGUI;
import ptui.ControllerPTUI;
import ptui.LasersPTUI;

/**
 * This is the main class for the Lasers project.  It is used to run either
 * the plain text UI (LasersPTUI) or the JavaFX graphical UI (LasersGUI).
 * It is run on the command line as follows:<br>
 * <br>
 * 1. Plain text UI mode: <br>
 *     java Lasers ptui safe-file [input]<br>
 * <br>
 * Here, the input file of commands from the file is optional<br>
 * <br>
 * 2. JavaFX GUI mode:<br>
 *     java Lasers gui safe-file<br>
 * <br>
 * @author James Heliotis @ RIT CS
 * @author Sean Strout @ RIT CS
 */
public class Lasers {
    /** The 2 modes are GUI and PTUI */
    public enum UIMode { GUI, PTUI, UNKNOWN };

    /**
     * Displays the usage message and exits the program.
     */
    private static void usage() {
        System.err.println("Usage: java Lasers (gui | ptui) safe-file [input]");
        System.exit(-1);
    }

    /**
     * The main method reads the command line arguments to determine the
     * UI to run and the input settings.  It then either launches
     * the GUI, or hands control over to the ControllerPTUI's run method.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // get the arguments from the command line
        UIMode mode = UIMode.UNKNOWN;
        String safeFile = null;
        String inputFile = null;
        switch (args.length) {
            case 3:
                inputFile = args[2];
            case 2:
                safeFile = args[1];
                try {
                    mode = UIMode.valueOf(args[0].toUpperCase());
                } catch( IllegalArgumentException iae ) {
                    usage();
                }
                break;
            default:
                usage();
        }

        // now either launch the GUI or create the PTUI and pass control to it
        try {
            switch (mode) {
                case GUI:
                    // this launches the GUI and passes in the safe file.
                    Application.launch(LasersGUI.class, safeFile);
                    break;
                case PTUI:
                    // create the "view" first
                    LasersPTUI ptui = new LasersPTUI(safeFile);
                    // now create the "controller"
                    ControllerPTUI ctrlr = new ControllerPTUI(ptui.getModel());
                    // now pass control to the run method of the controller
                    ctrlr.run(inputFile);
                    break;
                case UNKNOWN:
                    usage();
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
    }
}