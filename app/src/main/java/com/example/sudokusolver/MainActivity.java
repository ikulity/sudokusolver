package com.example.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Board board;
    private Solver boardSolver;
    private Button solveBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        solveBTN = findViewById(R.id.solveButton);
        board = findViewById(R.id.Board);

        boardSolver = board.getSolver();
    }


    public void BTNSolve(View view){
        if (solveBTN.getText().toString().equals(getString(R.string.solve))){
            solveBTN.setText(getString(R.string.clear));

            boardSolver.storeEmptyBoxIndexes();

            BoardSolveThread boardSolveThread = new BoardSolveThread();
            new Thread(boardSolveThread).start();

            board.invalidate();
        }else{
            solveBTN.setText(getString(R.string.solve));
            boardSolver.clearBoard();
            board.invalidate();
        }
    }

    class BoardSolveThread implements Runnable{

        @Override
        public void run()
        {
            boardSolver.solveSudoku(board);
        }
    }


    public void BTNOnePress(View view){
        boardSolver.setNumberPos(1);
        board.invalidate();
    }

    public void BTNTwoPress(View view){
        boardSolver.setNumberPos(2);
        board.invalidate();
    }

    public void BTNThreePress(View view){
        boardSolver.setNumberPos(3);
        board.invalidate();
    }

    public void BTNFourPress(View view){
        boardSolver.setNumberPos(4);
        board.invalidate();
    }

    public void BTNFivePress(View view){
        boardSolver.setNumberPos(5);
        board.invalidate();
    }

    public void BTNSixPress(View view){
        boardSolver.setNumberPos(6);
        board.invalidate();
    }

    public void BTNSevenPress(View view){
        boardSolver.setNumberPos(7);
        board.invalidate();
    }

    public void BTNEightPress(View view){
        boardSolver.setNumberPos(8);
        board.invalidate();
    }

    public void BTNNinePress(View view){
        boardSolver.setNumberPos(9);
        board.invalidate();
    }

}