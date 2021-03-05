package com.example.sudokusolver;

import java.util.ArrayList;

public class Solver {

    int selected_row;
    int selected_column;

    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndexes;

    Solver()
    {
        selected_row = -1;
        selected_column = -1;

        board = new int[9][9];
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                board[r][c] = 0;
            }
        }

        emptyBoxIndexes = new ArrayList<>();
    }

    public void storeEmptyBoxIndexes()
    {
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (this.board[r][c] == 0){
                    this.emptyBoxIndexes.add(new ArrayList<>());
                    this.emptyBoxIndexes.get(this.emptyBoxIndexes.size() - 1).add(r);
                    this.emptyBoxIndexes.get(this.emptyBoxIndexes.size() - 1).add(c);
                }
            }
        }
    }

    public void clearBoard()
    {
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                board[r][c] = 0;
            }
        }

        this.emptyBoxIndexes = new ArrayList<>();
    }

    public void setNumberPos(int num)
    {
        if (this.selected_row != -1 && this.selected_column != -1)
        {
            if (this.board[this.selected_row-1][this.selected_column-1] == num){
                this.board[this.selected_row-1][this.selected_column-1] = 0;
            }else{
                this.board[this.selected_row-1][this.selected_column-1] = num;
            }
        }
    }

    public int[][] getBoard() {
        return this.board;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndexes(){
        return this.emptyBoxIndexes;
    }

    public int getSelected_row(){
        return selected_row;
    }

    public int getSelected_column(){
        return selected_column;
    }

    public void setSelected_row(int r){
        selected_row = r;
    }

    public void setSelected_column(int c){
        selected_column = c;
    }



    public boolean solveSudoku(Board board)
    {
        //Search for the first empty cell
        int chosenRow = -1;
        int chosenCol = -1;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (this.board[r][c] == 0) {
                    chosenRow = r;
                    chosenCol = c;
                }
            }
        }


        //If an empty cell wasn't found
        if (chosenRow == -1)
            return true;

        int guessedNumber = 1;

        while (guessedNumber <= 9)
        {
            if (isViable(guessedNumber, chosenRow, chosenCol)) {
                this.board[chosenRow][chosenCol] = guessedNumber;
                board.invalidate();
                //Recursion
                if (solveSudoku(board))
                    //jos stackin ylin solveSudoku palauttaa tämän niin sudoku on ratkaistu?
                    return true;
            }

            //If guess is not viable, increment the guess:
            guessedNumber++;

            //If the guesses 1 to 9 weren't viable return False to signal that we need to go back
            this.board[chosenRow][chosenCol] = 0;
        }
        return false;
    }


    public boolean isViable(int guess, int chosenRow, int chosenCol)
    {
        //Row test
        for (int col = 0; col < 9; col++) {
            if (this.board[chosenRow][col] == guess)
                return false;
        }

        //Column test
        for (int row = 0; row < 9; row++) {
            if (this.board[row][chosenCol] == guess)
                return false;
        }

        //3x3 block test
        int rowStart = chosenRow / 3 * 3;
        int colStart = chosenCol / 3 * 3;

        for (int row = rowStart; row < rowStart + 3; row++) {
            for (int col = colStart; col < colStart + 3; col++) {
                if (this.board[row][col] == guess)
                    return false;
            }
        }
        return true;
    }
}
