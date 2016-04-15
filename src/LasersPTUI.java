import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Created by Trevor Powers and Brian Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    private board game;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);

        String filename = in.nextLine();

        board game = new board(filename);
        System.out.println(game);
    }

    public void help(){
        System.out.println("a|add r c: Add laser to (r,c)");
        System.out.println("d|display: Display safe");
        System.out.println("h|help: Print this help message");
        System.out.println("q|quit: Exit program");
        System.out.println("r|remove r c: Remove laser from (r,c)");
        System.out.println("v|verify: Verify safe correctness");
    }
}
