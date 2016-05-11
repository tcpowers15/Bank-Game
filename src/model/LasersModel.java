package model;

import backtracking.Backtracker;
import backtracking.Configuration;
import backtracking.SafeConfig;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Optional;
import java.util.Scanner;

public class LasersModel extends Observable {

    private char [][] board;  //This 2d array is the board itself
    private int numRows = 0;
    private int numCol = 0;
    private String WeOut = "";
    private String filename = "";
    private SafeConfig solution;

    private final static char EMPTY = '.';  //used for tiles that currently have nothing on them
    private final static char LASER = 'L';  // used for tiles that are holding a laser
    private final static char L_BEAM = '*';  // used for tiles that are covered by a laser beam
    private final static char xPillar = 'X';  // used for tiles that are pillars that can have an unlimited amount of lasers
    private final static char zero = '0'; //used for pillars that requires zero lasers
    private final static char one = '1'; //used for pillars that requires one laser
    private final static char two = '2'; //used for pillars that requires two lasers
    private final static char three = '3'; //used for pillars that requires three lasers
    private final static char four = '4'; //used for pillars that requires four lasers

    public LasersModel(String filename) throws FileNotFoundException {
        this.filename = filename;
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
                        this.WeOut = "Error verifying at: ("+row+", "+col+")";
                        break;
                    }
                }
                if(sbreak){
                    break;
                }
            }
            this.WeOut = "Safe is fully verified!";
        }
        else{
            this.WeOut = "Error verifying at: ("+orow+", "+ocol+")";
        }
        announceChange();
    }



    /**
     * This method adds a laser at the specified location
     * @param r the row number
     * @param c the column number
     */
    public void add(int r, int c) {
        if (r >= 0 && r < numRows && c >= 0 && c < numCol) {
            if (board[r][c] == LASER || board[r][c] == xPillar || board[r][c] == zero || board[r][c] == one
                    || board[r][c] == two || board[r][c] == three || board[r][c] == four) {
                this.WeOut = "Error adding laser at: (" + r + ", " + c + ")";
            } else if (board[r][c] == EMPTY || board[r][c] == L_BEAM) {
                board[r][c] = LASER;
                for (int i = 1; r + i < numRows; i++) {
                    if (board[r + i][c] == EMPTY || board[r + i][c] == L_BEAM) {
                        board[r + i][c] = L_BEAM;
                    } else {
                        break;
                    }

                }
                for (int j = 1; r - j >= 0; j++) {
                    if (board[r - j][c] == EMPTY || board[r - j][c] == L_BEAM) {
                        board[r - j][c] = L_BEAM;
                    } else {
                        break;
                    }
                }
                for (int k = 1; c + k < numCol; k++) {
                    if (board[r][c + k] == EMPTY || board[r][c + k] == L_BEAM) {
                        board[r][c + k] = L_BEAM;
                    } else {
                        break;
                    }
                }
                for (int l = 1; c - l >= 0; l++) {
                    if (board[r][c - l] == EMPTY || board[r][c - l] == L_BEAM) {
                        board[r][c - l] = L_BEAM;
                    } else {
                        break;
                    }
                }
                this.WeOut = "Laser added at: (" + r + ", " + c + ")";
            }
        }
        else{
            this.WeOut = "Error adding laser at: (" + r + ", " + c + ")";
        }
        announceChange();
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

    /**
     * Removes the Laser at he given row and column if there is one.
     * @param r the row number
     * @param c the column number
     */
    public void remove(int r, int c){
        if (r >= 0 && r < numRows && c >= 0 && c < numCol) {
            if (board[r][c] == LASER) {
                if (removedown(r, c) || removeup(r, c)) {
                    board[r][c] = L_BEAM;
                } else {
                    for (int row = r; row < numRows; row++) {
                        if (!removeright(row, c) && !removeleft(row, c) && board[row][c] == L_BEAM) {
                            board[row][c] = EMPTY;
                        } else if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one ||
                                board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                            break;
                        }
                    }
                    for (int row = r; row >= 0; row--) {
                        if (!removeright(row, c) && !removeleft(row, c) && board[row][c] == L_BEAM) {
                            board[row][c] = EMPTY;
                        } else if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one ||
                                board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                            break;
                        }
                    }
                }
                if (removeleft(r, c) || removeright(r, c)) {
                    board[r][c] = L_BEAM;
                } else {
                    for (int col = c; col < numCol; col++) {
                        if (!removeup(r, col) && !removedown(r, col) && board[r][col] == L_BEAM) {
                            board[r][col] = EMPTY;
                        } else if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one ||
                                board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                            break;
                        }
                    }
                    for (int col = c; col >= 0; col--) {
                        if (!removeup(r, col) && !removedown(r, col) && board[r][col] == L_BEAM) {
                            board[r][col] = EMPTY;
                        } else if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one ||
                                board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                            break;
                        }
                    }
                }
                if (board[r][c] == LASER) {
                    board[r][c] = EMPTY;
                }
                this.WeOut = "Laser removed at: (" + r + ", " + c + ")";
            } else {
                this.WeOut = "Error removing laser at: (" + r + ", " + c + ")";
            }
        }
        else{
            this.WeOut = "Error removing laser at: (" + r + ", " + c + ")";
        }
        announceChange();
    }

    /**
     * Checks if there is a laser to the right
     * @param r the row number
     * @param c the column number
     * @return boolean true if there is a laser to the right
     */
    public boolean removeright(int r, int c){
        boolean hasLaserright = false;

        for(int col = c+1; col < numCol; col++) {
            if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one ||
                    board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                break;
            }
            else if (board[r][col] == LASER) {
                hasLaserright = true;
                break;
            }
        }
        return hasLaserright;
    }

    /**
     * Checks if there is a laser to the left
     * @param r the row number
     * @param c the column number
     * @return boolean true if there is a laser to the left
     */
    public boolean removeleft(int r, int c){
        boolean hasLaserleft = false;

        for(int col = c-1; col >= 0; col--) {
            if (board[r][col] == xPillar || board[r][col] == zero || board[r][col] == one ||
                    board[r][col] == two || board[r][col] == three || board[r][col] == four) {
                break;
            }
            else if (board[r][col] == LASER) {
                hasLaserleft = true;
                break;
            }
        }
        return hasLaserleft;
    }

    /**
     * Checks if there is a laser to the up
     * @param r the row number
     * @param c the column number
     * @return boolean true if there is a laser to the up
     */
    public boolean removeup(int r, int c){
        boolean hasLaserup = false;

        for(int row = r+1; row < numRows; row++) {
            if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one ||
                    board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                break;
            }
            else if (board[row][c] == LASER) {
                hasLaserup = true;
                break;
            }
        }
        return hasLaserup;
    }

    /**
     * Checks if there is a laser to the down
     * @param r the row number
     * @param c the column number
     * @return boolean true if there is a laser to the down
     */
    public boolean removedown(int r, int c){
        boolean hasLaserdown = false;

        for(int row = r-1; row >= 0; row--) {
            if (board[row][c] == xPillar || board[row][c] == zero || board[row][c] == one ||
                    board[row][c] == two || board[row][c] == three || board[row][c] == four) {
                break;
            }
            else if (board[row][c] == LASER) {
                hasLaserdown = true;
                break;
            }
        }
        return hasLaserdown;
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

    public String getOuts(){
        return this.WeOut;
    }

    public int getrows(){
        return this.numRows;
    }

    public int getcols(){
        return this.numCol;
    }

    public char getPos(int col, int row){
        return board[row][col];
    }

    public void hint() throws FileNotFoundException{
        // construct the initial configuration from the file
        Configuration init = new SafeConfig(this.filename);

        // create the backtracker with the debug flag
        Backtracker bt = new Backtracker(false);

        // start the clock
        double start = System.currentTimeMillis();

        // attempt to solve the puzzle
        Optional<Configuration> sol = bt.solve(init);
        boolean works = true;
        boolean hint = false;
        int ro = 0;
        int co = 0;
        if(sol.isPresent()){
            this.solution = (SafeConfig)sol.get();
            for(int i = 0; i < numRows; i++){
                for(int j = 0; j < numCol; j++){
                    if(board[i][j] == 'L' && solution.getboard()[i][j] != 'L'){
                       works = false;
                    }
                }
            }
            if(works){
                for(int r = 0; r < numRows; r++) {
                    for (int c = 0; c < numCol; c++) {
                            if (solution.getboard()[r][c] == 'L' && board[r][c] != 'L' && !hint) {
                                hint = true;
                                add(r, c);
                                WeOut = "Hint: added laser to (" + r + ", " + c + ")";
                            }
                    }
                }
            }
            else{
                WeOut = "Hint: no next step!";
            }
            announceChange();
        }else{

            announceChange();
        }
    }

    public void solve()throws FileNotFoundException{
        // construct the initial configuration from the file
        Configuration init = new SafeConfig(this.filename);

        // create the backtracker with the debug flag
        Backtracker bt = new Backtracker(false);

        // start the clock
        double start = System.currentTimeMillis();

        // attempt to solve the puzzle
        Optional<Configuration> sol = bt.solve(init);
        if(sol.isPresent()){
            this.solution = (SafeConfig)sol.get();

            this.board = this.solution.getboard();
            announceChange();
        }else{
            this.board = this.solution.getboard();
            announceChange();
        }


        announceChange();
    }

    public void restart() throws FileNotFoundException{
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
        this.WeOut = filename + " has been reset";
        announceChange();
    }

    public String getFilename(){
        return this.filename;
    }

    public void setFilename(String file){
        this.filename = file;
    }

    public void load(){
        this.WeOut = filename + " loaded";
        announceChange();
    }

    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    private void announceChange() {
        setChanged();
        notifyObservers();
    }
}
