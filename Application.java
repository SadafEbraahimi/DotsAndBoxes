package Homework2;

import stdlib.StdIn;
import java.util.Scanner;

/* ***********************************************************************
 *  Compilation:  javac Application.java
 *  Dependencies: Board.java Point.java
 *
 *  This is the class that interact with the user and contains the main method.
 *
 *************************************************************************/

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to dots and boxes.");

        int ply;
        int row;
        int first;
        Turn turn;
        int column;
        Board board;
        String[] moveStr;
        Point point = new Point(0, 0);

        System.out.print("Please enter the number of rows for the board. ");
        row = StdIn.readInt();

        System.out.print("Please enter the number of columns for the board. ");
        column = StdIn.readInt();

        System.out.print("Please enter the difficulty. ");
        ply = StdIn.readInt();

        System.out.println("Who plays first? ");
        System.out.println("1. You\t2. AI");
        first = StdIn.readInt();
        System.out.print("\n");

        if(first == 1)
            turn = Turn.HUMAN;
        else
            turn = Turn.AI;

        board = new Board(column, row, turn);

        System.out.println("You should enter the coordinate of your move in this format: column,row ");

        board.monitor();

        while(!board.isGameOver()) {
            if (board.getTurn() == Turn.AI && !board.isGameOver()) {
                System.out.print("AI selected the following coordinates to play: ");
                point = Minimax.decideNextMove(board, ply);
                System.out.println("(" + point.getColumn() + " , " + point.getRow() + ")\n");
                board.drawLine(point);
                board.monitor();
            }
            else if (board.getTurn() == Turn.HUMAN && !board.isGameOver()) {
                System.out.print("Your move? ");
                moveStr = scanner.nextLine().split(",");
                point.setRow(Integer.parseInt(moveStr[1]));
                point.setColumn(Integer.parseInt(moveStr[0]));
                board.drawLine(point);
                System.out.print("\n");
                board.monitor();
            }
        }

        System.out.println("Your score: " + board.getHumanScore());
        System.out.println("AI score: " + board.getAIScore());
        if(board.getAIScore() > board.getHumanScore())
            System.out.println("Oops! AI won.");
        else if(board.getHumanScore() > board.getAIScore())
            System.out.println("You won!");
        else
            System.out.println("The game is a draw.");

    }
}
