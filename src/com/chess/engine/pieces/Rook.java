package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;

public class Rook extends Piece {

    static int[] possibleMoves = {-8, -1, 1, 8};

    public Rook(Colour pieceColour, int pieceLocation) {
        super(PieceType.ROOK, pieceLocation, pieceColour, true);
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        Collection<Move> legalMoves = new ArrayList<>();

        for (int currPossibleMove : possibleMoves) {

            int possibleCoordinate = this.pieceLocation;

              while (possibleCoordinate < 64 && possibleCoordinate >=0) {

                if (firstColumn(possibleCoordinate, currPossibleMove) || eighthColumn(possibleCoordinate, currPossibleMove)) {
                    break;
                }

                possibleCoordinate += currPossibleMove;

                if (Board.isValidCoordinate(possibleCoordinate)) {

                    Tile possibleTile = board.getTile(possibleCoordinate);

                    if (!possibleTile.isTileOccupied()) {
                        legalMoves.add(new Move.ImpMove(board, this, possibleCoordinate));
                    } else {
                        Piece pieceAtLocation = possibleTile.getPiece();
                        Colour pieceColour = pieceAtLocation.getPieceColour();

                        if (this.pieceColour != pieceColour) {
                            legalMoves.add(new Move.ImpAttackMove(board, this, possibleCoordinate, pieceAtLocation));
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getPieceMoved().getPieceColour(), move.getFinalCoordinate());
    }

    public static boolean firstColumn(int position, int change) {

        return Board.FIRST_COLUMN[position] && (change == -1);
    }

    static boolean eighthColumn(int position, int change) {
        return Board.EIGHTH_COLUMN[position] && (change == 1);
    }
}
