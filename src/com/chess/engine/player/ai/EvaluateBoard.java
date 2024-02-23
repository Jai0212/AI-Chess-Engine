package com.chess.engine.player.ai;

import com.chess.engine.board.Board;

public interface EvaluateBoard {

    int evaluate(Board board, int depth);
}
