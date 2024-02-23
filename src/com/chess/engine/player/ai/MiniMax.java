package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.AfterMove;

public class MiniMax implements MoveStrategy {

    EvaluateBoard evaluateBoard;
    int currDepth;

    public MiniMax(int currDepth) {
        this.evaluateBoard = new ChessBoardEvaluator();
        this.currDepth  = currDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        Move bestMove = null;

        int smallestValue = Integer.MAX_VALUE;
        int largestValue = Integer.MIN_VALUE;
        int currValue;

        System.out.println(board.currPlayer() + "Processing! Depth: " + this.currDepth);

        for (Move move : board.currPlayer().legalMoves()) {

            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {

                if (board.currPlayer().getColour().isWhite()) {
                    currValue = min(afterMove.getAfterBoard(), this.currDepth - 1);
                }
                else {
                    currValue = max(afterMove.getAfterBoard(), this.currDepth - 1);
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

    public int min(Board board, int depth) {

        if (depth == 0 || gameEnded(board)) {
            return this.evaluateBoard.evaluate(board, depth);
        }

        int smallestValue = Integer.MAX_VALUE;

        for (Move move : board.currPlayer().legalMoves()) {
            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {
                int currValue = max(afterMove.getAfterBoard(), depth - 1);

                if (currValue <= smallestValue) {
                    smallestValue = currValue;
                }
            }
        }

        return smallestValue;
    }

    public int max(Board board, int depth) {

        if (depth == 0 || gameEnded(board)) {
            return this.evaluateBoard.evaluate(board, depth);
        }

        int largestValue = Integer.MIN_VALUE;

        for (Move move : board.currPlayer().legalMoves()) {
            AfterMove afterMove = board.currPlayer().makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {
                int currValue = min(afterMove.getAfterBoard(), depth - 1);

                if (currValue >= largestValue) {
                    largestValue = currValue;
                }
            }
        }

        return largestValue;
    }

    public static boolean gameEnded(Board board) {
        return board.currPlayer().isCheckMate() || board.currPlayer().isStaleMate();
    }


}
