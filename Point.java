package Homework2;

/* ***********************************************************************
 *  Compilation:  javac Point.java
 *  Dependencies:
 *
 *  Any object of this class represents a valid move in the game in a (row, column) pair.
 *
 *************************************************************************/

public class Point {

    private int row;
    private int column;

    public Point(int row, int column){
        this.row = row;
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

}
