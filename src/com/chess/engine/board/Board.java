package com.chess.engine.board;

import com.chess.engine.Colour;
import com.chess.engine.pieces.*;
import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerBlack;
import com.chess.engine.player.PlayerWhite;

import java.util.*;

public class Board {

    List<Tile> chessBoard;
    Collection<Piece> whitePieces;
    Collection<Piece> blackPieces;

    Player currPlayer;
    PlayerWhite playerWhite;
    PlayerBlack playerBlack;
    Pawn enPassantPawn;

    public static final boolean[] FIRST_COLUMN = initializeColumn(0);
    public static final boolean[] SECOND_COLUMN = initializeColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initializeColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initializeColumn(7);

    public static final boolean[] EIGHTH_RANK = initializeRow(0);
    public static final boolean[] SEVENTH_RANK = initializeRow(8);
    public static final boolean[] SIXTH_RANK = initializeRow(16);
    public static final boolean[] FIFTH_RANK = initializeRow(24);
    public static final boolean[] FOURTH_RANK = initializeRow(32);
    public static final boolean[] THIRD_RANK = initializeRow(40);
    public static final boolean[] SECOND_RANK = initializeRow(48);
    public static final boolean[] FIRST_RANK = initializeRow(56);

    public static final String[] CHESS_NOTATION = initChessNotation();

    private static String[] initChessNotation() {

        String[] chessBoardRepresentationInNotation = new String[64];
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};

        int count = 0;
        for(int num : numbers) {
            for (String letter : letters) {
                chessBoardRepresentationInNotation[count] = letter + num;
                count++;
            }
        }

        return chessBoardRepresentationInNotation;

    }

    public Board(Build build) {
        this.chessBoard = createChessBoard(build);
        this.whitePieces = calculateCurrPieces(this.chessBoard, Colour.WHITE);
        this.blackPieces = calculateCurrPieces(this.chessBoard, Colour.BLACK);
        this.enPassantPawn = build.enPassantPawn;

        Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);

        this.playerWhite = new PlayerWhite(this, whiteLegalMoves, blackLegalMoves);
        this.playerBlack = new PlayerBlack(this, whiteLegalMoves, blackLegalMoves);

        this.currPlayer = build.nextMove.selectPlayer(this.playerWhite, this.playerBlack);
    }

    public static boolean[] initializeRow(int rowNum) {

        boolean[] row = new boolean[64];

        do {
            row[rowNum] = true;
            rowNum ++;
        }
        while (rowNum % 8 != 0);

        return row;
    }

    public static boolean[] initializeColumn(int columnNumber) {
        boolean[] columns = new boolean[64];

        for (int i = columnNumber; i < 64; i += 8) {
            columns[i] = true;
        }
        return Arrays.copyOf(columns, columns.length);
    }

    public static boolean isValidCoordinate(int coordinate) {
        return coordinate < 64 && coordinate >= 0;
    }

    public static String getBoardLocationAtCoordinate(int coordinate) {
        return CHESS_NOTATION[coordinate];
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Player playerWhite() {
        return this.playerWhite;
    }

    public Player playerBlack() {
        return this.playerBlack;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            String stringTile = this.chessBoard.get(i).toString();
            builder.append(String.format("%3s", stringTile));
            if ((i + 1) % 8 == 0) {
                builder.append(("\n"));
            }
        }
        return builder.toString();
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {

        List<Move> legalMoves = new ArrayList<>();

        for (Piece piece: pieces) {
            legalMoves.addAll(piece.findLegalMoves(this));
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static Collection<Piece> calculateCurrPieces(List<Tile> chessBoard, Colour colour) {

        List<Piece> currPieces = new ArrayList<>();

        for (Tile tile: chessBoard) {
            if (tile.isTileOccupied()) {
                Piece piece = tile.getPiece();

                if (piece.getPieceColour() == colour) {
                    currPieces.add(piece);
                }
            }
        }

        return Collections.unmodifiableList(currPieces);
    }

    private static List<Tile> createChessBoard(Build build) {

        Tile[] tiles = new Tile[64];

        for (int i = 0; i < 64; i++) {
            tiles[i] = Tile.createTile(i, build.buildBoard.get(i));
        }

        return Arrays.asList(tiles);
    }

    public static Board createInitialChessBoard() {
        Build build = new Build();

        build.putPiece(new Rook(Colour.BLACK, 0));
        build.putPiece(new Knight(Colour.BLACK, 1));
        build.putPiece(new Bishop(Colour.BLACK, 2));
        build.putPiece(new Queen(Colour.BLACK, 3));
        build.putPiece(new King(Colour.BLACK, 4));
        build.putPiece(new Bishop(Colour.BLACK, 5));
        build.putPiece(new Knight(Colour.BLACK, 6));
        build.putPiece(new Rook(Colour.BLACK, 7));
        build.putPiece(new Pawn(Colour.BLACK, 8));
        build.putPiece(new Pawn(Colour.BLACK, 9));
        build.putPiece(new Pawn(Colour.BLACK, 10));
        build.putPiece(new Pawn(Colour.BLACK, 11));
        build.putPiece(new Pawn(Colour.BLACK, 12));
        build.putPiece(new Pawn(Colour.BLACK, 13));
        build.putPiece(new Pawn(Colour.BLACK, 14));
        build.putPiece(new Pawn(Colour.BLACK, 15));

        build.putPiece(new Pawn(Colour.WHITE, 48));
        build.putPiece(new Pawn(Colour.WHITE, 49));
        build.putPiece(new Pawn(Colour.WHITE, 50));
        build.putPiece(new Pawn(Colour.WHITE, 51));
        build.putPiece(new Pawn(Colour.WHITE, 52));
        build.putPiece(new Pawn(Colour.WHITE, 53));
        build.putPiece(new Pawn(Colour.WHITE, 54));
        build.putPiece(new Pawn(Colour.WHITE, 55));
        build.putPiece(new Rook(Colour.WHITE, 56));
        build.putPiece(new Knight(Colour.WHITE, 57));
        build.putPiece(new Bishop(Colour.WHITE, 58));
        build.putPiece(new Queen(Colour.WHITE, 59));
        build.putPiece(new King(Colour.WHITE, 60));
        build.putPiece(new Bishop(Colour.WHITE, 61));
        build.putPiece(new Knight(Colour.WHITE, 62));
        build.putPiece(new Rook(Colour.WHITE, 63));


        build.initNextMove(Colour.WHITE);

        return build.build();
    }
    public Tile getTile(int tileCoordinate) {
        return chessBoard.get(tileCoordinate);
    }

    public Player currPlayer() {
        return this.currPlayer;
    }

    public Iterable<Move> allLegalMoves() {
        List<Move> temp = new ArrayList<>();

        temp.addAll(this.playerWhite.legalMoves());
        temp.addAll(this.playerBlack.legalMoves());

        return Collections.unmodifiableList(temp);
    }

    public static class Build {

        Map<Integer, Piece> buildBoard;
        Colour nextMove;
        Pawn enPassantPawn;

        public Build() {
            this.buildBoard = new HashMap<>();
        }

        public void putPiece(Piece piece) {
            this.buildBoard.put(piece.getPieceLocation(), piece);
        }

        public void initNextMove(Colour nextMove) {
            this.nextMove = nextMove;
        }

        public Board build() {
            return new Board(this);
        }

        public void putEnPassant(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
