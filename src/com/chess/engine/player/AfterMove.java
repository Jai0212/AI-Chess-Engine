package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class AfterMove {

    Board afterBoard;
    Move move;
    MoveStatus moveStatus;

    public AfterMove(Board afterBoard, Move move, MoveStatus moveStatus) {
        this.afterBoard = afterBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getAfterBoard() {
        return this.afterBoard;
    }
}
