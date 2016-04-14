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
    private int numRows;
    private int numCol;

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
        this.numRows = in.nextInt();
        this.numCol = in.nextInt();
        this.board = new char [numRows][numCol];
        in.nextLine();
        for(int row = 0; row < numRows; row ++){
            String line = in.nextLine();
            String [] tokens = line.split(" ");
            for(int col = 0; col < numCol; col ++){
                String x = tokens[col];
                char y = x.charAt(0);
                board[row][col] = y;
            }
        }
        in.close();
    }


    public void initiate(String file) throws FileNotFoundException{
        Scanner in = new Scanner(new File(file));
        while(in.hasNext()){
            String line = in.nextLine();
            String [] tokens = line.split(" ");

            //TODO
        }
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
        String result = "  ";
        for(Integer i = 0; i < numCol; i++){
            if(i >= 10){
                i = i-10;
                result += i.toString() + " ";
            }
            else{
                result += i.toString() + " ";
            }
        }
        result += "\n";
        for(int i = 0; i < numCol*2; i++){
            if(i == 0){
                result += "  ";
            }
            else{
                result += "-";
            }
        }
        result += "\n";
        for(Integer row = 0; row < numRows; row ++){
            if (row >= 10){
                row -= 10;
                result += row.toString();
                row += 10;
            }
            else{
                result += row.toString();
            }
            result += "|";
            for(int col = 0; col < numCol; col++){
                result += board[row][col] + " ";
            }
            result += "\n";
        }


        return result;
    }
}
