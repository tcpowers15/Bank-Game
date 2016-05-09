package backtracking;

import java.util.Collection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Trevor Powers
 */
public class SafeConfig implements Configuration {

    /** constants */
    private final static char EMPTY = '.'; //if tile is empty
    private final static char LASER = 'L';//Laser on tile
    private final static char L_BEAM = '*';//tiles covered by beam
    private final static char xPillar = 'X';//pillar with unlimited lasers off it
    private final static char zero = '0';//pillar that can hvae noe lasers
    private final static char one = '1';//pillar that can hav 1
    private final static char two = '2';//pillar that can have 2
    private final static char three = '3';//pillar that can have 3
    private final static char four = '4';//pillar that can 4


    //BOARD STUFF
    public int numRows;//number of rows on board
    public int numCol;//number of col on board
    public char [][]board; // the board
    public static int row = 0; //current row
    public static int col = -1; //current col
    private int personalRow;
    private int personalCol;


    public SafeConfig(String filename) throws FileNotFoundException {
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

    public SafeConfig(SafeConfig other){
        this.numRows = other.numRows;
        this.numCol = other.numCol;
        this.board = new char[other.numRows][other.numCol];
        this.personalCol = other.personalCol;
        this.personalRow = other.personalRow;

        for(int row = 0; row < this.numRows; row++){
            for(int col = 0; col < this.numCol; col++){
                this.board[row][col] = other.board[row][col];
            }
        }

    }

    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> newConfigs = new ArrayList<Configuration>();
        if(col+1 >= numCol || row >= numRows){
            if(row >= numRows){
                col = personalCol;
                row = personalRow;
            }
            else {
                row += 1;
                col = 0;
            }
        }
        else{
            col ++;
        }
        this.personalCol = col +1;
        this.personalRow = row;
        if(row==3 && col ==3){
            int g = 0;
        }
        try{

            if(board[row][col] != EMPTY){
                char thing = board[row][col];
                SafeConfig child = new SafeConfig(this);
                child.board[row][col] = thing;
                newConfigs.add(child);
                return newConfigs;
            }
            if(board[row][col] == EMPTY){
                SafeConfig child = new SafeConfig(this);
                child.adds(row,col);
                newConfigs.add(child);

                SafeConfig child2 = new SafeConfig(this);
                child2.board[row][col] = EMPTY;
                newConfigs.add(child2);

                return newConfigs;

            }


        }
        catch(IndexOutOfBoundsException e){
        }

        return newConfigs;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        try{
            for(int r = 0; r < numRows; r++){
                for(int c = 0; c < numCol; c++){
                    if(!valid){
                        break;
                    }

                    char help = board[r][c];
                    if(help == LASER){
                        valid = laserVal(r,c);
                    }
                    else if(help == zero){
                        valid = zeroVal(r,c);
                    }
                    else if(help == one){
                        valid = oneVal(r,c);
                    }
                    else if(help == two){
                        valid = twoVal(r,c);
                    }
                    else if(help == three){
                        valid = threeVal(r,c);
                    }
                    else if(help == four){
                        valid = fourVal(r,c);
                    }
                }
                if(!valid){
                    break;
                }
            }

        }
        catch(IndexOutOfBoundsException w){
        }
        return valid;
    }

    @Override
    public boolean isGoal() {
        boolean correct = true;
        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCol; c ++){
                if(board[r][c] == EMPTY){
                    correct = false;
                }
            }
        }
        return correct;
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



    /** methods for manipulating the boar*/

    /**
     * This method adds a laser at the specified location
     * @param r the row number
     * @param c the column number
     */
    private void adds(int r, int c) {
        if (r >= 0 && r < numRows && c >= 0 && c < numCol) {
            if (board[r][c] == LASER || board[r][c] == xPillar || board[r][c] == zero || board[r][c] == one
                    || board[r][c] == two || board[r][c] == three || board[r][c] == four) {
                System.out.println("Error adding laser at: (" + r + ", " + c + ")");
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
                System.out.println("Laser added at: (" + r + ", " + c + ")");
            }
        }
        else{
            System.out.println("Error adding laser at: (" + r + ", " + c + ")");
        }
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
            if(board[row][col+1]==LASER){
                counter++;
            }
            if(board[row-1][col]==LASER){
                counter++;
            }
            if(board[row][col-1]==LASER){
                counter++;
            }
        }

        if(counter <= 1){
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

        if(counter <= 2){
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

        if(counter <= 3){
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

        if(counter <= 4){
            return true;
        }
        else{
            return false;
        }
    }

}
