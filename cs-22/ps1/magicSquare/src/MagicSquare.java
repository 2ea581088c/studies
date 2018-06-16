/* 
 * MagicSquare.java
 * 
 * Author:          Computer Science E-22 staff
 * Modified by:     <your name>, <your e-mail address>
 * Date modified:   <current date>
 */

import java.lang.reflect.Array;
import java.util.*;

public class MagicSquare {
    // the current contents of the cells of the puzzle values[r][c]
    // gives the value in the cell at row r, column c
    private int[][] values;

    // the order (i.e., the dimension) of the puzzle
    private int order;

    // the sum of each side for a given order
    private int magicSum;

    // available numbers to choose from to fill the array. set to true on initialisation and set to false if used.
    // of form boolean[n] isAvailable, where n is between 0 to orderSq - 1
    private boolean[] isAvailable;

    // order ^ 2
    private int orderSq;

    // int[i][j] array where [i] is an int from 0 - orderSq, and [j=2] is of form [row][col].
    private int[][] arrayCoord;

    private boolean solutionFound = false;

    /**
     * Creates a MagicSquare object for a puzzle with the specified
     * dimension/order.
     */
    public MagicSquare(int order) {
        values = new int[order][order];
        this.order = order;

        // Add code to this constructor as needed to initialize
        // the fields that you add to the object.

        this.magicSum = (int)Math.pow(order, 3) + order;
        this.magicSum = magicSum / 2;

        orderSq = (int)Math.pow(order, 2);

        isAvailable = new boolean[orderSq];
        for (int i = 0; i < orderSq; i++) {
            isAvailable[i] = true;
        }

        //implement array for helperArray() to return int[2] of row, column
        arrayCoord = new int[order][order];
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                arrayCoord[i][j] = -1;
            }
        }
        arrayHelper();
    }

    /**
     * This method should call the separate recursive-backtracking method
     * that you will write, passing it the appropriate initial parameter(s).
     * It should return true if a solution is found, and false otherwise.
     */
    public boolean solve() {
        // Replace the line below with your implementation of this method.
        // REMEMBER: The recursive-backtracking code should NOT go here.
        // See the comments above.
        backtrack(0);
        return solutionFound;
    }

    /**
     * Recursive backtracking method that modifies the int array 'values' until a solution.
     * may be modified to find multiple solutions by storing solutions outside frame of function and incrementing counter
     *
     * int n is the current cell to be filled. should be called with value 0 from outside of function.
     */
    public void backtrack(int n) {
        //for (val = first to last) {
        //if (isValid(val, n)) {
        //applyValue(val, n);
        //findSolutions(n + 1, other params);
        //removeValue(val, n);
        //}
        //}

        int row;
        int col;

        // fill in incrementing order - arrayCoord[n] returns the coordinates for the nth cell to be filled.
        // each number to be used exactly once. search bool[] for available numbers.
        // this will increment through the length of isAvailable to make sure each value is used once

        for (int j = 0; j < isAvailable.length; j++) {
            // if solution found, end the recursive call.
            if (solutionFound) {
                break;
            }

            if (isAvailable[j]) {
                // remove number from pool of available numbers to fill in cells and search next cell
                isAvailable[j] = false;
                // retrieve array coordinates and set j as the value of the cell
                row = arrayCoord[n][0];
                col = arrayCoord[n][1];
                values[row][col] = j + 1;

                // if reached end of a row or col, check that the sum matches magicSum
                // row == order - 1 means reached end of a col. vice versa for col == order - 1
                // reset cell value to 0 and add number back to pool of available numbers if sumCheck returns false
                if (row == order - 1 && col == order - 1) {
                    if (sumCheck('r', row) && sumCheck('c', col)) {
                        solutionFound = true;
                        break;
                    }

                } else if (row == order - 1) {
                    if (!sumCheck('c', col)) {
                        values[row][col] = 0;
                    } else {
                        backtrack(n + 1);
                    }

                } else if (col == order - 1) {
                    if (!sumCheck('r', row)) {
                        values[row][col] = 0;
                    } else {
                        backtrack(n + 1);
                    }

                } else {
                    backtrack(n + 1);
                }

                isAvailable[j] = true;
            }
        }
    }

    /**
     * Checks if row/column sums up to the magicSum, returns true if summed or false if not summed
     * input - character either 'r' for row or 'c' for column.
     * should be used in conjunction with incrementing integer along with %2 function to alternate between row/col
     */
    public boolean sumCheck(char c, int i) {
        // if char == 'r' check for row i. rows from 0-(n-1)
        int sum = 0;

        if (c == 'r') {
            for (int r = 0; r < this.order; r++) {
                sum += values[i][r];
            }
        } else if (c == 'c') {
            for (int k = 0; k < this.order; k++) {
                sum += values[k][i];
            }
        }

        return (sum == magicSum);
    }

    /**
     * helper function that calculates which row/column currently working on
     * n is an integer that increments up and is used to backtrack by decrementing
     * this function is run once to initialise ArrayCoord as an int[total number of cells][2]
     * where each int[2] is in form [0] - row, [1] - col
     */
    public void arrayHelper() {
        int row = 0;
        int col = 0;
        int switcher = 0;
        for (int i = 0; i < orderSq; i++) {
            //fill out nxn array with any empty spaces in row 0 first, then col 0, then row 1, col 1 etc.
            if (switcher % 2 == 0) {
                // if [row][col] is empty, set row/col to value i. else, search rest of row for empty col
                while (true) {
                    if (arrayCoord[row][col] == -1) {
                        arrayCoord[row][col] = i;
                        break;
                    }

                    col++;

                    // if last column searched, then switch to rows
                    if (col == order) {
                        switcher++;
                        col = row;
                        row = 0;
                        i --;
                        break;
                    }
                }
            } else {
                // search col for any empty row
                while (true) {
                    if (arrayCoord[row][col] == -1) {
                        arrayCoord[row][col] = i;
                        break;
                    }

                    row++;

                    // if last row searched, then switch to cols
                    if (row == order) {
                        switcher++;
                        row = col + 1;
                        col = 0;
                        i --;
                        break;
                    }
                }
            }
        }

        // create a new int[orderSq][2] array to read row/col values for a given int n
        int[][] newArray = new int[orderSq][2];
        for (int i = 0; i < orderSq; i++) {
            boolean flagFound = false;

            for (int j = 0; j < order; j++) {
                for (int k = 0; k < order; k++) {
                    if (arrayCoord[j][k] == i) {
                        newArray[i][0] = j;
                        newArray[i][1] = k;
                        flagFound = true;
                    }

                    if (flagFound) {
                        break;
                    }
                }

                if (flagFound) {
                    break;
                }
            }
        }

        arrayCoord = newArray;
    }


    /**
     * Displays the current state of the puzzle.
     * You should not change this method.
     */
    public void display() {
        for (int r = 0; r < order; r++) {
            printRowSeparator();
            for (int c = 0; c < order; c++) {
                System.out.print("|");
                if (values[r][c] == 0)
                    System.out.print("   ");
                else {
                    if (values[r][c] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(" " + values[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        printRowSeparator();
    }

    // A private helper method used by display()
    // to print a line separating two rows of the puzzle.
    private void printRowSeparator() {
        for (int i = 0; i < order; i++)
            System.out.print("-----");
        System.out.println("-");
    }
    
    public static void main(String[] args) {
        /*******************************************************
          **** You should NOT change any code in this method ****
          ******************************************************/

        Scanner console = new Scanner(System.in);
        System.out.print("What order Magic Square would you like to solve? ");
        int order = console.nextInt();
        
        MagicSquare puzzle = new MagicSquare(order);
        if (puzzle.solve()) {
            System.out.println("Here's the solution:");
            puzzle.display();
        } else {
            System.out.println("No solution found.");
        }


    }
}