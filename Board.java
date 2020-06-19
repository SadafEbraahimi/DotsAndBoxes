package Homework2;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/* ***********************************************************************
 *  Compilation:  javac Board.java
 *  Dependencies: Move.java Turn.java
 *
 *  Any board object represents a possible setting in the game.
 *
 *************************************************************************/

public class Board {

    public int value;
    public int depth;
    private Turn turn;
    public Board parent;
    private int AIScore;
    private int[][] matrix;
    private int humanScore;
    private int lastChangedRow;
    private int lastChangedColumn;
    public LinkedList<Board> children;

    public Turn getTurn() { return turn; }
    public int getAIScore() { return AIScore; }
    public int getHumanScore() { return humanScore; }
    public int getLastChangedRow() { return lastChangedRow; }
    public int getLastChangedColumn() { return lastChangedColumn; }

    public Board(int rows, int columns, Turn turn) {
        matrix = new int[rows * 2 + 1][columns * 2 + 1];
        this.turn = turn;
        lastChangedRow = 0;
        lastChangedColumn = 0;
        AIScore = 0;
        humanScore = 0;
        value = 0;
        depth = 0;
        parent = null;
        children = new LinkedList<>();
        matrixGenerator();
    }

    public Board(Board board) {
        matrix = new int[board.matrix.length][board.matrix[0].length];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = board.matrix[i][j];
        turn = board.turn;
        lastChangedRow = board.lastChangedRow;
        lastChangedColumn = board.lastChangedColumn;
        AIScore = board.AIScore;
        humanScore = board.humanScore;
        value = 0;
        depth = 0;
        parent = null;
        children = new LinkedList<>();
    }

    // It assigns values to blocks of the board randomly.
    // All numbers should be in range of 1 to 5.
    private void matrixGenerator() {
        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix[row].length; col++) {
                if((row % 2 == 0) || (col % 2 == 0))
                    matrix[row][col] = 0;
                else if ((row % 2 != 0) && (col % 2 != 0))
                    matrix[row][col] = ThreadLocalRandom.current().nextInt(1, 6);
            }
        }
    }

    // Takes a move object as an argument. A place on the board is already marked if its value is 1.
    // So we change that coordinate on the board to 1, and also reset the values of lastChangedRow
    // And lastChangedColumn to the recently drawn line.
    // In the end we have to change the turn because one has made its move.
    public void drawLine(Point point) {
        matrix[point.getRow()][point.getColumn()] = 1;
        lastChangedRow = point.getRow();
        lastChangedColumn = point.getColumn();
        updateScoreBoard(point);
        if(turn == Turn.AI)
            turn = Turn.HUMAN;
        else
            turn = Turn.AI;
    }

    public void addChildren() {
        int start;
        for(int i = 0; i < matrix.length; i++) {
            if (i % 2 == 0)
                start = 1;
            else
                start = 0;
            for(int j = start; j < matrix[i].length; j += 2)
                if(matrix[i][j] < 1) {
                    Point point = new Point(i, j);
                    Board child = new Board(this);
                    child.depth = depth + 1;
                    child.parent = this;
                    child.drawLine(point);
                    children.add(child);
                }
        }
    }

    // If one of the randomly generated numbers on the board is surrounded with lines,
    // It means we have to assign that number to the score of last player who drew a line.
    // As mentioned earlier, those places which already have a line are marked by 1.
    private boolean isSquareComplete(Point point) {
        int row = point.getRow();
        int column = point.getColumn();
        if(row > 0 && row < matrix.length && column > 0 && column < matrix[row].length) {
            if(matrix[row - 1][column] > 0 && matrix[row + 1][column] > 0 && matrix[row][column - 1] > 0 && matrix[row][column + 1] > 0)
                return true;
        }
        return false;
    }

    // After every line that was successfully drawn, we call this method to update
    // AI and Human's scores. Here we check whether the last move that was made
    // created a surrounded area on the board. If yes, we assign the number in that
    // area to the player who made the last move.
    private void updateScoreBoard(Point point) {
        int row = point.getRow();
        int column = point.getColumn();
        if(row % 2 == 0) {
            Point up = new Point(row - 1, column);
            if(isSquareComplete(up)) {
                if(turn == Turn.AI)
                    AIScore += matrix[row - 1][column];
                else
                    humanScore += matrix[row - 1][column];
            }
            Point down = new Point(row + 1, column);
            if(isSquareComplete(down)) {
                if(turn == Turn.AI)
                    AIScore += matrix[row + 1][column];
                else
                    humanScore += matrix[row + 1][column];
            }
        }
        else {
            Point left = new Point(row, column - 1);
            if(isSquareComplete(left)) {
                if(turn == Turn.AI)
                    AIScore += matrix[row][column - 1];
                else
                    humanScore += matrix[row][column - 1];
            }
            Point right = new Point(row, column + 1);
            if(isSquareComplete(right)) {
                if(turn == Turn.AI)
                    AIScore += matrix[row][column + 1];
                else
                    humanScore += matrix[row][column + 1];
            }
        }
    }

    // If a possible place to draw on the board is filled with 1, it means that
    // Place is already full. In this method we iterate over the board and if we
    // Detect one place that is still 0, it means we still have empty places
    // To draw lines. If that is the case, this method returns false and the game
    // Must go on.
    public boolean isGameOver() {
        int start;
        for(int i = 0; i < matrix.length; i++) {
            if(i % 2 == 0)
                start = 1;
            else
                start = 0;
            for(int j = start; j < matrix[i].length; j += 2)
                if(matrix[i][j] == 0)
                    return false;
        }
        return true;
    }

    // This method prints the board object in a human-readable format.
    // It tried to make it look exactly like the example prof mentioned on D2L
    // Discussion board.
    public void monitor() {
        System.out.print("  ");

        for (int i = 0; i < matrix[0].length; i++)
            System.out.print(" " + i);
        System.out.print("\n   ");
        for (int i = 0; i < matrix[0].length; i++)
            System.out.print("__");

        System.out.print("\n");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(i + "| ");
            if (i % 2 == 0) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (j % 2 == 0)
                        System.out.print(".");
                    else if (matrix[i][j] > 0)
                        System.out.print("───");
                    else
                        System.out.print("   ");
                }
            } else {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (j % 2 != 0)
                        System.out.print(" " + matrix[i][j] + " ");
                    else if (matrix[i][j] > 0)
                        System.out.print("│");
                    else
                        System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("   ");
        for (int col = 0; col < matrix[0].length; col++)
            System.out.print("__");
        System.out.println("\n");
        System.out.println("Current Score =====>> Your Score " + humanScore + " AI Score " + AIScore + "\n");
    }
}
