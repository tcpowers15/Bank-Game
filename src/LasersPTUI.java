import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Created by Trevor Powers and Brian Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    private board game;

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length == 1) {
            String filename = args[0];
            board game = new board(filename);
            play(game);
        }
        else if (args.length == 2) {
            String filename = args[0];
            String initial = args[1];

            board game = new board(filename);
            game.initiate(initial);
            play(game);
        }
        else {
            System.out.println("Usage java LasersPTUI safe-file [input]");
        }
    }

    /**
     * This method actually simulates the game of placing the lasers on and off
     * the board
     *
     * @param game a board
     */
    public static void play(board game){
        Scanner in = new Scanner(System.in);
        boolean playing = true;

        while (playing){
            System.out.println(game);
            String input = in.nextLine();

            String command = input.substring(0,1);

            if(command.equals("a")){
                //TODO
                //Call boards add method
            }
            else if(command.equals("d")){
                //TODO
                //Call boards display method
            }
            else if(command.equals("h")){
                //TODO
                //Call help
            }
            else if(command.equals("q")){
                //TODO
                //quit
            }
            else if(command.equals("r")){
                //TODO
                //call boards remove method
            }
            else if(command.equals("v")){
                //TODO
                //Call boards verify
            }
            else{
                System.out.println("Unrecognized command: " + input);
            }

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
