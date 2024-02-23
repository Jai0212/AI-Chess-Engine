package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends Piece {

    int[] possibleMoves = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(Colour pieceColour, int pieceLocation) {
        super(PieceType.KNIGHT, pieceLocation, pieceColour, true);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        Collection <Move> legalMoves = new ArrayList<>();

        for (int currPossibleMove: possibleMoves) {
            int possibleCoordinate = this.pieceLocation + currPossibleMove;

            if (Board.isValidCoordinate(possibleCoordinate)) {

                if (firstColumn(this.pieceLocation, currPossibleMove) || secondColumn(this.pieceLocation, currPossibleMove)
                || seventhColumn(this.pieceLocation, currPossibleMove) || eighthColumn(this.pieceLocation, currPossibleMove)) {
                    continue;
                }

                Tile possibleTile = board.getTile(possibleCoordinate);

                if (!possibleTile.isTileOccupied()) {
                    legalMoves.add(new Move.ImpMove(board, this, possibleCoordinate));
                }
                else {
                    Piece pieceAtLocation = possibleTile.getPiece();
                    Colour pieceColour = pieceAtLocation.getPieceColour();

                    if (this.pieceColour != pieceColour) {
                        legalMoves.add(new Move.ImpAttackMove(board, this, possibleCoordinate, pieceAtLocation));
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getPieceMoved().getPieceColour(), move.getFinalCoordinate());
    }

    static boolean firstColumn(int position, int change) {

        return Board.FIRST_COLUMN[position] && ((change == -17) || (change == -10) || (change == 6) || (change == 15));
    }

    static boolean secondColumn(int position, int change) {

        return Board.SECOND_COLUMN[position] && ((change == -10) || (change == 6));
    }

    static boolean seventhColumn(int position, int change) {

        return Board.SEVENTH_COLUMN[position] && ((change == 10) || (change == -6));
    }

    static boolean eighthColumn(int position, int change) {

        return Board.EIGHTH_COLUMN[position] && ((change == -15) || (change == -6) || (change == 10) || (change == 17));
    }

}
