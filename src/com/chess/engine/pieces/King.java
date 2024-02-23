package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;

public class King extends Piece {

    static int[] possibleMoves = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(Colour pieceColour, int pieceLocation) {
        super(PieceType.KING, pieceLocation, pieceColour, true);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        Collection<Move> legalMoves = new ArrayList<>();

        for (int currPossibleMove: possibleMoves) {

            int possibleCoordinate = this.pieceLocation + currPossibleMove;

            if (firstColumn(this.pieceLocation, currPossibleMove) || eighthColumn(this.pieceLocation, currPossibleMove)) {
                continue;
            }

            if (Board.isValidCoordinate(possibleCoordinate)) {

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
    public King movePiece(Move move) {
        return new King(move.getPieceMoved().getPieceColour(), move.getFinalCoordinate());
    }

    static boolean firstColumn(int position, int change) {

        return Board.FIRST_COLUMN[position] && ((change == -9) || (change == -1) || (change == 7));
    }

    static boolean eighthColumn(int position, int change) {

        return Board.EIGHTH_COLUMN[position] && ((change == 9) || (change == 1) || (change == -7));
    }
}
