package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogPanel extends JPanel {

    DataModel model;
    JScrollPane scrollPane;
    static Dimension logPanelDimension = new Dimension(100, 400);

    LogPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(logPanelDimension);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    void redo(Board board, Table.LogMoves logMoves) {

        int currRow = 0;
        this.model.clear();

        for (Move move : logMoves.getMovesInLog()) {
            String moveText = move.toString();

            if (move.getPieceMoved().getPieceColour().isWhite()) {
                this.model.setValueAt(moveText, currRow, 0);
            }
            else if (move.getPieceMoved().getPieceColour().isBlack()) {
                this.model.setValueAt(moveText, currRow, 1);
                currRow++;
            }
        }

        if (!logMoves.getMovesInLog().isEmpty()) {
            Move lastMove = logMoves.getMovesInLog().get(logMoves.sizeOfLog() - 1);
            String moveText = lastMove.toString();

            if (lastMove.getPieceMoved().getPieceColour().isWhite()) {
                this.model.setValueAt(moveText + computeCheckAndCheckMateHash(board), currRow, 0);
            }
            else if (lastMove.getPieceMoved().getPieceColour().isBlack()) {
                this.model.setValueAt(moveText + computeCheckAndCheckMateHash(board), currRow - 1, 1);
            }
        }

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    private String computeCheckAndCheckMateHash(Board board) {
        if (board.currPlayer().isCheckMate()) {
            return "#";
        }
        else if (board.currPlayer().isCheck()) {
            return "+";
        }
        else {
            return "";
        }
    }

    private static class DataModel extends DefaultTableModel {

        List<Row> values;
        static String[] names = {"White", "Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int row, int column) {

            Row currRow = this.values.get(row);

            if (column == 0) {
                return currRow.getWhiteMove();
            }
            else if (column == 1) {
                return currRow.getBlackMove();
            }
            else {
                return null;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            Row currRow;

            if (this.values.size() <= row) {
                currRow = new Row();
                this.values.add(currRow);
            }
            else {
                currRow = this.values.get(row);
            }

            if (column == 0) {
                currRow.setWhiteMove((String) value);
                fireTableRowsInserted(row, row);
            }
            else if (column == 1) {
                currRow.setBlackMove((String) value);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(int column) {
            return names[column];
        }
    }

    private static class Row {

        String whiteMove;
        String blackMove;

        Row() {
        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public void setWhiteMove(String move) {
            this.whiteMove = move;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setBlackMove(String move) {
            this.blackMove = move;
        }
    }
}
