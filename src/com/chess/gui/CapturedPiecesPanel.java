package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CapturedPiecesPanel extends JPanel {

    JPanel northPanel;
    JPanel southPanel;

    static Color panelColour = Color.decode("#FFF8E1");
    static EtchedBorder panelBorder = new EtchedBorder(EtchedBorder.RAISED);
    static Dimension capturedPieceDimension = new Dimension(80, 80);

    public CapturedPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(panelColour);
        this.setBorder(panelBorder);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(panelColour);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel.setBackground(panelColour);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(capturedPieceDimension);
    }

    public void redo(Table.LogMoves logMoves) {

        this.northPanel.removeAll();
        this.southPanel.removeAll();

        List<Piece> whiteCapturedPieces = new ArrayList<>();
        List<Piece> blackCapturedPieces = new ArrayList<>();

        for (Move move : logMoves.getMovesInLog()) {

            if (move.isAttack()) {
                Piece capturedPiece = move.getAttackedPiece();

                if (capturedPiece.getPieceColour().isWhite()) {
                    whiteCapturedPieces.add(capturedPiece);
                }
                else {
                    blackCapturedPieces.add(capturedPiece);
                }
            }
        }

        whiteCapturedPieces.sort(Comparator.comparingInt(Piece::getPieceValue));

        blackCapturedPieces.sort(Comparator.comparingInt(Piece::getPieceValue));

        for (Piece capturedPiece: whiteCapturedPieces) {
            try {
                BufferedImage img = ImageIO.read(new File("ImagesChess/ChessPiecesImages/" + capturedPiece.getPieceColour().toString().charAt(0) + capturedPiece + ".png"));
                ImageIcon icon = new ImageIcon(img);
                JLabel imgLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imgLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Piece capturedPiece: blackCapturedPieces) {
            try {
                BufferedImage img = ImageIO.read(new File("ImagesChess/ChessPiecesImages/" + capturedPiece.getPieceColour().toString().charAt(0) + capturedPiece + ".png"));
                ImageIcon icon = new ImageIcon(img);
                JLabel imgLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imgLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        validate();
    }
}
