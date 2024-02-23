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

public class PlayerWhite extends Player {
    public PlayerWhite(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> currPieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Colour getColour() {
        return Colour.WHITE;
    }

    @Override
    public Player opponent() {
        return this.board.playerBlack();
    }

    @Override
    public Collection<Move> computeKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppLegalMoves) {

        List<Move> kingCastle = new ArrayList<>();

        if (this.king.firstMove() && !this.isCheck()) {

            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                Tile rookTile = this.board.getTile(63);

                if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove()) {

                    if (Player.attacksOnTile(61, oppLegalMoves).isEmpty() &&
                            Player.attacksOnTile(62, oppLegalMoves).isEmpty() &&
                            rookTile.getPiece().whichPiece().isRook()) {
                        kingCastle.add(new Move.KingSideCastle(this.board, this.king, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }

            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {

                Tile rookTile = this.board.getTile(56);

                if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove() &&
                        Player.attacksOnTile(58, oppLegalMoves).isEmpty() &&
                        Player.attacksOnTile(59, oppLegalMoves).isEmpty() &&
                        rookTile.getPiece().whichPiece().isRook()) {
                    kingCastle.add(new Move.QueenSideCastle(this.board, this.king, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }
        return Collections.unmodifiableList(kingCastle);
    }
}
