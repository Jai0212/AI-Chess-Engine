package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.AfterMove;

public class MiniMax implements MoveStrategy {

    EvaluateBoard evaluateBoard;
    int currDepth;
    int alpha;
    int beta;

    public MiniMax(int currDepth) {
        this.evaluateBoard = new ChessBoardEvaluator();
        this.currDepth = currDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        Move bestMove = null;

        int smallestValue = Integer.MAX_VALUE;
        this.beta = Integer.MAX_VALUE;

        int largestValue = Integer.MIN_VALUE;
        this.alpha = Integer.MIN_VALUE;

        int currValue;

        System.out.println(board.currPlayer().getColour() + " Move Processing! Depth: " + this.currDepth);

        for (Move move : board.currPlayer().legalMoves()) {

            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {

                if (board.currPlayer().getColour().isWhite()) {
                    currValue = min(afterMove.getAfterBoard(), this.currDepth - 1, this.alpha, this.beta);
                }
                else {
                    currValue = max(afterMove.getAfterBoard(), this.currDepth - 1, this.alpha, this.beta);
                }

                if (board.currPlayer().getColour().isWhite() && currValue >= largestValue) {
                    largestValue = currValue;
                    bestMove = move;
                }
                else if (board.currPlayer().getColour().isBlack() && currValue <= smallestValue) {
                    smallestValue = currValue;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    public int min(Board board, int depth, int alpha, int beta) {

        if (depth == 0 || gameEnded(board)) {
            return this.evaluateBoard.evaluate(board, depth);
        }

        int smallestValue = Integer.MAX_VALUE;

        for (Move move : board.currPlayer().legalMoves()) {

            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {

                int currValue = max(afterMove.getAfterBoard(), depth - 1, alpha, beta);

                if (currValue <= smallestValue) {
                    smallestValue = currValue;
                }

                if (currValue <= alpha) {
                    return smallestValue;
                }

                beta = Math.min(beta, currValue);
            }
        }

        return smallestValue;
    }

    public int max(Board board, int depth, int alpha, int beta) {

        if (depth == 0 || gameEnded(board)) {
            return this.evaluateBoard.evaluate(board, depth);
        }

        int largestValue = Integer.MIN_VALUE;

        for (Move move : board.currPlayer().legalMoves()) {
            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {
                int currValue = min(afterMove.getAfterBoard(), depth - 1, alpha, beta);

                if (currValue >= largestValue) {
                    largestValue = currValue;
                }

                if (currValue >= beta) {
                    return largestValue;
                }

                alpha = Math.max(alpha, currValue);
            }
        }

        return largestValue;
    }

    public static boolean gameEnded(Board board) {
        return board.currPlayer().isCheckMate() || board.currPlayer().isStaleMate();
    }
}
