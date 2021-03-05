package com.example.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Board extends View {
    private final int boardColor;
    private final int cellSelectedColor;
    private final int rowColumnHighlightColor;
    private final int letterColor;
    private final int letterColorSolver;

    private final Paint boardPaint = new Paint();
    private final Paint cellPaint = new Paint();
    private final Paint rcHighlightPaint = new Paint();
    private final Paint letterPaint = new Paint();
    private final Rect letterBounds = new Rect();

    private int cellSize;

    private final Solver solver = new Solver();

    public Board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Board, 0, 0);

        try{
            boardColor = array.getInteger(R.styleable.Board_color, 0);
            cellSelectedColor = array.getInteger(R.styleable.Board_cellSelectedColor, 0);
            rowColumnHighlightColor = array.getInteger(R.styleable.Board_rcHighlightColor, 0);
            letterColor = array.getInteger(R.styleable.Board_letterColor, 0);
            letterColorSolver = array.getInteger(R.styleable.Board_letterColorSolver, 0);
        }finally {
            array.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //get board dimension and cellSize when onMeasure is called
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();

        int dimension = Math.min(width, height);
        cellSize = dimension / 9;

        setMeasuredDimension(dimension, dimension);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        //Paint setup
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(16);
        boardPaint.setColor(boardColor);
        boardPaint.setAntiAlias(true);

        cellPaint.setStyle(Paint.Style.FILL);
        cellPaint.setColor(cellSelectedColor);
        cellPaint.setAntiAlias(true);

        rcHighlightPaint.setStyle(Paint.Style.FILL);
        rcHighlightPaint.setColor(rowColumnHighlightColor);
        rcHighlightPaint.setAntiAlias(true);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);
        //

        //Drawing
        colorHighlightedCells(canvas, solver.getSelected_row(), solver.getSelected_column());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isValid;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        //Set clicked cell active if the event is 'ACTION_DOWN'
        if (action == MotionEvent.ACTION_DOWN) {
            solver.setSelected_row((int) Math.ceil(y / cellSize));
            solver.setSelected_column((int) Math.ceil(x / cellSize));
            isValid = true;
        }else{
            isValid = false;
        }

        return isValid;
    }


    private void drawNumbers(Canvas canvas)
    {
        letterPaint.setTextSize(cellSize);


        //first color the user-inputted numbers
        int[][] board = solver.getBoard();
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (board[r][c] != 0){
                    String number = Integer.toString(board[r][c]);
                    float width, height;

                    letterPaint.getTextBounds(number, 0, number.length(), letterBounds);
                    width = letterPaint.measureText(number);
                    height = letterBounds.height();

                    canvas.drawText(number, (c * cellSize) + ((cellSize - width) / 2), (r * cellSize + cellSize) - ((cellSize - height) / 2), letterPaint);
                }
            }
        }

        //change the color and then do the same for solvers numbers
        letterPaint.setColor(letterColorSolver);

        for (ArrayList<Object> letter : solver.getEmptyBoxIndexes())
        {
            int r = (int) letter.get(0);
            int c = (int) letter.get(1);

            String number = Integer.toString(board[r][c]);
            float width, height;

            letterPaint.getTextBounds(number, 0, number.length(), letterBounds);
            width = letterPaint.measureText(number);
            height = letterBounds.height();

            canvas.drawText(number, (c * cellSize) + ((cellSize - width) / 2), (r * cellSize + cellSize) - ((cellSize - height) / 2), letterPaint);
        }
    }


    private void colorHighlightedCells(Canvas canvas, int r, int c)
    {
        if(solver.getSelected_row() != -1 && solver.getSelected_column() != -1)
        {
            //draw row and column of the selected cell
            canvas.drawRect((c - 1) * cellSize, 0, c * cellSize, cellSize * 9, rcHighlightPaint);
            canvas.drawRect(0, (r - 1) * cellSize, cellSize * 9, r * cellSize, rcHighlightPaint);
            //then the selected cell itself
            canvas.drawRect((c - 1) * cellSize, (r - 1) * cellSize, c * cellSize, r * cellSize, cellPaint);
        }
        invalidate();
    }


    private void setPaintForBigLines()
    {
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(10);
        boardPaint.setColor(boardColor);
        boardPaint.setAntiAlias(true);
    }

    private void setPaintForSmallLines()
    {
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(4);
        boardPaint.setColor(boardColor);
        boardPaint.setAntiAlias(true);
    }

    private void drawBoard(Canvas canvas)
    {
        for (int c = 0; c < 10; c++)
        {
            if (c % 3 == 0){
                setPaintForBigLines();
            }else{
                setPaintForSmallLines();
            }
            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), boardPaint);
        }

        for (int r = 0; r < 10; r++)
        {
            if (r % 3 == 0){
                setPaintForBigLines();
            }else{
                setPaintForSmallLines();
            }
            canvas.drawLine(0, cellSize * r, getWidth(), cellSize * r, boardPaint);
        }
    }

    //getter for Solver
    public Solver getSolver(){
        return this.solver;
    }
}
