package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.AfterMove;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {

    JFrame chessFrame;
    LogPanel logPanel;
    CapturedPiecesPanel capturedPiecesPanel;
    static Dimension outerDimension = new Dimension(800, 700);
    static Dimension boardPanelDimension = new Dimension(400, 350);
    static Dimension tilePanelDimension = new Dimension(10, 10);

    Tile initialTile;
    Tile finalTile;
    Piece finalMovedPiece;

    BoardPanel boardPanel;
    LogMoves logMoves;

    ChessGameInitialization chessGameInitialization;

    Color lightColouredTile = Color.decode("#784F3A");
    Color darkColouredTile = Color.decode("#FFDDB5");
    String pathForDot;

    Board chessBoard;
    BoardDirection boardDirection;
    boolean highlight;

    Move computerMove;

    static String pieceImagePath = "ImagesChess/ChessPiecesImages/";

    public static Table instanceOfTable = new Table();

    private Table() {

        this.chessFrame = new JFrame("Chess by Jai Joshi");

        this.chessFrame.setLayout(new BorderLayout());

        JMenuBar tableMenuBar = createMenuBar();

        this.chessFrame.setJMenuBar(tableMenuBar);

        this.chessFrame.setSize(outerDimension);

        this.chessBoard = Board.createInitialChessBoard();

        this.logPanel = new LogPanel();
        this.capturedPiecesPanel = new CapturedPiecesPanel();

        this.boardPanel = new BoardPanel();
        this.logMoves = new LogMoves();

        this.addObserver(new ChessObserver());

        this.chessGameInitialization = new ChessGameInitialization(this.chessFrame, true);

        this.boardDirection = BoardDirection.NOTFLIPPED;
        this.highlight = true;

        this.chessFrame.add(this.capturedPiecesPanel, BorderLayout.WEST);
        this.chessFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.chessFrame.add(this.logPanel, BorderLayout.EAST);

        this.chessFrame.setVisible(true);

        this.pathForDot = "orange_dot";
        this.lightColouredTile = Color.decode("#784F3A");
        this.darkColouredTile = Color.decode("#FFDDB5");

    }

    public static Table get() {
        return instanceOfTable;
    }

    public void show() {

        Table.get().getLogMoves().clearLog();
        Table.get().getLogPanel().redo(chessBoard, Table.get().getLogMoves());
        Table.get().getCapturedPiecesPanel().redo(Table.get().getLogMoves());
        Table.get().getBoardPanel().drawBoard(Table.get().getChessBoard());
    }

    public ChessGameInitialization getChessGameInitialization() {
        return this.chessGameInitialization;
    }

    public Board getChessBoard() {
        return this.chessBoard;
    }

    private JMenuBar createMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenuBar());
        tableMenuBar.add(createPreferencesMenuBar());
        tableMenuBar.add(createCustomizeMenuBar());
        tableMenuBar.add(createOptionsMenuBar());
        return tableMenuBar;
    }

    private JMenu createOptionsMenuBar() {

        JMenu optionsMenu = new JMenu("Options");

        JMenuItem gameSetupMenuItem = new JMenuItem("Setup Game");
        gameSetupMenuItem.addActionListener(e -> {
            Table.get().getChessGameInitialization().promptUser();
            Table.get().initUpdate(Table.get().getChessGameInitialization());
        });

        optionsMenu.add(gameSetupMenuItem);

        return optionsMenu;
    }

    public void initUpdate(ChessGameInitialization chessGameInitialization) {
        setChanged();
        notifyObservers(chessGameInitialization);
    }

    public static class ChessObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {

            if (Table.get().getChessGameInitialization().isAIPlayer(Table.get().getChessBoard().currPlayer())
                && !Table.get().getChessBoard().currPlayer().isCheckMate() && !Table.get().getChessBoard().currPlayer().isStaleMate()) {

                AIProcessor processor = new AIProcessor();
                processor.execute();
            }

            if (Table.get().getChessBoard().currPlayer().isCheckMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over! " + Table.get().getChessBoard().currPlayer().getColour() + " Lost!", "Game Over!",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (Table.get().getChessBoard().currPlayer().isStaleMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over! " + Table.get().getChessBoard().currPlayer().getColour() + " is in Stalemate!", "Game Over!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static class AIProcessor extends SwingWorker <Move, String> {

        AIProcessor() {

        }

        @Override
        protected Move doInBackground() {
            MoveStrategy miniMax = new MiniMax(Table.get().getChessGameInitialization().getDepthOfAI());
            return miniMax.execute(Table.get().getChessBoard());
        }

        @Override
        public void done() {

            try {
                Move bestMove = get();

                Table.get().updateComputerMove(bestMove);
                Table.get().updateChessBoard(Table.get().getChessBoard().currPlayer().makeMove(bestMove).getAfterBoard());
                Table.get().getLogMoves().addMoveToLog(bestMove);
                Table.get().getLogPanel().redo(Table.get().getChessBoard(), Table.get().getLogMoves());
                Table.get().getCapturedPiecesPanel().redo(Table.get().getLogMoves());
                Table.get().getBoardPanel().drawBoard(Table.get().getChessBoard());
                Table.get().updateMoveMade(PlayerType.COMPUTER);

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateComputerMove(Move move) {
        this.computerMove = move;
    }

    public void updateChessBoard(Board board) {
        this.chessBoard = board;
    }

    public LogMoves getLogMoves() {
        return this.logMoves;
    }

    public LogPanel getLogPanel() {
        return this.logPanel;
    }

    public CapturedPiecesPanel getCapturedPiecesPanel() {
        return this.capturedPiecesPanel;
    }

    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    public void updateMoveMade(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private JMenu createFileMenuBar() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitGame = new JMenuItem("Exit Game");
        exitGame.addActionListener(e -> System.exit(0));

        fileMenu.add(exitGame);

        return fileMenu;
    }

    private JMenu createCustomizeMenuBar() {

        JMenu customizeMenu = new JMenu("Customize");

        ButtonGroup dotColours = new ButtonGroup();

        JRadioButtonMenuItem orange_dot = new JRadioButtonMenuItem("Orange Dot", true);
        JRadioButtonMenuItem blue_dot = new JRadioButtonMenuItem("Blue Dot", false);
        JRadioButtonMenuItem gray_dot = new JRadioButtonMenuItem("Gray Dot", false);
        JRadioButtonMenuItem yellow_dot = new JRadioButtonMenuItem("Yellow Dot", false);

        dotColours.add(orange_dot);
        dotColours.add(blue_dot);
        dotColours.add(gray_dot);
        dotColours.add(yellow_dot);

        orange_dot.addActionListener(e -> pathForDot = "orange_dot");

        blue_dot.addActionListener(e -> pathForDot = "blue_dot");

        gray_dot.addActionListener(e -> pathForDot = "gray_dot");

        yellow_dot.addActionListener(e -> pathForDot = "yellow_dot");

        customizeMenu.add(orange_dot);
        customizeMenu.add(blue_dot);
        customizeMenu.add(gray_dot);
        customizeMenu.add(yellow_dot);


        customizeMenu.addSeparator();

        ButtonGroup tileCustomColours = new ButtonGroup();

        JRadioButtonMenuItem mapleAndWood = new JRadioButtonMenuItem("Maple and Wood", true);
        JRadioButtonMenuItem lightAndDarkBrown = new JRadioButtonMenuItem("Light and Dark Brown", false);
        JRadioButtonMenuItem greenAndBlue = new JRadioButtonMenuItem("Green and Blue", false);
        JRadioButtonMenuItem redAndYellow = new JRadioButtonMenuItem("Red and Yellow", false);

        tileCustomColours.add(mapleAndWood);
        tileCustomColours.add(lightAndDarkBrown);
        tileCustomColours.add(greenAndBlue);
        tileCustomColours.add(redAndYellow);

        mapleAndWood.addActionListener(e -> {
            lightColouredTile = Color.decode("#784F3A");
            darkColouredTile = Color.decode("#FFDDB5");
        });

        lightAndDarkBrown.addActionListener(e -> {
            lightColouredTile = Color.decode("#D2B48C");
            darkColouredTile = Color.decode("#8B4513");
        });

        greenAndBlue.addActionListener(e -> {
            lightColouredTile = Color.decode("#2E8B57");
            darkColouredTile = Color.decode("#41648B");
        });

        redAndYellow.addActionListener(e -> {
            lightColouredTile = Color.decode("#FF0000");
            darkColouredTile = Color.decode("#FFFF00");
        });

        customizeMenu.add(mapleAndWood);
        customizeMenu.add(lightAndDarkBrown);
        customizeMenu.add(greenAndBlue);
        customizeMenu.add(redAndYellow);

        return customizeMenu;
    }

    private JMenu createPreferencesMenuBar() {

        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoardMenuItems = new JMenuItem("Flip Board");

        flipBoardMenuItems.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });

        preferencesMenu.add(flipBoardMenuItems);

        preferencesMenu.addSeparator();

        JCheckBoxMenuItem moveHighlighter = new JCheckBoxMenuItem("Highlight Legal Moves", true);

        moveHighlighter.addActionListener(e -> highlight = moveHighlighter.isSelected());

        preferencesMenu.add(moveHighlighter);

        return preferencesMenu;
    }

    public enum PlayerType {
        HUMAN, COMPUTER
    }

    public enum BoardDirection {

        NOTFLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        }, FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return NOTFLIPPED;
            }
        };

        abstract java.util.List<TilePanel> traverse(java.util.List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }
    public class BoardPanel extends JPanel {

        java.util.List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < 64; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(boardPanelDimension);
            validate();
        }

        public void drawBoard(Board chessBoard) {
            removeAll();

            for (TilePanel tilePanel: boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(chessBoard);
                add(tilePanel);
            }

            validate();
            repaint();
        }
    }

    public static class LogMoves {

        List <Move> movesInLog;

        LogMoves() {
            this.movesInLog = new ArrayList<>();
        }

        public List<Move> getMovesInLog() {
            return this.movesInLog;
        }

        public void addMoveToLog(Move move) {
            this.movesInLog.add(move);
        }

        public int sizeOfLog() {
            return this.movesInLog.size();
        }

        public void clearLog() {
            this.movesInLog.clear();
        }
    }

    class TilePanel extends JPanel {

        int tileCoordinate;

        TilePanel(BoardPanel boardPanel, int tileCoordinate) {
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(tilePanelDimension);

            tileColour();
            assignTilePieceImage(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (isRightMouseButton(e)) {

                        initialTile = null;
                        finalTile = null;
                        finalMovedPiece = null;

                    }
                    else if (isLeftMouseButton(e)) {

                        if (!chessGameInitialization.isAIPlayer(chessBoard.currPlayer())) {

                            if (initialTile == null) {
                                initialTile = chessBoard.getTile(tileCoordinate);
                                finalMovedPiece = initialTile.getPiece();

                                if (finalMovedPiece == null) {
                                    initialTile = null;
                                }
                            } else {
                                finalTile = chessBoard.getTile(tileCoordinate);
                                Move move = Move.AllMoves.createMove(chessBoard, initialTile.getTileCoordinate(), finalTile.getTileCoordinate());

                                AfterMove afterMove = chessBoard.currPlayer().makeMove(move);

                                if (afterMove.getMoveStatus().isCompleted()) {
                                    chessBoard = afterMove.getAfterBoard();
                                    logMoves.addMoveToLog(move);

                                    if (move.isCastleMove()) {
                                        chessBoard.currPlayer().opponent().king().firstMove = false;
                                    }

                                }

                                initialTile = null;
                                finalTile = null;
                                finalMovedPiece = null;
                            }
                        }
                        SwingUtilities.invokeLater(() -> {
                            logPanel.redo(chessBoard, logMoves);
                            capturedPiecesPanel.redo(logMoves);

                            if (chessGameInitialization.isAIPlayer(chessBoard.currPlayer())) {
                                Table.get().updateMoveMade(PlayerType.HUMAN);
                            }

                            boardPanel.drawBoard(chessBoard);
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            validate();
        }

        public void drawTile(Board board) {
            tileColour();
            assignTilePieceImage(board);
            highlight(board);
            validate();
            repaint();
        }

        private void assignTilePieceImage(Board board) {
            this.removeAll();

            if (board.getTile(this.tileCoordinate).isTileOccupied()) {

                try {
                    BufferedImage img = ImageIO.read(new File(pieceImagePath + board.getTile(this.tileCoordinate).getPiece().getPieceColour().toString().charAt(0) +
                            board.getTile(this.tileCoordinate).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(img)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlight(Board board) {
            if (highlight) {
                for (Move move: legalMovesOfPiece(board)) {
                    if (move.getFinalCoordinate() == this.tileCoordinate) {


                        if (!initialTile.getPiece().whichPiece().isKing() && move.isCastleMove()) {
                            continue;
                        }
                        if (initialTile.getPiece().whichPiece().isKing() && move.isCastleMove() && !initialTile.getPiece().firstMove()) {
                            continue;
                        }

                        AfterMove afterMove = chessBoard.currPlayer().makeMove(move);

                        if (!afterMove.getMoveStatus().isCompleted()) {
                            continue;
                        }

                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("ImagesChess/ColouredDotImages/" + pathForDot + ".png")))));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> legalMovesOfPiece(Board board) {

            if (finalMovedPiece != null && finalMovedPiece.getPieceColour() == board.currPlayer().getColour()) {

                Collection<Move> temp = new ArrayList<>();

                temp.addAll(finalMovedPiece.findLegalMoves(board));
                temp.addAll(chessBoard.currPlayer().computeKingCastle(chessBoard.currPlayer().legalMoves(),
                        chessBoard.currPlayer().opponent().legalMoves()));

                return temp;
            }
            return Collections.emptyList();

        }

        public void tileColour() {

            if (Board.EIGHTH_RANK[this.tileCoordinate] || Board.SIXTH_RANK[this.tileCoordinate] || Board.FOURTH_RANK[this.tileCoordinate] || Board.SECOND_RANK[this.tileCoordinate]) {
                if (this.tileCoordinate % 2 == 0) {
                    setBackground(lightColouredTile);
                }
                else {
                    setBackground(darkColouredTile);
                }
            }
            else if (Board.SEVENTH_RANK[this.tileCoordinate] || Board.FIFTH_RANK[this.tileCoordinate] || Board.THIRD_RANK[this.tileCoordinate] || Board.FIRST_RANK[this.tileCoordinate]) {
                if (this.tileCoordinate % 2 != 0) {
                    setBackground(lightColouredTile);
                }
                else {
                    setBackground(darkColouredTile);
                }
            }
        }

    }

}
