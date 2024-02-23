package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    int tileCoordinate;

    static Map<Integer, EmptyTile> EMPTY_TILES = createEmptyTiles();

    static Map<Integer, EmptyTile> createEmptyTiles() {
        Map<Integer, EmptyTile> emptyMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            emptyMap.put(i, new EmptyTile(i));
        }

        return emptyMap;
    }

    public static Tile createTile(int tileCoordinate, Piece piece) {
        if (piece != null)
            return new NonEmptyTile(tileCoordinate, piece);
        else
            return EMPTY_TILES.get(tileCoordinate);
    }

    Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }

    public static class EmptyTile extends Tile {
        EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }


    public static class NonEmptyTile extends Tile {

        Piece pieceOnTile;
        NonEmptyTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString() {
            if (getPiece().getPieceColour().isBlack()) {
                return getPiece().toString().toLowerCase();
            }
            else {
                return getPiece().toString();
            }
        }
    }
}





