import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Created by Trevor Powers and Brian Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);

        if (args.length == 1) {
            String filename = args[0];
            board game = new board(filename);
        }
        else if (args.length == 2) {
            String filename = args[0];
            String initial = args[1];

            board game = new board(filename);
            game.initiate(initial);
        }
        else {
            System.out.println("Usage java LasersPTUI safe-file [input]");
        }
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
