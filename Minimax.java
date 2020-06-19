package Homework2;

/* ***********************************************************************
 *  Compilation:  javac Minimax.java
 *  Dependencies: Move.java Board.java
 *
 *  Returns the next best move with a ply that is passed as an argument
 *
 *************************************************************************/

public class Minimax {

    // This method starts by calling minMax method and uses its return value to
    // Select the best move among it's children.
    public static Point decideNextMove(Board board, int ply) {
        Point point = new Point(0, 0);
        Board root = new Board(board);
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int best = minMax(root, ply, alpha, beta);
        for (int i = 0; i < root.children.size(); i++)
            if(root.children.get(i).value == best) {
                point.setRow(root.children.get(i).getLastChangedRow());
                point.setColumn(root.children.get(i).getLastChangedColumn());
                return point;
            }
        return point;
    }

    // This method has three different phases. The first one checks if we are checking
    // The last possible level and there are no moves after that. These are leaves.
    // If it is AI's turn, we call this method within itself for every children of current
    // board situation. We check if the returned value can beat the last best score.
    // If yes, then that is going to be AI's next move. If that move is also more than alpha,
    // We update our alpha too to prune based on that. If beta gets les than that move,
    // Then we don't need to go down that branch anymore.
    private static int minMax(Board board, int ply, int alpha, int beta) {
        board.addChildren();

        if (board.depth >= ply || board.children.isEmpty()) {
            board.value = board.getAIScore() - board.getHumanScore();
            return board.value;
        }

        if (board.getTurn() == Turn.AI) {
            int bestValue = Integer.MIN_VALUE;
            int value;
            for (int i = 0; i < board.children.size(); i++) {
                value = minMax(board.children.get(i), ply - 1, alpha, beta);
                if(value > bestValue)
                    bestValue = value;
                if(bestValue > alpha)
                    alpha = bestValue;
                if(beta <= alpha)
                    break;
            }
            board.value = bestValue;
            return bestValue;
        }

        else {
            int bestValue = Integer.MAX_VALUE;
            int value;
            for (int i = 0; i < board.children.size(); i++) {
                value = minMax(board.children.get(i), ply - 1, alpha, beta);
                if(value < bestValue)
                    bestValue = value;
                if(bestValue < beta)
                    beta = bestValue;
                if(beta <= alpha)
                    break;
            }
            board.value = bestValue;
            return bestValue;
        }
    }
}
