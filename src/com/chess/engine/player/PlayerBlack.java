package com.chess.engine.player;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayerBlack extends Player {
    public PlayerBlack(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> currPieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Colour getColour() {
        return Colour.BLACK;
    }

    @Override
    public Player opponent() {
        return this.board.playerWhite();
    }

    @Override
    public Collection<Move> computeKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppLegalMoves) {
        List<Move> kingCastle = new ArrayList<>();

        if (this.king.firstMove() && !this.isCheck()) {

            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove()) {

                    if (Player.attacksOnTile(5, oppLegalMoves).isEmpty() &&
                            Player.attacksOnTile(6, oppLegalMoves).isEmpty() &&
                            rookTile.getPiece().whichPiece().isRook()) {
                        kingCastle.add(new Move.KingSideCastle(this.board, this.king, 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }

            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {

                Tile rookTile = this.board.getTile(0);

                if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove() &&
                        Player.attacksOnTile(2, oppLegalMoves).isEmpty() &&
                        Player.attacksOnTile(3, oppLegalMoves).isEmpty() && rookTile.getPiece().whichPiece().isRook()) {
                    kingCastle.add(new Move.QueenSideCastle(this.board, this.king, 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }
        return Collections.unmodifiableList(kingCastle);
    }
}
