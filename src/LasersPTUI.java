import java.io.FileNotFoundException;

/**
 * Created by Trevor Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("My name is Trevor Powers");
        System.out.println("My name Brian Powers");
        System.out.println("Our Project account is p142-11h");

        String filename = "simpleSafe.txt";

        board game = new board(filename);
        System.out.println(game);
    }
}
