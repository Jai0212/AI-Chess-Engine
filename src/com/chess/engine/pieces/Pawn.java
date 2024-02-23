package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends Piece {

    static int[] possibleMoves = {7, 8, 9, 16};

    public Pawn(Colour pieceColour, int pieceLocation) {
        super(PieceType.PAWN, pieceLocation, pieceColour, true);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        Collection<Move> legalMoves = new ArrayList<>();

        for (int currPossibleMove: possibleMoves) {

            int possibleCoordinate = this.pieceLocation + (this.pieceColour.directionColour() * currPossibleMove);

            if (!Board.isValidCoordinate(possibleCoordinate)) {
                continue;
            }

            if (currPossibleMove == 8 && !board.getTile(possibleCoordinate).isTileOccupied()) {

                if (this.pieceColour.isPawnPromotionTile(possibleCoordinate)) {
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, possibleCoordinate)));
                }
                else {
                    legalMoves.add(new Move.PawnMove(board, this, possibleCoordinate));
                }
            }
            else if (currPossibleMove == 16 && this.firstMove()
                    && ((Board.SEVENTH_RANK[this.pieceLocation] && this.pieceColour.isBlack())
                    || (Board.SECOND_RANK[this.pieceLocation] && this.pieceColour.isWhite()))) {

                int behindPossibleCoordinate = this.pieceLocation + (this.getPieceColour().directionColour() * 8);

                if (!board.getTile(behindPossibleCoordinate).isTileOccupied() && !board.getTile(possibleCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.PawnJump(board, this, possibleCoordinate));
                }
            }
            else if (currPossibleMove == 7 && !((Board.EIGHTH_COLUMN[this.pieceLocation] && this.pieceColour.isWhite())
                    || (Board.FIRST_COLUMN[this.pieceLocation] && this.pieceColour.isBlack()))) {

                if (board.getTile(possibleCoordinate).isTileOccupied()) {
                    Piece pieceAtCoordinate = board.getTile(possibleCoordinate).getPiece();

                    if (this.pieceColour != pieceAtCoordinate.getPieceColour()) {

                        if (this.pieceColour.isPawnPromotionTile(possibleCoordinate)) {
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, possibleCoordinate, pieceAtCoordinate)));
                        }
                        else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, possibleCoordinate, pieceAtCoordinate));
                        }
                    }
                }

                else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPieceLocation() == (this.pieceLocation + (this.pieceColour.oppositeDirectionColour()))) {

                        Piece pieceAtCoordinate = board.getEnPassantPawn();

                        if (this.pieceColour != pieceAtCoordinate.getPieceColour()) {
                            legalMoves.add(new Move.PawnEnPassant(board, this, possibleCoordinate, pieceAtCoordinate));
                        }
                    }
                }
            }
            else if (currPossibleMove == 9 && !((Board.FIRST_COLUMN[this.pieceLocation] && this.pieceColour.isWhite())
                    || (Board.EIGHTH_COLUMN[this.pieceLocation] && this.pieceColour.isBlack()))) {

                if (board.getTile(possibleCoordinate).isTileOccupied()) {
                    Piece pieceAtCoordinate = board.getTile(possibleCoordinate).getPiece();

                    if (this.pieceColour != pieceAtCoordinate.getPieceColour()) {

                        if (this.pieceColour.isPawnPromotionTile(possibleCoordinate)) {
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, possibleCoordinate, pieceAtCoordinate)));
                        }
                        else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, possibleCoordinate, pieceAtCoordinate));
                        }
                    }
                }

                else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPieceLocation() == (this.pieceLocation - (this.pieceColour.oppositeDirectionColour()))) {

                        Piece pieceAtCoordinate = board.getEnPassantPawn();

                        if (this.pieceColour != pieceAtCoordinate.getPieceColour()) {
                            legalMoves.add(new Move.PawnEnPassant(board, this, possibleCoordinate, pieceAtCoordinate));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getPieceMoved().getPieceColour(), move.getFinalCoordinate());
    }

    public Piece getPromotionPiece() {
        return new Queen(this.pieceColour, this.pieceLocation, false);
    }
}
