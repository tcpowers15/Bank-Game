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
            display();
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
    public void verify(){
        boolean populated = true;
        boolean isvalid = true;
        boolean filled = false;

        int orow = 0;
        int ocol = 0;

        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCol; col++){
                if(board[row][col] == EMPTY){
                    populated = false;
                    if(!filled){
                        orow = row;
                        ocol = col;
                        filled = true;
                    }
                }

            }
        }
        if(populated){
            boolean sbreak = false;
          for(int row = 0; row < numRows; row ++){
              for(int col = 0; col < numCol; col++){
                  char help = board[row][col];
                  if(help == LASER){
                      isvalid = laserVal(row, col);
                  }
                  else if(help == zero){
                      isvalid = zeroVal(row, col);
                  }
                  else if(help == one){
                      isvalid = oneVal(row, col);
                  }
                  else if(help == two){
                      isvalid = twoVal(row,col);
                  }
                  else if(help == three){
                      isvalid = threeVal(row, col);
                  }
                  else if(help == four){
                      isvalid = fourVal(row, col);
                  }
                  if(!isvalid){
                      sbreak = true;
                      System.out.println("Trouble verifying at: ("+row+", "+col+")");
                      break;
                  }
              }
              if(sbreak){
                  break;
              }
          }
        }
        else{
            System.out.println("Trouble verifying at: ("+orow+", "+ocol+")");
        }
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
            for(int j = 1; r-j > 0; j++){
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
            for(int l = 1; c-l > 0; l++){
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
        boolean hasLaser;
        boolean hasLaser2;
        boolean hasLaser3;
        if (board[r][c] == LASER){
            hasLaser = false;
            hasLaser2 = false;
            hasLaser3 = false;
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
                    for(int n = 1; c+n < numCol; n++){
                        if(board[r][c+n] == LASER){
                            hasLaser2 = true;
                            break;
                        }
                        else{
                            hasLaser2 = false;
                        }
                    }
                    for(int o = 1; c-o > 0; o++){
                        if(board[r][c-o] == LASER){
                            hasLaser3 = true;
                            break;
                        }
                        else{
                            hasLaser3 = false;
                        }
                    }
                    if(!hasLaser3 && !hasLaser2 && !hasLaser){
                        board[r+i][c] = EMPTY;
                    }
                    if(hasLaser){
                        board[r][c] = L_BEAM;
                    }
                }
                else{
                    break;
                }
            }
            hasLaser = false;
            hasLaser2 = false;
            hasLaser3 = false;
            for(int j = 1; r-j > 0; j++){
                if(board[r-j][c] == L_BEAM && !hasLaser){
                    for(int jm = 1; r-jm < numRows; jm++){
                        if(board[r-jm][c] == LASER){
                            hasLaser = true;
                            break;
                        }
                        else{
                            hasLaser = false;
                        }
                    }
                    for(int jn = 1; c+jn < numCol; jn++){
                        if(board[r][c+jn] == LASER){
                            hasLaser2 = true;
                            break;
                        }
                        else{
                            hasLaser2 = false;
                        }
                    }
                    for(int jo = 1; c-jo > 0; jo++){
                        if(board[r][c-jo] == LASER){
                            hasLaser3 = true;
                            break;
                        }
                        else{
                            hasLaser3 = false;
                        }
                    }
                    if(!hasLaser3 && !hasLaser2 && !hasLaser){
                        board[r-j][c] = EMPTY;
                    }
                    if(hasLaser){
                        board[r][c] = L_BEAM;
                    }
                }
                else{
                    break;
                }
            }
            hasLaser = false;
            hasLaser2 = false;
            hasLaser3 = false;
            for(int k = 1; c+k < numCol; k++){
                if(board[r][c+k] == L_BEAM && !hasLaser){
                    for(int km = 1; c+km < numRows; km++){
                        if(board[r][c+km] == LASER){
                            hasLaser = true;
                            break;
                        }
                        else{
                            hasLaser = false;
                        }
                    }
                    for(int kn = 1; r+kn < numCol; kn++){
                        if(board[r+kn][c] == LASER){
                            hasLaser2 = true;
                            break;
                        }
                        else{
                            hasLaser2 = false;
                        }
                    }
                    for(int ko = 1; r-ko > 0; ko++){
                        if(board[r-ko][c] == LASER){
                            hasLaser3 = true;
                            break;
                        }
                        else{
                            hasLaser3 = false;
                        }
                    }
                    if(!hasLaser3 && !hasLaser2 && !hasLaser){
                        board[r+k][c] = EMPTY;
                    }
                    if(hasLaser){
                        board[r][c] = L_BEAM;
                    }
                }
                else{
                    break;
                }
            }
            hasLaser = false;
            hasLaser2 = false;
            hasLaser3 = false;
            for(int l = 1; c-l > 0; l++){
                if(board[r][c-l] == L_BEAM && !hasLaser){
                    for(int lm = 1; c-lm < numRows; lm++){
                        if(board[r][c-lm] == LASER){
                            hasLaser = true;
                            break;
                        }
                        else{
                            hasLaser = false;
                        }
                    }
                    for(int ln = 1; r+ln < numCol; ln++){
                        if(board[r+ln][c] == LASER){
                            hasLaser2 = true;
                            break;
                        }
                        else{
                            hasLaser2 = false;
                        }
                    }
                    for(int lo = 1; r-lo > 0; lo++){
                        if(board[r-lo][c] == LASER){
                            hasLaser3 = true;
                            break;
                        }
                        else{
                            hasLaser3 = false;
                        }
                    }
                    if(!hasLaser3 && !hasLaser2 && !hasLaser){
                        board[r+l][c] = EMPTY;
                    }
                    if(hasLaser){
                        board[r][c] = L_BEAM;
                    }
                }
                else{
                    break;
                }
            }
            if(board[r][c] == LASER){
                board[r][c] = EMPTY;
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


    public boolean laserVal(int row, int col){
        if(row == 0) {

            if (col == 0) {     /**if looking at (0,0) */

                for (int r = 1; r < numRows; r++) {
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for (int c = 1; c < numCol; c++) {
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
            if (col == numCol - 1) {    /**if looking at (0,lastCol)*/

                for (int r = 1; r < numRows; r++) {
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for (int c = col - 1; c >= 0; c--) {
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
            else {  /**if looking on first row*/

                for (int r = 1; r < numRows; r++) {

                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for (int c = col - 1; c >= 0; c--) {

                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
                for(int c = col +1; c < numCol; c++){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
        }

        else if(row == numRows - 1){

            if(col == 0){   /**if looking at (lastRow,0)*/

               for(int r = row-1; r >= 0; r --){

                   if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                           || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                       break;
                   }

                   else if (board[r][col] == LASER) {
                       return false;
                   }
               }
                for(int c = col+1; c < numCol; c++){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }

            if(col == numCol-1){    /**if looking at (lastRow,lastCol)*/

                for(int r = row-1; r >= 0; r--){

                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for(int c = col-1; c >= 0; c --){

                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
            else{   /** on the last row*/

                for(int c = col-1; c >= 0; c--){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
                for(int r = row-1; r >= 0; r--){
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for(int c = col+1; c < numCol; c++){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
        }

        if(col == 0){

            if(row != 0 || row!= numRows-1){    /**looks at the first collumn*/

                for(int r = row+1; r >= 0; r++){
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for(int c = col +1; c < numCol; c++){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
                for(int r = row +1; r < numRows; r++){
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
            }
        }
        if(col == numCol-1){

            if(row != 0 || row != numRows-1){   /**if looking at last column*/

                for(int r = row+1; r < numRows; r++){
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for(int r = row-1; r >= 0; r --){
                    if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                            || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                        break;
                    }

                    else if (board[r][col] == LASER) {
                        return false;
                    }
                }
                for(int c = col -1; c >= 0; c--){
                    if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                            || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                        break;
                    }

                    else if (board[row][c] == LASER) {
                        return false;
                    }
                }
            }
        }
        else{

            for(int r = row +1; r < numRows; r++){
                if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                        || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                    break;
                }

                else if (board[r][col] == LASER) {
                    return false;
                }
            }
            for(int c = col +1; c < numCol; c++){
                if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                        || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                    break;
                }

                else if (board[row][c] == LASER) {
                    return false;
                }
            }
            for(int r = row -1; r >= 0; r--){
                if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one
                        || board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                    break;
                }

                else if (board[r][col] == LASER) {
                    return false;
                }
            }
            for(int c = col -1; c >= 0; c--){
                if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one
                        || board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                    break;
                }

                else if (board[row][c] == LASER) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean zeroVal(int row, int col){
        if(row==0){
            if(col==0){
                if(board[row][col+1]==LASER || board[row+1][col]==LASER){
                    return false;
                }
            }
            else if(col == numCol-1){
                if(board[row][col-1]==LASER || board[row+1][col]==LASER){
                    return false;
                }
            }
            else{
                if(board[row][col-1]==LASER||board[row][col+1]==LASER||board[row+1][col]==LASER){
                    return false;
                }
            }
        }
        else if(row == numRows-1){
            if(col==0){
                if(board[row-1][col]==LASER||board[row][col+1]==LASER){
                    return false;
                }
            }
            else if(col==numCol-1){
                if(board[row-1][col]==LASER||board[row][col-1]==LASER){
                    return false;
                }
            }
            else{
                if(board[row-1][col]==LASER||board[row][col-1]==LASER||board[row][col+1]==LASER){
                    return false;
                }
            }
        }
        else if(col == 0){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER||board[row][col+1]==LASER||board[row+1][col]==LASER){
                    return false;
                }
            }
        }
        else if (col == numCol-1){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER||board[row][col-1]==LASER||board[row+1][col]==LASER){
                    return false;
                }
            }
        }
        else{
            if(board[row+1][col]==LASER||board[row][col+1]==LASER||board[row-1][col]==LASER||board[row][col-1]==LASER){
                return false;
            }
        }
        return true;
    }

    public boolean oneVal(int row, int col){
        int counter = 0;
        if(row==0){
            if(col==0){
                if(board[row][col+1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
            else if(col==numCol-1){
                if(board[row][col-1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else if(row==numRows-1){
            if(col==0){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
            else if(col == numCol-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
        }
        else if(col==0){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
        }
        else if(col == numCol-1){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else{
            if(board[row+1][col]==LASER){
                counter++;
            }
            else if(board[row][col+1]==LASER){
                counter++;
            }
            else if(board[row-1][col]==LASER){
                counter++;
            }
            else if(board[row][col-1]==LASER){
                counter++;
            }
        }

        if(counter == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean twoVal(int row, int col){
        int counter = 0;
        if(row==0){
            if(col==0){
                if(board[row][col+1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
            else if(col==numCol-1){
                if(board[row][col-1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else if(row==numRows-1){
            if(col==0){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
            else if(col == numCol-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
        }
        else if(col==0){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
        }
        else if(col == numCol-1){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else{
            if(board[row+1][col]==LASER){
                counter++;
            }
            else if(board[row][col+1]==LASER){
                counter++;
            }
            else if(board[row-1][col]==LASER){
                counter++;
            }
            else if(board[row][col-1]==LASER){
                counter++;
            }
        }

        if(counter == 2){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean threeVal(int row, int col){
        int counter = 0;
        if(row==0){
            if(col==0){
                if(board[row][col+1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
            else if(col==numCol-1){
                if(board[row][col-1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else if(row==numRows-1){
            if(col==0){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
            else if(col == numCol-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
        }
        else if(col==0){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
        }
        else if(col == numCol-1){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else{
            if(board[row+1][col]==LASER){
                counter++;
            }
            else if(board[row][col+1]==LASER){
                counter++;
            }
            else if(board[row-1][col]==LASER){
                counter++;
            }
            else if(board[row][col-1]==LASER){
                counter++;
            }
        }

        if(counter == 3){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean fourVal(int row, int col){
        int counter = 0;
        if(row==0){
            if(col==0){
                if(board[row][col+1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
            else if(col==numCol-1){
                if(board[row][col-1]==LASER){
                    counter ++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else if(row==numRows-1){
            if(col==0){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
            else if(col == numCol-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
            }
            else{
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
            }
        }
        else if(col==0){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col+1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter ++;
                }
            }
        }
        else if(col == numCol-1){
            if(row != 0 || row != numRows-1){
                if(board[row-1][col]==LASER){
                    counter++;
                }
                else if(board[row][col-1]==LASER){
                    counter++;
                }
                else if(board[row+1][col]==LASER){
                    counter++;
                }
            }
        }
        else{
            if(board[row+1][col]==LASER){
                counter++;
            }
            else if(board[row][col+1]==LASER){
                counter++;
            }
            else if(board[row-1][col]==LASER){
                counter++;
            }
            else if(board[row][col-1]==LASER){
                counter++;
            }
        }

        if(counter == 4){
            return true;
        }
        else{
            return false;
        }
    }
}
