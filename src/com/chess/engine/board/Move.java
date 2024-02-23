package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    Board board;
    Piece pieceMoved;
    int finalCoordinate;
    boolean firstMove;

    public static Move INVALID_MOVE = new InvalidMove();

    Move(Board board, Piece pieceMoved, int finalCoordinate) {
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.finalCoordinate = finalCoordinate;
        this.firstMove = pieceMoved.firstMove();
    }

    Move(Board board, int finalCoordinate) {
        this.board = board;
        this.finalCoordinate = finalCoordinate;
        this.pieceMoved = null;
        this.firstMove = false;
    }

    @Override
    public int hashCode() {

        int r = 1;

        r = 31 * r + this.finalCoordinate;
        r = 31 * r + this.pieceMoved.hashCode();
        r = 31 * r + this.pieceMoved.getPieceLocation();

        return r;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Move)) {
            return false;
        }

        Move objMove = (Move) obj;
        return getFinalCoordinate() == objMove.getFinalCoordinate() && getPieceMoved().equals(objMove.getPieceMoved()) && getCurrCoordinate() == objMove.getCurrCoordinate();
    }

    public int getCurrCoordinate() {
        return this.getPieceMoved().getPieceLocation();
    }

    public int getFinalCoordinate() {
        return this.finalCoordinate;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastleMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board execute() {

        Board.Build build = new Board.Build();

        for (Piece piece : this.board.currPlayer().currPieces()) {
            if (!this.pieceMoved.equals(piece)) {
                build.putPiece(piece);
            }
        }

        for (Piece piece : this.board.currPlayer().opponent().currPieces()) {
            build.putPiece(piece);
        }

        build.putPiece(this.pieceMoved.movePiece(this));
        build.initNextMove(this.board.currPlayer().opponent().getColour());

        return build.build();
    }

    public static class ImpMove extends Move {

        public ImpMove(Board board, Piece pieceMoved, int finalCoordinate) {
            super(board, pieceMoved, finalCoordinate);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof ImpMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return pieceMoved.whichPiece().toString() + Board.getBoardLocationAtCoordinate(this.finalCoordinate);
        }
    }

    public static class ImpAttackMove extends AttackMove {

        public ImpAttackMove(Board board, Piece pieceMoved, int finalCoordinate, Piece attackedPiece) {
            super(board, pieceMoved, finalCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof ImpAttackMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return pieceMoved.whichPiece() + Board.getBoardLocationAtCoordinate(this.finalCoordinate);
        }
    }

    public Piece getPieceMoved() {
        return this.pieceMoved;
    }

    public static class AttackMove extends Move {

        Piece attackedPiece;

        public AttackMove(Board board, Piece pieceMoves, int finalCoordinate, Piece attackedPiece) {
            super(board, pieceMoves, finalCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof AttackMove)) {
                return false;
            }

            AttackMove objAttackMove = (AttackMove) obj;
            return super.equals(objAttackMove) && getAttackedPiece().equals(objAttackMove.getAttackedPiece());
        }
    }

    public static class PawnMove extends Move {

        public PawnMove(Board board, Piece pieceMoved, int finalCoordinate) {
            super(board, pieceMoved, finalCoordinate);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return Board.getBoardLocationAtCoordinate(this.finalCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece pieceMoved, int finalCoordinate, Piece attackedPiece) {
            super(board, pieceMoved, finalCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnAttackMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return Board.getBoardLocationAtCoordinate(this.pieceMoved.getPieceLocation()).charAt(0) + "x" +
                    Board.getBoardLocationAtCoordinate(this.finalCoordinate);
        }
    }

    public static class PawnEnPassant extends PawnAttackMove {

        public PawnEnPassant(Board board, Piece pieceMoved, int finalCoordinate, Piece attackedPiece) {
            super(board, pieceMoved, finalCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnEnPassant && super.equals(obj);
        }

        @Override
        public Board execute() {

            Board.Build build = new Board.Build();

            for (Piece piece : this.board.currPlayer().currPieces()) {

                if (!this.pieceMoved.equals(piece)) {
                    build.putPiece(piece);
                }
            }

            for (Piece piece : this.board.currPlayer().opponent().currPieces()) {

                if (!piece.equals(this.getAttackedPiece())) {
                    build.putPiece(piece);
                }
            }

            build.putPiece(this.pieceMoved.movePiece(this));
            build.initNextMove(this.board.currPlayer().opponent().getColour());

            return build.build();
        }
    }

    public static class PawnJump extends Move {

        public PawnJump(Board board, Piece pieceMoved, int finalCoordinate) {
            super(board, pieceMoved, finalCoordinate);
        }

        @Override
        public Board execute() {

            Board.Build build = new Board.Build();

            for (Piece piece : this.board.currPlayer().currPieces()) {
                if (!this.pieceMoved.equals(piece)) {
                    build.putPiece(piece);
                }
            }

            for (Piece piece : this.board.currPlayer().opponent().currPieces()) {
                build.putPiece(piece);
            }

            Pawn movedPawn = (Pawn) this.pieceMoved.movePiece(this);

            build.putPiece(movedPawn);
            build.putEnPassant(movedPawn);
            build.initNextMove(this.board.currPlayer().opponent().getColour());
            return build.build();
        }

        @Override
        public String toString() {
            return Board.getBoardLocationAtCoordinate(this.finalCoordinate);
        }
    }

    public static class PawnPromotion extends Move {

        Move specialMove;
        Pawn promotedPawn;

        public PawnPromotion(Move specialMove) {
            super(specialMove.getBoard(), specialMove.getPieceMoved(), specialMove.getFinalCoordinate());
            this.specialMove = specialMove;
            this.promotedPawn = (Pawn) specialMove.getPieceMoved();
        }

        @Override
        public boolean isAttack() {
            return this.specialMove.isAttack();
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int hashCode() {
            return specialMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnPromotion && super.equals(obj);
        }

        @Override
        public Piece getAttackedPiece() {
            return this.specialMove.getAttackedPiece();
        }

        @Override
        public Board execute() {

            Board boardAfterPawnMove = this.specialMove.execute();

            Board.Build build = new Board.Build();

            for (Piece piece : boardAfterPawnMove.currPlayer().currPieces()) {

                if (!this.promotedPawn.equals(piece)) {
                    build.putPiece(piece);
                }
            }

            for (Piece piece : boardAfterPawnMove.currPlayer().opponent().currPieces()) {
                build.putPiece(piece);
            }

            build.putPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            build.initNextMove(boardAfterPawnMove.currPlayer().getColour());

            return build.build();
        }
    }

    public static abstract class CastleMove extends Move {

        Rook castleRook;
        int castleRookStartLocation;
        int castleRookFinalLocation;

        public CastleMove(Board board, Piece pieceMoved, int finalCoordinate, Rook castleRook, int castleRookStartLocation, int castleRookFinalLocation) {
            super(board, pieceMoved, finalCoordinate);
            this.castleRook = castleRook;
            this.castleRookStartLocation = castleRookStartLocation;
            this.castleRookFinalLocation = castleRookFinalLocation;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastleMove() {
            return true;
        }

        @Override
        public Board execute() {

            Board.Build build = new Board.Build();

            for (Piece piece : this.board.currPlayer().currPieces()) {
                if (!this.pieceMoved.equals(piece) && !this.castleRook.equals(piece)) {
                    build.putPiece(piece);
                }
            }

            for (Piece piece : this.board.currPlayer().opponent().currPieces()) {
                build.putPiece(piece);
            }

            build.putPiece(this.pieceMoved.movePiece(this));
            build.putPiece(new Rook(this.castleRook.getPieceColour(), this.castleRookFinalLocation));
            build.initNextMove(this.board.currPlayer().opponent().getColour());

            return build.build();
        }

        @Override
        public int hashCode() {
            int r = super.hashCode();

            r = 31 * r + this.castleRook.hashCode();
            r = 31 * r + this.castleRookFinalLocation;

            return r;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CastleMove)) {
                return false;
            }

            CastleMove objCastleMove = (CastleMove) obj;
            return super.equals(objCastleMove) && this.castleRook.equals(objCastleMove.getCastleRook());
        }
    }

    public static class KingSideCastle extends CastleMove {

        public KingSideCastle(Board board, Piece pieceMoved, int finalCoordinate, Rook castleRook, int castleRookStartLocation, int castleRookFinalLocation) {
            super(board, pieceMoved, finalCoordinate, castleRook, castleRookStartLocation, castleRookFinalLocation);
        }

        @Override
        public String toString() {
            return "O-O";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof KingSideCastle && super.equals(obj);
        }
    }

    public static class QueenSideCastle extends CastleMove {

        public QueenSideCastle(Board board, Piece pieceMoved, int finalCoordinate, Rook castleRook, int castleRookStartLocation, int castleRookFinalLocation) {
            super(board, pieceMoved, finalCoordinate, castleRook, castleRookStartLocation, castleRookFinalLocation);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof QueenSideCastle && super.equals(obj);
        }
    }

    public static class InvalidMove extends Move {

        public InvalidMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Invalid Move!");
        }

        @Override
        public int getCurrCoordinate() {
            return -1;
        }
    }

    public static class AllMoves {

        public AllMoves() {
            throw new RuntimeException("Should not be instantiated!");
        }

        public static Move createMove(Board board, int currCoordinate, int finalCoordinate) {

            for (Move move : board.allLegalMoves()) {

                if (move.getCurrCoordinate() == currCoordinate && move.getFinalCoordinate() == finalCoordinate) {
                    return move;
                }
            }

            return INVALID_MOVE;
        }
    }
}
