package com.chess.engine.pieces;

import com.chess.engine.Colour;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    int pieceLocation;
    Colour pieceColour;
    public boolean firstMove;
    PieceType pieceType;
    int hashCode;

    Piece(PieceType pieceType, int pieceLocation, Colour pieceColour, boolean firstMove) {
        this.pieceLocation = pieceLocation;
        this.pieceColour = pieceColour;
        this.firstMove = firstMove;
        this.pieceType = pieceType;
        this.hashCode = calculateHashCode();
    }

    private int calculateHashCode() {
        int r = pieceType.hashCode();
        r = 31 * r + pieceColour.hashCode();
        r = 31 * r + pieceLocation;

        if (firstMove) {
            r = 31 * r + 1;
        }
        else {
            r = 31 * r;
        }

        return r;
    }

    public PieceType whichPiece() {
        return this.pieceType;
    }

    public boolean firstMove() {
        return this.firstMove;
    }
    public Colour getPieceColour() {
        return this.pieceColour;
    }
    public int getPieceLocation() {
        return this.pieceLocation;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Piece)) {
            return false;
        }

        Piece objPiece = (Piece) obj;

        return pieceLocation == objPiece.getPieceLocation() && pieceType == objPiece.whichPiece() && pieceColour == objPiece.getPieceColour() && firstMove == objPiece.firstMove();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public abstract Collection<Move> findLegalMoves(Board board);

    public abstract Piece movePiece(Move move);

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {

        PAWN(1, "P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, KNIGHT(3, "N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, BISHOP(3, "B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(5, "R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        }, QUEEN(9, "Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, KING(100, "K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        private final int pieceValue;

        PieceType(int pieceValue, String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        public int getPieceValue() {
            return this.pieceValue;
        }
    }
}
