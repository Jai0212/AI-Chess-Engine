package com.chess.gui;

import com.chess.engine.Colour;
import com.chess.engine.player.Player;

import javax.swing.*;
import java.awt.*;

public class ChessGameInitialization extends JDialog {

    private Table.PlayerType whitePlayerType;
    private Table.PlayerType blackPlayerType;
    public JSpinner AIDepthSpinner;

    ChessGameInitialization(final JFrame frame, final boolean modal) {

        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton("Human");
        final JRadioButton whiteComputerButton = new JRadioButton("Computer");
        final JRadioButton blackHumanButton = new JRadioButton("Human");
        final JRadioButton blackComputerButton = new JRadioButton("Computer");
        whiteHumanButton.setActionCommand("Human");

        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        myPanel.add(new JLabel("AI"));
        this.AIDepthSpinner = addLabeledSpinner(myPanel, new SpinnerNumberModel(4, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("Ok");

        okButton.addActionListener(e -> {
            whitePlayerType = whiteComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
            blackPlayerType = blackComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
            ChessGameInitialization.this.setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            System.out.println("Cancel");
            ChessGameInitialization.this.setVisible(false);
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player) {
        if(player.getColour() == Colour.WHITE) {
            return getWhitePlayerType() == Table.PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == Table.PlayerType.COMPUTER;
    }

    Table.PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    Table.PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    private static JSpinner addLabeledSpinner(final Container c, final SpinnerModel model) {

        final JLabel l = new JLabel("Depth of AI");
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    int getDepthOfAI() {
        return (Integer)this.AIDepthSpinner.getValue();
    }
}