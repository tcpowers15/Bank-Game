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
    private final static char zero = '0'; //used for pillars that requires zero lasers
    private final static char one = '1'; //used for pillars that requires one laser
    private final static char two = '2'; //used for pillars that requires two lasers
    private final static char three = '3'; //used for pillars that requires three lasers
    private final static char four = '4'; //used for pillars that requires four lasers

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



    /**
     * This method takes in a text file, and uses the commands in it to populate
     * the board with the initial set up
     * @param file  the name of a .txt file
     * @throws FileNotFoundException
     */
    public boolean initiate(String file) throws FileNotFoundException{
        Scanner in = new Scanner(new File(file));
        boolean playing = true;
        while(in.hasNext()){
            String line = in.nextLine();
            String [] tokens = line.split(" ");

            String command = tokens[0];
            if(tokens.length == 3){
                int row = Integer.parseInt(tokens[1]);
                int col = Integer.parseInt(tokens[2]);

                if(command.substring(0,1).matches("a")){
                    add(row,col);
                }
                else if(command.substring(0,1).matches("r")){
                    remove(row,col);
                }
            }
            else{

                if(command.substring(0,1).matches("d")){
                    display();
                }
                else if(command.substring(0,1).matches("h")){
                    help();
                }
                else if(command.substring(0,1).matches("q")){
                    playing = quit(playing);
                    break;
                }
                else if(command.substring(0,1).matches("v")){
                    verify();
                }
            }
        }
        in.close();

        if(playing){
            return true;
        }
        else{
            return false;
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



    /**
     * This method adds a laser at the specified location
     * @param r the row number
     * @param c the column number
     */
    public void add(int r, int c) {
        if (board[r][c] == LASER || board[r][c] == xPillar || board[r][c] == zero || board[r][c] == one
                || board[r][c] == two || board[r][c] == three || board[r][c] == four) {
            System.out.println("Error adding laser at: (" + r + "," + c + ")");
        }
        else if(board[r][c] == EMPTY || board[r][c] == L_BEAM){
            board[r][c] = LASER;
            for(int i = 1; r+i < numRows;i++){
                if(board[r+i][c] == EMPTY || board[r+i][c] == L_BEAM){
                    board[r+i][c] = L_BEAM;
                }
                else{
                    break;
                }

            }
            for(int j = 1; r-j > 0; j--){
                if(board[r-j][c] == EMPTY || board[r-j][c] == L_BEAM){
                    board[r-j][c] = L_BEAM;
                }
                else{
                    break;
                }
            }
            for(int k = 1; c+k < numCol; k++){
                if(board[r][c+k] == EMPTY || board[r][c+k] == L_BEAM){
                    board[r][c+k] = L_BEAM;
                }
                else{
                    break;
                }
            }
            for(int l = 1; c-l > 0; l--){
                if(board[r][c-l] == EMPTY || board[r][c-l] == L_BEAM){
                    board[r][c-l] = L_BEAM;
                }
                else{
                    break;
                }
            }
        }
    }



    /**
     * This method removes a laser from a given position
     * @param r the row number
     * @param c the column number
     */
    public void remove(int r, int c){
        // make check for other lasers
        boolean hasLaser;
        if (board[r][c] == LASER){
            board[r][c] = EMPTY;
            hasLaser = false;
            for(int i = 1; r+i < numRows;i++){
                if(board[r+i][c] == L_BEAM && !hasLaser){
                    for(int m = 1; r+m < numRows; m++){
                        if(board[r+m][c] == LASER){
                            hasLaser = true;
                            break;
                        }
                        else{
                            hasLaser = false;
                        }
                    }
                }

                else{
                    break;
                }
            }
            for(int j = 1; r-j > 0; j--){
                if(board[r-j][c] == L_BEAM){

                }
                else{
                    break;
                }
            }
            for(int k = 1; c+k < numCol; k++){
                if(board[r][c+k] == L_BEAM){
                    board[r][c+k] = EMPTY;
                }
                else{
                    break;
                }
            }
            for(int l = 1; c-l > 0; l--){
                if(board[r][c-l] == L_BEAM){
                    board[r][c-l] = EMPTY;
                }
                else{
                    break;
                }
            }
        }
        else{
            System.out.println("Error removing laser at: (" + r + "," + c + ")");
        }
    }




    /**
     * This method prints the board
     */
    public void display(){
        String r = this.toString();
        System.out.println(r);
    }


    /**
     * This prints the help message
     */
    public void help(){
        System.out.println("a|add r c: Add laser to (r,c)");
        System.out.println("d|display: Display safe");
        System.out.println("h|help: Print this help message");
        System.out.println("q|quit: Exit program");
        System.out.println("r|remove r c: Remove laser from (r,c)");
        System.out.println("v|verify: Verify safe correctness");
    }


    /**
     * causes the program to quit
     */
    public boolean quit (boolean play){
        play = false;
        return play;
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
