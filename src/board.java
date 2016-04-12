import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Author: Trevor Powers
 *
 * This class creates the board for the laser project, and provides methods to
 * interact with the board.
 */
public class board {
    private char [][] board;  //This 2d array is the board itself

    private final static char EMPTY = '.';  //used for tiles that currently have nothing on them
    private final static char LASER = 'L';  // used for tiles that are holding a laser
    private final static char L_BEAM = '*';  // used for tiles that are covered by a laser beam
    private final static char xPillar = 'X';  // used for tiles that are pillars that can have an unlimited amount of lasers

    /**
     * Creates the 'board' by populating the 2d Array with input supplied by
     * a textile
     * @param filename  a file that contains the information to creat a board of
     *                  the vault
     * @throws FileNotFoundException
     */
    public board(String filename) throws FileNotFoundException{
        Scanner in = new Scanner(new File(filename));
        int dim = in.nextInt();
        this.board = new char [dim][dim];
        for(int row = 0; row < dim; row ++){
            String line = in.nextLine();
            String [] tokens = line.split(" ");
            for(int col = 0; col < dim; col ++){
                String x = tokens[col];
                char y = x.charAt(0);
                board[row][col] = y;
            }
        }
        in.close();
    }

    /**
     * This method will verify whether or not the board is valid or not
     *
     * @return  True if the board is valid, and false if not
     */
    public boolean verify(){
        //TODO
        return false;
    }

    public String toString(){
        String result = " ";
        for(Integer i = 0; i < board.length; i++){
            result += i.toString();
        }
        return result;
    }
}
