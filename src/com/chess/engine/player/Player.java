package com.chess.engine.player;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    boolean isCheck;
    Board board;
    King king;
    Collection<Move> legalMoves;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> oppMoves) {

        this.board = board;
        this.king = setKing();

        this.isCheck = !Player.attacksOnTile(this.king.getPieceLocation(), oppMoves).isEmpty();

        List<Move> temp = new ArrayList<>();
        temp.addAll(legalMoves);
        temp.addAll(computeKingCastle(legalMoves, oppMoves));
        this.legalMoves = temp;

    }

    public static Collection<Move> attacksOnTile(int pieceLocation, Collection<Move> moves) {

        List<Move> attackMoves = new ArrayList<>();

        for (Move move: moves) {

            if (pieceLocation == move.getFinalCoordinate()) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    private King setKing() {

        for (Piece piece: currPieces()) {

            if (piece.whichPiece().isKing()) {
                return (King) piece;
            }
        }

        throw new RuntimeException("Chess can't be played without the King!");
    }

    public boolean isLegalMove(Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public boolean isCheckMate() {
        return this.isCheck && canEscape();
    }

    private boolean canEscape() {

        for (Move move: this.legalMoves) {
            AfterMove afterMove = makeMove(move);

            if (afterMove.getMoveStatus().isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public boolean isStaleMate() {
        return !this.isCheck && canEscape();
    }

    public boolean isCastle() {
        return false;
    }

    public AfterMove makeMove (Move move) {

        if (!isLegalMove(move)) {
            return new AfterMove(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        Board afterBoard = move.execute();

        Collection<Move> kingAttacks = Player.attacksOnTile(afterBoard.currPlayer().opponent().king().getPieceLocation(), afterBoard.currPlayer().legalMoves());

        if (!kingAttacks.isEmpty()) {
            return new AfterMove(this.board, move, MoveStatus.PUTS_PLAYER_IN_CHECK);
        }

        return new AfterMove(afterBoard, move, MoveStatus.COMPLETED);
    }

    public Collection<Move> legalMoves() {
        return this.legalMoves;
    }

    public King king() {
        return this.king;
    }

    public abstract Collection<Piece> currPieces();
    public abstract Colour getColour();
    public abstract Player opponent();
    public abstract Collection<Move> computeKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppLegalMoves);
}
