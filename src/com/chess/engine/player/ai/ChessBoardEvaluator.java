package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public class ChessBoardEvaluator implements EvaluateBoard {

    @Override
    public int evaluate(Board board, int depth) {
        return computeScoreOfPlayer(board.playerWhite(), depth) - computeScoreOfPlayer(board.playerBlack(), depth);
    }

    private int computeScoreOfPlayer(Player player, int depth) {

        return pieceValue(player) + numberOfLegalMoves(player) + check(player) + checkmate(player, depth) + castle(player);
    }

    private static int castle(Player player) {
        if (player.isCastle()) {
            return 60;
        }
        else {
            return 0;
        }
    }

    private static int checkmate(Player player, int depth) {
        if (player.opponent().isCheckMate()) {
            return 10000 * scoreAtDepth(depth);
        }
        else {
            return 0;
        }
    }

    private static int scoreAtDepth(int depth) {
        if (depth == 0) {
            return 1;
        }
        else {
            return 100 * depth;
        }
    }

    private static int check(Player player) {
        if (player.opponent().isCheck()) {
            return 50;
        }
        else {
            return 0;
        }
    }

    private static int pieceValue(Player player) {

        int pieceValue = 0;

        for (Piece piece : player.currPieces()) {
            pieceValue += piece.getPieceValue();
        }

        return pieceValue * 100;
    }

    private static int numberOfLegalMoves(Player player) {
        return player.legalMoves().size();
    }
}
