package com.chess.engine.player;

public enum MoveStatus {

    COMPLETED {
        @Override
        public boolean isCompleted() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isCompleted() {
            return false;
        }
    },

    PUTS_PLAYER_IN_CHECK {
        @Override
        public boolean isCompleted() {
            return false;
        }
    };

    public abstract boolean isCompleted();
}
