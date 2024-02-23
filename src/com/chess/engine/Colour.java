package com.chess.engine;

import com.chess.engine.board.Board;
import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerBlack;
import com.chess.engine.player.PlayerWhite;

public enum Colour {
    BLACK {
        @Override
        public boolean isPawnPromotionTile(int location) {
            return Board.FIRST_RANK[location];
        }

        @Override
        public int directionColour() {
            return 1;
        }

        @Override
        public int oppositeDirectionColour() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player selectPlayer(PlayerWhite playerWhite, PlayerBlack playerBlack) {
            return playerBlack;
        }
    }, WHITE {
        @Override
        public boolean isPawnPromotionTile(int location) {
            return Board.EIGHTH_RANK[location];
        }

        @Override
        public int directionColour() {
            return -1;
        }

        @Override
        public int oppositeDirectionColour() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player selectPlayer(PlayerWhite playerWhite, PlayerBlack playerBlack) {
            return playerWhite;
        }
    };

    public abstract boolean isPawnPromotionTile(int location);
    public abstract int directionColour();
    public abstract int oppositeDirectionColour();
    public abstract boolean isWhite();
    public abstract boolean isBlack();

    public abstract Player selectPlayer(PlayerWhite playerWhite, PlayerBlack playerBlack);
}
