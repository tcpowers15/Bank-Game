import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Created by Trevor Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);

        String filename = in.nextLine();

        board game = new board(filename);
        System.out.println(game);
    }
}
