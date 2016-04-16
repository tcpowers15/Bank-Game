import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Created by Trevor Powers and Brian Powers on 4/8/2016.
 *
 */
public class LasersPTUI {

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length == 1) {
            String filename = args[0];
            board game = new board(filename);
            play(game, true);
        }
        else if (args.length == 2) {
            String filename = args[0];
            String initial = args[1];

            board game = new board(filename);
            boolean play = game.initiate(initial);
            play(game, play);
        }
        else {
            System.out.println("Usage: java LasersPTUI safe-file [input]");
        }
    }

    /**
     * This method actually simulates the game of placing the lasers on and off
     * the board
     *
     * @param game a board
     */
    public static void play(board game, boolean play){
        Scanner in = new Scanner(System.in);
        boolean playing = play;
        System.out.print(game);

        while (playing){
            System.out.print("> ");
            String input = in.nextLine();


            String command = input.substring(0,1);
            String[] tokens = input.split(" ");

            if(command.equals("a")){
                if(tokens.length == 3){
                    int row = Integer.parseInt(tokens[1]);
                    int col = Integer.parseInt(tokens[2]);
                    game.add(row, col);
                    System.out.println(game);
                }
                else{
                    System.out.println("Incorrect Coordinates");
                }
            }
            else if(command.equals("d")){
                game.display();
            }
            else if(command.equals("h")){
                game.help();
            }
            else if(command.equals("q")){
                playing = game.quit(playing);
            }
            else if(command.equals("r")){
                if(tokens.length == 3){
                    int row = Integer.parseInt(tokens[1]);
                    int col = Integer.parseInt(tokens[2]);
                    game.remove(row,col);
                    System.out.println(game);
                }
                else{
                    System.out.println("Incorrect Coordinates");
                }
            }
            else if(command.equals("v")){
                game.verify();
                System.out.print(game);
            }
            else{
                System.out.println("Unrecognized command: " + input);
            }
        }
    }
}
